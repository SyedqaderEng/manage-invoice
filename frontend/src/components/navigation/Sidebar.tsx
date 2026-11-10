import { CreditCard, LayoutDashboard, LogOut, Settings, Shield, Users } from 'lucide-react'

interface Workspace {
  id: string
  name: string
  role: string
}

interface SidebarProps {
  workspaces: Workspace[]
  activeWorkspaceId?: string
  onWorkspaceChange: (workspaceId: string) => void
  onLogout: () => void
  mobileOpen: boolean
  onClose: () => void
  activeSection?: string
  onNavigate: (section: string) => void
}

const items = [
  { label: 'Overview', section: 'overview', icon: LayoutDashboard },
  { label: 'Team', section: 'team', icon: Users },
  { label: 'Billing', section: 'billing', icon: CreditCard },
  { label: 'Account', section: 'account', icon: Settings },
  { label: 'Admin', section: 'admin', icon: Shield },
]

export function Sidebar(props: SidebarProps) {
  return (
    <aside className={`sidebar ${props.mobileOpen ? 'mobile-open' : ''}`}>
      <div className="brand"><span className="brand-mark">S</span><span>SaaS Starter</span></div>
      <label className="workspace-switcher">
        <span>Workspace</span>
        <select value={props.activeWorkspaceId || ''} onChange={(event) => props.onWorkspaceChange(event.target.value)} aria-label="Select workspace">
          {props.workspaces.map((workspace) => <option key={workspace.id} value={workspace.id}>{workspace.name}</option>)}
        </select>
      </label>
      <nav className="sidebar-nav" aria-label="Primary navigation">
        {items.map(({ label, section, icon: Icon }) => (
          <button
            className={`nav-item ${props.activeSection === section ? 'active' : ''}`}
            key={section}
            onClick={() => { props.onNavigate(section); props.onClose() }}
          >
            <Icon size={18} aria-hidden="true" /><span>{label}</span>
          </button>
        ))}
      </nav>
      <nav className="sidebar-nav secondary-nav" aria-label="Account actions">
        <button className="nav-item nav-button" onClick={props.onLogout}><LogOut size={18} />Sign out</button>
      </nav>
    </aside>
  )
}
