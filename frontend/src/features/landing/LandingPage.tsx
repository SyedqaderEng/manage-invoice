import { useEffect, useState } from 'react'
import { ArrowRight, Check, Layers3, ShieldCheck, Users } from 'lucide-react'
import { Button } from '../../components/ui/Button'
import { formatPlanPrice } from '../billing/pricing'

interface Plan {
  id: string
  name: string
  description: string
  monthlyPrice: number
  yearlyPrice: number
}

interface LandingPageProps {
  onNavigate: (path: string) => void
  api: <T>(path: string, options?: RequestInit, token?: string) => Promise<T>
}

export function LandingPage({ onNavigate, api }: LandingPageProps) {
  const [plans, setPlans] = useState<Plan[]>([])
  const [error, setError] = useState('')

  useEffect(() => {
    api<Plan[]>('/api/v1/billing/plans').then(setPlans).catch((reason: Error) => setError(reason.message))
  }, [])

  return (
    <main className="landing-page">
      <nav className="landing-nav" aria-label="Main navigation">
        <a className="landing-brand" href="/" onClick={(event) => { event.preventDefault(); onNavigate('/') }}><span className="brand-mark">S</span>SaaS Starter</a>
        <div className="landing-nav-links">
          <a href="#features">Features</a>
          <a href="#pricing">Pricing</a>
        </div>
        <div className="landing-actions">
          <Button variant="ghost" onClick={() => onNavigate('/login')}>Sign in</Button>
          <Button onClick={() => onNavigate('/register')}>Get started</Button>
        </div>
      </nav>

      <section className="landing-hero">
        <div className="landing-copy">
          <p className="landing-eyebrow">A foundation for your next product</p>
          <h1>Your SaaS, built on a foundation that scales.</h1>
          <p>Start with secure accounts, team workspaces, role-based access, and subscription billing. Spend your time building the product that makes your idea unique.</p>
          <div className="landing-hero-actions">
            <Button icon={<ArrowRight size={18} />} onClick={() => onNavigate('/register')}>Create your account</Button>
            <a href="#pricing">Explore plans</a>
          </div>
        </div>
        <div className="landing-preview starter-preview" aria-label="Workspace product preview">
          <div className="preview-topbar"><span /><span /><span /><small>Workspace overview</small></div>
          <div className="starter-preview-content">
            <div className="starter-preview-heading"><span className="brand-mark">S</span><div><strong>Acme workspace</strong><small>Team plan</small></div></div>
            <div className="starter-preview-stat"><span>Team members</span><strong>12</strong></div>
            <div className="starter-preview-stat"><span>Subscription</span><strong className="preview-ready">Active</strong></div>
            <div className="starter-preview-members"><span /><span /><span /><small>+9 teammates</small></div>
          </div>
        </div>
      </section>

      <section className="landing-proof" aria-label="Platform features">
        <div><strong>Authentication</strong><span>Secure account access</span></div>
        <div><strong>Multi-tenant</strong><span>Workspace isolation</span></div>
        <div><strong>Subscriptions</strong><span>Stripe-ready billing</span></div>
      </section>

      <section className="landing-features" id="features">
        <div className="landing-section-heading"><p>Product foundations</p><h2>The essentials, already connected.</h2></div>
        <div className="feature-grid">
          <article><span><ShieldCheck size={21} /></span><h3>Authentication and access</h3><p>Registration, sign-in, and protected APIs built around authenticated users and workspace roles.</p></article>
          <article><span><Users size={21} /></span><h3>Teams and workspaces</h3><p>Keep organizations separate and give each workspace a clear membership boundary.</p></article>
          <article><span><Layers3 size={21} /></span><h3>Plans and subscriptions</h3><p>Present configurable pricing and connect paid plans to Stripe Checkout and customer billing.</p></article>
        </div>
      </section>

      <section className="landing-workflow">
        <div><p>Start with the basics</p><h2>Go from account to paid workspace.</h2></div>
        <ol>
          <li><span>01</span><strong>Create an account</strong><p>Register and set up your first workspace.</p></li>
          <li><span>02</span><strong>Bring in your team</strong><p>Build your product on top of workspace membership.</p></li>
          <li><span>03</span><strong>Choose a plan</strong><p>Let customers subscribe and manage billing securely.</p></li>
        </ol>
      </section>

      <section className="pricing-section" id="pricing">
        <div className="landing-section-heading"><p>Simple pricing</p><h2>Plans that grow with your product.</h2></div>
        {error ? <p className="form-error" role="alert">Pricing is temporarily unavailable.</p> : null}
        <div className="plan-grid">{plans.map((plan) => (
          <article className="plan-card" key={plan.id}>
            <h3>{plan.name}</h3><p>{plan.description}</p>
            <strong>{formatPlanPrice(plan.monthlyPrice)}<small> / month</small></strong>
            <ul><li><Check size={16} />Workspace access</li><li><Check size={16} />Account management</li><li><Check size={16} />Secure billing</li></ul>
            <Button variant="secondary" onClick={() => onNavigate('/register')}>Get started</Button>
          </article>
        ))}</div>
      </section>

      <footer className="landing-footer"><a className="landing-brand" href="/"><span className="brand-mark">S</span>SaaS Starter</a><p>A reusable foundation for your next SaaS.</p></footer>
    </main>
  )
}
