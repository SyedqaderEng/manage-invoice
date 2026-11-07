import { FormEvent, useEffect, useState } from 'react'
import { FilePlus2, Plus, Upload } from 'lucide-react'
import { AuthScreen } from './features/auth/AuthScreen'
import { Sidebar } from './components/navigation/Sidebar'
import { TopBar } from './components/navigation/TopBar'
import { Button } from './components/ui/Button'
import { StatusBadge } from './components/ui/StatusBadge'

interface User { id: string; email: string; firstName: string; lastName: string }
interface Workspace { id: string; name: string; workspaceType: 'PERSONAL' | 'BUSINESS' | 'ACCOUNTANT'; defaultCurrency: string; role: string }
interface AuthResponse { accessToken: string; user: User }

const api = async <T,>(path: string, options: RequestInit = {}, token?: string): Promise<T> => {
  const headers = new Headers(options.headers)
  if (!headers.has('Content-Type')) headers.set('Content-Type', 'application/json')
  if (token) headers.set('Authorization', `Bearer ${token}`)
  const response = await fetch(path, { ...options, headers })
  if (!response.ok) {
    const body = await response.json().catch(() => null)
    throw new Error(body?.detail || body?.message || 'Request failed')
  }
  return response.json() as Promise<T>
}

export function App() {
  const [token, setToken] = useState(() => localStorage.getItem('manageInvoiceToken') || '')
  const [user, setUser] = useState<User | null>(null)
  const [workspaces, setWorkspaces] = useState<Workspace[]>([])
  const [activeWorkspace, setActiveWorkspace] = useState<Workspace | null>(() => {
    const saved = localStorage.getItem('manageInvoiceWorkspace')
    return saved ? JSON.parse(saved) as Workspace : null
  })
  const [mobileOpen, setMobileOpen] = useState(false)
  const [error, setError] = useState('')
  const [workspaceName, setWorkspaceName] = useState('')

  useEffect(() => {
    if (!token) return
    api<User>('/api/v1/auth/me', {}, token).then(setUser).catch(() => logout())
    api<Workspace[]>('/api/v1/workspaces', {}, token).then((items) => {
      setWorkspaces(items)
      if (!activeWorkspace && items.length) selectWorkspace(items[0])
      if (activeWorkspace && !items.some((item) => item.id === activeWorkspace.id)) selectWorkspace(items[0] || null)
    }).catch((reason: Error) => setError(reason.message))
  }, [token])

  const selectWorkspace = (workspace: Workspace | null) => {
    setActiveWorkspace(workspace)
    if (workspace) localStorage.setItem('manageInvoiceWorkspace', JSON.stringify(workspace))
    else localStorage.removeItem('manageInvoiceWorkspace')
  }

  const authenticated = (response: AuthResponse) => {
    localStorage.setItem('manageInvoiceToken', response.accessToken)
    setToken(response.accessToken)
    setUser(response.user)
  }

  const logout = () => {
    localStorage.removeItem('manageInvoiceToken')
    localStorage.removeItem('manageInvoiceWorkspace')
    setToken('')
    setUser(null)
    setWorkspaces([])
    selectWorkspace(null)
  }

  const createWorkspace = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError('')
    try {
      const workspace = await api<Workspace>('/api/v1/workspaces', { method: 'POST', body: JSON.stringify({ name: workspaceName, workspaceType: 'BUSINESS' }) }, token)
      setWorkspaces((items) => [...items, workspace])
      selectWorkspace(workspace)
      setWorkspaceName('')
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to create workspace')
    }
  }

  if (!token) return <AuthScreen api={api} onAuthenticated={authenticated} />

  const userName = user ? `${user.firstName} ${user.lastName}` : 'Account'

  return (
    <div className="app-shell">
      {mobileOpen ? <button className="sidebar-backdrop" aria-label="Close navigation" onClick={() => setMobileOpen(false)} /> : null}
      <Sidebar workspaces={workspaces} activeWorkspaceId={activeWorkspace?.id} onWorkspaceChange={(id) => selectWorkspace(workspaces.find((item) => item.id === id) || null)} onLogout={logout} mobileOpen={mobileOpen} onClose={() => setMobileOpen(false)} />
      <div className="main-column">
        <TopBar userName={userName} workspaceName={activeWorkspace?.name || 'No workspace'} onMenu={() => setMobileOpen(true)} />
        <main className="page-content">
          <div className="page-heading">
            <div><p>Overview</p><h1>{activeWorkspace ? `Good morning, ${user?.firstName || 'there'}` : 'Workspace setup'}</h1></div>
            <Button icon={<Upload size={18} />} disabled={!activeWorkspace}>Upload document</Button>
          </div>

          {!activeWorkspace ? (
            <section className="workspace-empty">
              <span className="empty-icon"><FilePlus2 size={22} /></span>
              <h2>Create your first workspace</h2>
              <p>Workspaces keep documents and financial records isolated for each business or client.</p>
              <form onSubmit={createWorkspace} className="inline-create">
                <input aria-label="Workspace name" value={workspaceName} onChange={(event) => setWorkspaceName(event.target.value)} placeholder="Workspace name" required />
                <Button type="submit" icon={<Plus size={18} />}>Create workspace</Button>
              </form>
              {error ? <p className="form-error" role="alert">{error}</p> : null}
            </section>
          ) : (
            <>
              <section className="summary-grid" aria-label="Workspace summary">
                <article className="summary-panel"><span>Current workspace</span><strong>{activeWorkspace.name}</strong><StatusBadge label="Active" tone="success" /></article>
                <article className="summary-panel"><span>Default currency</span><strong>{activeWorkspace.defaultCurrency}</strong><small>Workspace setting</small></article>
                <article className="summary-panel"><span>Your role</span><strong>{activeWorkspace.role}</strong><small>Workspace access</small></article>
              </section>
              <section className="activity-panel">
                <div className="section-heading"><div><h2>Getting started</h2><p>Phase 2 shell complete. Document and reporting activity will appear as their REST APIs are implemented.</p></div></div>
                <div className="empty-state"><span className="empty-icon"><Upload size={22} /></span><h3>No processing activity</h3><p>Uploaded documents and processing status will appear here.</p></div>
              </section>
            </>
          )}
        </main>
      </div>
    </div>
  )
}
