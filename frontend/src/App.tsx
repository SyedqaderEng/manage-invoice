import { FormEvent, useEffect, useState } from 'react'
import { Plus } from 'lucide-react'
import { AuthScreen } from './features/auth/AuthScreen'
import { Sidebar } from './components/navigation/Sidebar'
import { TopBar } from './components/navigation/TopBar'
import { Button } from './components/ui/Button'
import { LandingPage } from './features/landing/LandingPage'
import { formatPlanPrice } from './features/billing/pricing'

interface User { id: string; email: string; firstName: string; lastName: string }
interface Workspace { id: string; name: string; workspaceType: string; role: string }
interface Member { id: string; email: string; role: string }
interface Plan { id: string; name: string; description: string; monthlyPrice: number; yearlyPrice: number }
interface Subscription { status: string; plan: string; currentPeriodEnd?: string }
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
  return response.status === 204 ? undefined as T : response.json() as Promise<T>
}
type Api = typeof api

export function App() {
  const [token, setToken] = useState(() => localStorage.getItem('saasStarterToken') || '')
  const [user, setUser] = useState<User | null>(null)
  const [workspaces, setWorkspaces] = useState<Workspace[]>([])
  const [activeWorkspace, setActiveWorkspace] = useState<Workspace | null>(() => {
    const saved = localStorage.getItem('saasStarterWorkspace')
    return saved ? JSON.parse(saved) as Workspace : null
  })
  const [workspaceName, setWorkspaceName] = useState('')
  const [error, setError] = useState('')
  const [mobileOpen, setMobileOpen] = useState(false)
  const [section, setSection] = useState(() => window.location.hash.slice(1) || 'overview')
  const [pathname, setPathname] = useState(() => window.location.pathname)

  const navigate = (path: string) => {
    window.history.pushState({}, '', path)
    setPathname(window.location.pathname)
    setSection(window.location.hash.slice(1) || 'overview')
  }

  useEffect(() => {
    const onPopState = () => {
      setPathname(window.location.pathname)
      setSection(window.location.hash.slice(1) || 'overview')
    }
    window.addEventListener('popstate', onPopState)
    return () => window.removeEventListener('popstate', onPopState)
  }, [])

  useEffect(() => {
    const onHashChange = () => setSection(window.location.hash.slice(1) || 'overview')
    window.addEventListener('hashchange', onHashChange)
    return () => window.removeEventListener('hashchange', onHashChange)
  }, [])

  useEffect(() => {
    if (!token) return
    api<User>('/api/v1/auth/me', {}, token).then(setUser).catch(() => logout())
    api<Workspace[]>('/api/v1/workspaces', {}, token).then((items) => {
      setWorkspaces(items)
      const saved = items.find((item) => item.id === activeWorkspace?.id)
      if (saved) setActiveWorkspace(saved)
      else if (items.length) setActiveWorkspace(items[0])
      else setActiveWorkspace(null)
    }).catch((reason: Error) => setError(reason.message))
  }, [token])

  useEffect(() => {
    if (token && ['/', '/login', '/register', '/pricing'].includes(pathname)) navigate('/app')
  }, [token, pathname])

  useEffect(() => {
    if (activeWorkspace) localStorage.setItem('saasStarterWorkspace', JSON.stringify(activeWorkspace))
    else localStorage.removeItem('saasStarterWorkspace')
  }, [activeWorkspace])

  const authenticated = (response: AuthResponse) => {
    localStorage.setItem('saasStarterToken', response.accessToken)
    setToken(response.accessToken)
    setUser(response.user)
    navigate('/app')
  }

  function logout() {
    localStorage.removeItem('saasStarterToken')
    localStorage.removeItem('saasStarterWorkspace')
    setToken('')
    setUser(null)
    setWorkspaces([])
    setActiveWorkspace(null)
    navigate('/')
  }

  const createWorkspace = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError('')
    try {
      const workspace = await api<Workspace>('/api/v1/workspaces', {
        method: 'POST',
        body: JSON.stringify({ name: workspaceName, workspaceType: 'ORGANIZATION' }),
      }, token)
      setWorkspaces((items) => [...items, workspace])
      setActiveWorkspace(workspace)
      setWorkspaceName('')
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to create workspace')
    }
  }

  if (!token && (pathname === '/login' || pathname === '/register')) {
    return <AuthScreen api={api} onAuthenticated={authenticated} initialMode={pathname === '/register' ? 'register' : 'login'} onNavigate={navigate} />
  }
  if (!token) return <LandingPage onNavigate={navigate} api={api} />

  const userName = user ? `${user.firstName} ${user.lastName}` : 'Account'
  return (
    <div className="app-shell">
      {mobileOpen ? <button className="sidebar-backdrop" aria-label="Close navigation" onClick={() => setMobileOpen(false)} /> : null}
      <Sidebar
        workspaces={workspaces}
        activeWorkspaceId={activeWorkspace?.id}
        onWorkspaceChange={(id) => setActiveWorkspace(workspaces.find((item) => item.id === id) || null)}
        onLogout={logout}
        mobileOpen={mobileOpen}
        onClose={() => setMobileOpen(false)}
        activeSection={section}
        onNavigate={(target) => navigate(`/app#${target}`)}
      />
      <div className="main-column">
        <TopBar userName={userName} workspaceName={activeWorkspace?.name || 'No workspace'} onMenu={() => setMobileOpen(true)} />
        <main className="page-content">
          {error ? <p className="form-error" role="alert">{error}</p> : null}
          {section === 'account' ? (
            <AccountSettings user={user} token={token} api={api} onSaved={setUser} />
          ) : section === 'team' || section === 'admin' ? (
            <WorkspaceAdmin workspace={activeWorkspace} token={token} api={api} />
          ) : section === 'billing' ? (
            <BillingSettings workspace={activeWorkspace} token={token} api={api} />
          ) : (
            <>
              <div className="page-heading"><div><p>Workspace</p><h1>{activeWorkspace ? `Welcome, ${user?.firstName || 'there'}` : 'Set up your workspace'}</h1></div></div>
              {!activeWorkspace ? (
                <section className="workspace-empty">
                  <h2>Create your first workspace</h2>
                  <p>Workspaces keep your organization and its members together.</p>
                  <form onSubmit={createWorkspace} className="inline-create">
                    <input aria-label="Workspace name" value={workspaceName} onChange={(event) => setWorkspaceName(event.target.value)} placeholder="Workspace name" required />
                    <Button type="submit" icon={<Plus size={18} />}>Create workspace</Button>
                  </form>
                </section>
              ) : (
                <section className="summary-grid" aria-label="Workspace overview">
                  <article className="summary-panel"><span>Workspace</span><strong>{activeWorkspace.name}</strong></article>
                  <article className="summary-panel"><span>Your role</span><strong>{activeWorkspace.role}</strong></article>
                  <article className="summary-panel"><span>Plan</span><strong><a href="/app#billing" onClick={(event) => { event.preventDefault(); navigate('/app#billing') }}>Manage billing</a></strong></article>
                </section>
              )}
            </>
          )}
        </main>
      </div>
    </div>
  )
}

function AccountSettings({ user, token, api, onSaved }: {
  user: User | null
  token: string
  api: Api
  onSaved: (user: User) => void
}) {
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const save = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const form = new FormData(event.currentTarget)
    setError('')
    setMessage('')
    try {
      const updated = await api<User>('/api/v1/auth/me', {
        method: 'PATCH',
        body: JSON.stringify({ firstName: form.get('firstName'), lastName: form.get('lastName') }),
      }, token)
      onSaved(updated)
      setMessage('Account settings saved.')
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to save settings')
    }
  }
  return (
    <section className="settings-card">
      <div className="page-heading"><div><p>Account</p><h1>Profile settings</h1></div></div>
      <form className="settings-form" onSubmit={save}>
        <label className="field">Email address<input value={user?.email || ''} readOnly /></label>
        <label className="field">First name<input name="firstName" defaultValue={user?.firstName || ''} required /></label>
        <label className="field">Last name<input name="lastName" defaultValue={user?.lastName || ''} required /></label>
        {error ? <p className="form-error" role="alert">{error}</p> : null}
        {message ? <p role="status">{message}</p> : null}
        <Button type="submit">Save changes</Button>
      </form>
    </section>
  )
}

function WorkspaceAdmin({ workspace, token, api }: {
  workspace: Workspace | null
  token: string
  api: Api
}) {
  const [members, setMembers] = useState<Member[]>([])
  const [error, setError] = useState('')
  const [email, setEmail] = useState('')
  const [saving, setSaving] = useState(false)
  const reload = () => {
    if (!workspace) return
    api<Member[]>(`/api/v1/workspaces/${workspace.id}/members`, {}, token)
      .then(setMembers).catch((reason: Error) => setError(reason.message))
  }
  useEffect(() => {
    reload()
  }, [workspace?.id])
  const addMember = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!workspace) return
    setSaving(true)
    setError('')
    try {
      await api(`/api/v1/workspaces/${workspace.id}/members`, {
        method: 'POST',
        body: JSON.stringify({ email, role: 'MEMBER' }),
      }, token)
      setEmail('')
      reload()
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to add member')
    } finally {
      setSaving(false)
    }
  }
  const updateRole = async (memberId: string, role: string) => {
    if (!workspace) return
    setError('')
    try {
      await api(`/api/v1/workspaces/${workspace.id}/members/${memberId}`, {
        method: 'PATCH',
        body: JSON.stringify({ role }),
      }, token)
      reload()
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to update role')
    }
  }
  const removeMember = async (memberId: string) => {
    if (!workspace) return
    setError('')
    try {
      await api(`/api/v1/workspaces/${workspace.id}/members/${memberId}`, { method: 'DELETE' }, token)
      reload()
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to remove member')
    }
  }
  return (
    <section className="settings-card">
      <div className="page-heading"><div><p>Workspace administration</p><h1>{workspace?.name || 'Workspace'}</h1></div></div>
      <p className="settings-copy">Add existing accounts and manage workspace roles. Workspace ownership and administrator permissions are enforced by the API.</p>
      {error ? <p className="form-error" role="alert">{error}</p> : null}
      {workspace && ['OWNER', 'ADMIN'].includes(workspace.role) ? (
        <form className="inline-create" onSubmit={addMember}>
          <input type="email" aria-label="Existing account email" placeholder="Existing account email" value={email} onChange={(event) => setEmail(event.target.value)} required />
          <Button type="submit" disabled={saving}>Add member</Button>
        </form>
      ) : null}
      <div className="member-list">{members.map((member) => (
        <div className="member-row" key={member.id}>
          <span>{member.email}</span>
          {member.role === 'OWNER' ? <strong>Owner</strong> : (
            <div className="member-controls">
              <select aria-label={`Role for ${member.email}`} value={member.role} onChange={(event) => updateRole(member.id, event.target.value)}>
                <option value="MEMBER">Member</option><option value="ADMIN">Admin</option>
              </select>
              <button type="button" className="text-button" onClick={() => removeMember(member.id)}>Remove</button>
            </div>
          )}
        </div>
      ))}</div>
      {!workspace ? <p>Create a workspace to manage its team.</p> : null}
    </section>
  )
}

function BillingSettings({ workspace, token, api }: {
  workspace: Workspace | null
  token: string
  api: Api
}) {
  const [plans, setPlans] = useState<Plan[]>([])
  const [subscription, setSubscription] = useState<Subscription | null>(null)
  const [interval, setInterval] = useState<'monthly' | 'yearly'>('monthly')
  const [error, setError] = useState('')
  useEffect(() => {
    api<Plan[]>('/api/v1/billing/plans').then(setPlans).catch((reason: Error) => setError(reason.message))
    if (workspace) api<Subscription>(`/api/v1/billing/workspaces/${workspace.id}`, {}, token)
      .then(setSubscription).catch(() => setSubscription(null))
  }, [workspace?.id])
  const startCheckout = async (planId: string) => {
    if (!workspace) return
    setError('')
    try {
      const result = await api<{ url: string }>(`/api/v1/billing/workspaces/${workspace.id}/checkout`, {
        method: 'POST',
        body: JSON.stringify({ planId, interval }),
      }, token)
      window.location.assign(result.url)
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to start checkout')
    }
  }
  const openPortal = async () => {
    if (!workspace) return
    try {
      const result = await api<{ url: string }>(`/api/v1/billing/workspaces/${workspace.id}/portal`, { method: 'POST' }, token)
      window.location.assign(result.url)
    } catch (reason) {
      setError(reason instanceof Error ? reason.message : 'Unable to open billing portal')
    }
  }
  return (
    <section className="settings-card">
      <div className="page-heading"><div><p>Subscription</p><h1>Plans and billing</h1></div></div>
      <p className="settings-copy">{subscription ? `Current subscription: ${subscription.plan} (${subscription.status}).` : 'Choose a plan for your workspace.'}</p>
      <div className="billing-interval" role="group" aria-label="Billing interval">
        <button className={interval === 'monthly' ? 'selected' : ''} onClick={() => setInterval('monthly')}>Monthly</button>
        <button className={interval === 'yearly' ? 'selected' : ''} onClick={() => setInterval('yearly')}>Yearly</button>
      </div>
      {error ? <p className="form-error" role="alert">{error}</p> : null}
      <div className="plan-grid">{plans.map((plan) => (
        <article className="plan-card" key={plan.id}>
          <h2>{plan.name}</h2><p>{plan.description}</p>
          <strong>{formatPlanPrice(interval === 'monthly' ? plan.monthlyPrice : plan.yearlyPrice)}<small> / {interval === 'monthly' ? 'month' : 'year'}</small></strong>
          <Button disabled={!workspace} onClick={() => startCheckout(plan.id)}>Choose {plan.name}</Button>
        </article>
      ))}</div>
      {subscription ? <Button variant="secondary" onClick={openPortal}>Manage subscription</Button> : null}
    </section>
  )
}
