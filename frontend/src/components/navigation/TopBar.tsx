import { Bell, Menu, Search } from 'lucide-react'
import { Avatar } from '../ui/Avatar'

interface TopBarProps {
  userName: string
  workspaceName: string
  onMenu: () => void
}

export function TopBar({ userName, workspaceName, onMenu }: TopBarProps) {
  return (
    <header className="topbar">
      <button className="icon-button mobile-menu" aria-label="Open navigation" onClick={onMenu}><Menu size={20} /></button>
      <label className="global-search">
        <Search size={18} aria-hidden="true" />
        <input aria-label="Search" placeholder="Search records" />
        <kbd>/</kbd>
      </label>
      <div className="topbar-actions">
        <button className="icon-button" aria-label="Notifications"><Bell size={19} /></button>
        <span className="user-identity"><strong>{userName}</strong><small>{workspaceName}</small></span>
        <Avatar name={userName} />
      </div>
    </header>
  )
}
