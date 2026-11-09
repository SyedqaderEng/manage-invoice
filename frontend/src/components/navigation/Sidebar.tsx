import {
  BarChart3, FileText, FolderKanban, LayoutDashboard, ReceiptText,
  Settings, Tags, Upload, Users, WalletCards
} from 'lucide-react'

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
}

const primaryItems = [
  { label: 'Overview', icon: LayoutDashboard, active: true },
  { label: 'Documents', icon: FileText },
  { label: 'Expenses', icon: ReceiptText },
  { label: 'Invoices', icon: WalletCards },
  { label: 'Vendors', icon: FolderKanban },
  { label: 'Categories', icon: Tags },
  { label: 'Reports', icon: BarChart3 },
  { label: 'Exports', icon: Upload }
]

export function Sidebar(props: SidebarProps) {
  return (
    <aside className={`sidebar ${props.mobileOpen ? 'mobile-open' : ''}`}>
      <div className="brand"><span className="brand-mark">M</span><span>Manage Invoice</span></div>
      <label className="workspace-switcher">
        <span>Workspace</span>
        <select value={props.activeWorkspaceId || ''} onChange={(event) => props.onWorkspaceChange(event.target.value)}>
          {props.workspaces.map((workspace) => <option key={workspace.id} value={workspace.id}>{workspace.name}</option>)}
        </select>
      </label>
      <nav className="sidebar-nav" aria-label="Primary navigation">
        {primaryItems.map(({ label, icon: Icon, active }) => (
          <a className={(props.activeSection === label.toLowerCase() || (!props.activeSection && label === 'Overview')) ? 'nav-item active' : 'nav-item'} href={label === 'Overview' ? '#' : `#${label.toLowerCase()}`} key={label} onClick={props.onClose}>
            <Icon size={18} aria-hidden="true" /><span>{label}</span>
            {!active && label !== 'Overview' ? null : null}
          </a>
        ))}
      </nav>
      <nav className="sidebar-nav secondary-nav" aria-label="Workspace settings">
        <a className="nav-item" href="#team"><Users size={18} />Team</a>
        <a className="nav-item" href="#settings"><Settings size={18} />Settings</a>
        <button className="nav-item nav-button" onClick={props.onLogout}>Sign out</button>
      </nav>
    </aside>
  )
}
