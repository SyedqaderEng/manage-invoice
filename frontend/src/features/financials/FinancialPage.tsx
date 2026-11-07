import { FormEvent, useEffect, useState } from 'react'
import { Plus } from 'lucide-react'
import { Button } from '../../components/ui/Button'

interface FinancialPageProps {
  section: string
  workspaceId?: string
  token: string
  api: <T>(path: string, options?: RequestInit, token?: string) => Promise<T>
}

interface Row { id: string; name?: string; displayName?: string; description?: string; vendorId?: string; invoiceNumber?: string; transactionDate?: string; invoiceDate?: string; totalAmount?: string; amount?: string; currency?: string; status?: string }

const sections: Record<string, { title: string; path: string }> = {
  expenses: { title: 'Expenses', path: '/api/v1/expenses' },
  invoices: { title: 'Invoices', path: '/api/v1/invoices' },
  vendors: { title: 'Vendors', path: '/api/v1/vendors' },
  categories: { title: 'Categories', path: '/api/v1/categories' }
}

export function FinancialPage({ section, workspaceId, token, api }: FinancialPageProps) {
  const config = sections[section]
  const [rows, setRows] = useState<Row[]>([])
  const [newName, setNewName] = useState('')
  const [error, setError] = useState('')

  const headers = { 'Content-Type': 'application/json', 'X-Workspace-Id': workspaceId || '' }
  const load = async () => {
    if (!workspaceId || !config) return
    try {
      const result = await api<{ content?: Row[] } | Row[]>(config.path, { headers }, token)
      setRows(Array.isArray(result) ? result : result.content || [])
      setError('')
    } catch (reason) { setError(reason instanceof Error ? reason.message : 'Unable to load records') }
  }
  useEffect(() => { load() }, [section, workspaceId, token])

  const create = async (event: FormEvent) => {
    event.preventDefault()
    if (!workspaceId) return
    try {
      const payload = section === 'vendors' ? { name: newName } : { name: newName }
      await api(config.path, { method: 'POST', headers, body: JSON.stringify(payload) }, token)
      setNewName(''); await load()
    } catch (reason) { setError(reason instanceof Error ? reason.message : 'Unable to create record') }
  }

  if (!config) return null
  const label = section === 'expenses' ? 'Expense description' : section === 'invoices' ? 'Invoice number' : `${config.title.replace(/s$/, '')} name`

  return (
    <>
      <div className="page-heading"><div><p>{config.title}</p><h1>{config.title}</h1></div></div>
      {section !== 'expenses' && section !== 'invoices' ? <form className="inline-create financial-create" onSubmit={create}>
        <input aria-label={label} value={newName} onChange={(event) => setNewName(event.target.value)} placeholder={label} required />
        <Button type="submit" icon={<Plus size={18} />}>Add {config.title.replace(/s$/, '').toLowerCase()}</Button>
      </form> : <div className="financial-notice">Create and payment workflows are available through the REST API. This view displays backend records only.</div>}
      {error ? <p className="form-error" role="alert">{error}</p> : null}
      <section className="document-panel financial-panel">
        <div className="section-heading"><h2>{config.title}</h2></div>
        {rows.length === 0 ? <div className="empty-state"><h3>No {config.title.toLowerCase()}</h3><p>Records created by the backend will appear here.</p></div> : <div className="financial-table">
          {rows.map((row) => <div className="financial-row" key={row.id}><strong>{row.displayName || row.name || row.description || row.invoiceNumber || row.id}</strong><span>{row.transactionDate || row.invoiceDate || row.currency || row.status || 'Record'}</span><span>{row.totalAmount || row.amount || ''}</span></div>)}
        </div>}
      </section>
    </>
  )
}
