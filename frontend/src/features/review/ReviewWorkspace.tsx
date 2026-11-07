import { FormEvent, useEffect, useState } from 'react'
import { Check, FileText, Save, X } from 'lucide-react'
import { Button } from '../../components/ui/Button'
import { Field } from '../../components/ui/Field'

interface ReviewWorkspaceProps {
  documentId: string
  workspaceId?: string
  token: string
  api: <T>(path: string, options?: RequestInit, token?: string) => Promise<T>
}

interface ReviewState {
  documentId: string
  status: string
  originalData: string
  correctedData: string
}

export function ReviewWorkspace({ documentId, workspaceId, token, api }: ReviewWorkspaceProps) {
  const [review, setReview] = useState<ReviewState | null>(null)
  const [fields, setFields] = useState<Record<string, string>>({})
  const [notes, setNotes] = useState('')
  const [error, setError] = useState('')

  const headers = () => ({ 'Content-Type': 'application/json', 'X-Workspace-Id': workspaceId || '' })

  const load = async () => {
    if (!workspaceId) return
    try {
      const state = await api<ReviewState>(`/api/v1/documents/${documentId}/review`, { headers: headers() }, token)
      const source = JSON.parse(state.correctedData || state.originalData) as Record<string, unknown>
      setReview(state)
      setFields(Object.fromEntries(Object.entries(source).map(([key, value]) => [key, value == null ? '' : String(value)])))
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to load review')
    }
  }

  useEffect(() => { load() }, [documentId, workspaceId, token])

  const submit = async (event: FormEvent, action: 'save' | 'approve' | 'reject') => {
    event.preventDefault()
    setError('')
    try {
      const body = action === 'reject'
        ? { notes }
        : { correctedData: fields, notes }
      await api(`/api/v1/documents/${documentId}/review/${action}`, { method: 'POST', headers: headers(), body: JSON.stringify(body) }, token)
      await load()
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : `Unable to ${action} review`)
    }
  }

  return (
    <div className="review-layout">
      <div className="review-heading"><div><p>Human review</p><h1>Document extraction</h1></div><span>Document {documentId.slice(0, 8)}</span></div>
      <div className="review-grid">
        <section className="review-preview" aria-label="Original document preview">
          <div className="review-preview-bar"><FileText size={18} /><strong>Original document</strong></div>
          <div className="source-placeholder"><FileText size={34} /><strong>Secure document preview</strong><p>Source preview is unavailable until document viewer integration is added.</p></div>
        </section>
        <form className="review-form" onSubmit={(event) => submit(event, 'save')}>
          <div className="section-heading"><div><h2>Extracted fields</h2><p>Corrections are preserved separately from the original extraction.</p></div></div>
          <div className="review-fields">
            {['documentType', 'vendorName', 'documentNumber', 'documentDate', 'dueDate', 'currency', 'subtotal', 'tax', 'total'].map((field) => (
              <Field key={field} label={field.replace(/([A-Z])/g, ' $1').replace(/^./, (value) => value.toUpperCase())}
                value={fields[field] || ''} onChange={(event) => setFields((current) => ({ ...current, [field]: event.target.value }))} />
            ))}
          </div>
          <label className="review-notes">Review notes<textarea value={notes} onChange={(event) => setNotes(event.target.value)} /></label>
          {error ? <p className="form-error" role="alert">{error}</p> : null}
          {review ? <p className="review-state">Status: {review.status}</p> : null}
          <div className="review-actions">
            <Button type="submit" variant="secondary" icon={<Save size={18} />}>Save draft</Button>
            <Button type="button" variant="secondary" icon={<X size={18} />} onClick={(event) => submit(event, 'reject')}>Reject</Button>
            <Button type="button" icon={<Check size={18} />} onClick={(event) => submit(event, 'approve')}>Approve</Button>
          </div>
        </form>
      </div>
    </div>
  )
}
