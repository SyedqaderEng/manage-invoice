import { Menu } from 'lucide-react'
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
      <div className="topbar-actions">
        <span className="user-identity"><strong>{userName}</strong><small>{workspaceName}</small></span>
        <Avatar name={userName} />
      </div>
    </header>
  )
}
