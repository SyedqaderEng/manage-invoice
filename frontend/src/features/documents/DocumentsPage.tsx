import { FormEvent, useEffect, useRef, useState } from 'react'
import { FileText, Upload } from 'lucide-react'
import { Button } from '../../components/ui/Button'
import { StatusBadge } from '../../components/ui/StatusBadge'

interface DocumentItem {
  id: string
  originalFilename: string
  mimeType: string
  fileSize: number
  checksum: string
  processingStatus: string
  uploadedAt: string
}

interface DocumentsPageProps {
  token: string
  workspaceId?: string
  api: <T>(path: string, options?: RequestInit, token?: string) => Promise<T>
}

export function DocumentsPage({ token, workspaceId, api }: DocumentsPageProps) {
  const inputRef = useRef<HTMLInputElement>(null)
  const [documents, setDocuments] = useState<DocumentItem[]>([])
  const [error, setError] = useState('')
  const [uploading, setUploading] = useState(false)

  const loadDocuments = async () => {
    if (!workspaceId) return
    try {
      const response = await api<{ content: DocumentItem[] }>('/api/v1/documents', { headers: { 'X-Workspace-Id': workspaceId } }, token)
      setDocuments(response.content)
      setError('')
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to load documents')
    }
  }

  useEffect(() => { loadDocuments() }, [workspaceId, token])

  const upload = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const file = inputRef.current?.files?.[0]
    if (!file || !workspaceId) return
    const form = new FormData()
    form.append('file', file)
    setUploading(true)
    setError('')
    try {
      const headers = new Headers()
      headers.set('Authorization', `Bearer ${token}`)
      headers.set('X-Workspace-Id', workspaceId)
      const response = await fetch('/api/v1/documents', { method: 'POST', headers, body: form })
      if (!response.ok) {
        const body = await response.json().catch(() => null)
        throw new Error(body?.detail || body?.message || 'Upload failed')
      }
      await loadDocuments()
      if (inputRef.current) inputRef.current.value = ''
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Upload failed')
    } finally {
      setUploading(false)
    }
  }

  const formatSize = (bytes: number) => bytes < 1024 * 1024 ? `${Math.ceil(bytes / 1024)} KB` : `${(bytes / 1024 / 1024).toFixed(1)} MB`

  return (
    <>
      <div className="page-heading">
        <div><p>Documents</p><h1>Document library</h1></div>
      </div>
      <section className="upload-panel">
        <form onSubmit={upload}>
          <span className="empty-icon"><Upload size={22} /></span>
          <div><strong>Upload a document</strong><p>PDF, JPG, JPEG, PNG, or WEBP up to 25 MB.</p></div>
          <input ref={inputRef} name="file" type="file" accept=".pdf,.jpg,.jpeg,.png,.webp,application/pdf,image/jpeg,image/png,image/webp" required />
          <Button type="submit" disabled={uploading || !workspaceId} icon={<Upload size={18} />}>{uploading ? 'Uploading' : 'Upload'}</Button>
        </form>
        {error ? <p className="form-error" role="alert">{error}</p> : null}
      </section>
      <section className="document-panel">
        <div className="section-heading"><h2>Files</h2></div>
        {documents.length === 0 ? (
          <div className="empty-state"><span className="empty-icon"><FileText size={22} /></span><h3>No documents</h3><p>Uploaded files for this workspace will appear here.</p></div>
        ) : (
          <div className="document-table" role="table">
            <div className="document-table-head" role="row"><span>File</span><span>Size</span><span>Status</span><span>Uploaded</span></div>
            {documents.map((document) => (
              <div className="document-table-row" role="row" key={document.id}>
                <span><FileText size={17} />{document.originalFilename}</span>
                <span>{formatSize(document.fileSize)}</span>
                <StatusBadge label={document.processingStatus.replaceAll('_', ' ').toLowerCase()} tone="neutral" />
                <span>{new Date(document.uploadedAt).toLocaleDateString()}</span>
              </div>
            ))}
          </div>
        )}
      </section>
    </>
  )
}
