import { ArrowRight, BarChart3, FileCheck2, ReceiptText, ShieldCheck } from 'lucide-react'
import { Button } from '../../components/ui/Button'

interface LandingPageProps {
  onNavigate: (path: string) => void
}

export function LandingPage({ onNavigate }: LandingPageProps) {
  return (
    <main className="landing-page">
      <nav className="landing-nav" aria-label="Main navigation">
        <a className="landing-brand" href="/" onClick={(event) => { event.preventDefault(); onNavigate('/') }}><span className="brand-mark">M</span>Manage Invoice</a>
        <div className="landing-nav-links">
          <a href="#product">Product</a>
          <a href="#workflow">How it works</a>
          <a href="#pricing">Pricing</a>
        </div>
        <div className="landing-actions">
          <Button variant="ghost" onClick={() => onNavigate('/login')}>Sign in</Button>
          <Button onClick={() => onNavigate('/register')}>Start free</Button>
        </div>
      </nav>

      <section className="landing-hero">
        <div className="landing-copy">
          <p className="landing-eyebrow">AI document operations</p>
          <h1>From invoice to answer, without the busywork.</h1>
          <p>Capture invoices and receipts, review extracted financial data, and understand spending from one secure workspace.</p>
          <div className="landing-hero-actions">
            <Button icon={<ArrowRight size={18} />} onClick={() => onNavigate('/register')}>Create a workspace</Button>
            <a href="#product">Explore the product</a>
          </div>
        </div>
        <div className="landing-preview" aria-label="Product preview">
          <div className="preview-topbar"><span /><span /><span /><small>Document review</small></div>
          <div className="preview-body">
            <div className="preview-document"><div className="preview-line wide" /><div className="preview-line" /><div className="preview-box" /><div className="preview-line wide" /><div className="preview-line short" /></div>
            <div className="preview-fields"><small>Extracted data</small><div><span>Vendor</span><strong>Northstar Office</strong></div><div><span>Total</span><strong>$1,284.00</strong></div><div><span>Status</span><strong className="preview-ready">Ready for review</strong></div></div>
          </div>
        </div>
      </section>

      <section className="landing-proof" aria-label="Product benefits">
        <div><strong>Document intelligence</strong><span>Structured extraction with human review</span></div>
        <div><strong>Expense clarity</strong><span>Expenses and invoices stay distinct</span></div>
        <div><strong>Workspace security</strong><span>Tenant-aware records and access</span></div>
      </section>

      <section className="landing-features" id="product">
        <div className="landing-section-heading"><p>Built for financial control</p><h2>Less data entry. More confidence.</h2></div>
        <div className="feature-grid">
          <article><span><FileCheck2 size={21} /></span><h3>Review every extraction</h3><p>Compare source documents with normalized fields before approval. Corrections remain auditable.</p></article>
          <article><span><ReceiptText size={21} /></span><h3>Keep payments accurate</h3><p>Track expenses, invoices, payment evidence, and balances without counting the same record twice.</p></article>
          <article><span><BarChart3 size={21} /></span><h3>Understand spending</h3><p>Build clear reporting by category, vendor, date, and workspace once financial records are approved.</p></article>
        </div>
      </section>

      <section className="landing-workflow" id="workflow">
        <div><p>How it works</p><h2>One controlled workflow from upload to approval.</h2></div>
        <ol><li><span>01</span><strong>Upload securely</strong><p>Store invoices, receipts, bills, and supporting images.</p></li><li><span>02</span><strong>Review extracted data</strong><p>Correct uncertain fields without losing the original result.</p></li><li><span>03</span><strong>Manage spending</strong><p>Create financial records only after the required review.</p></li></ol>
      </section>

      <section className="landing-pricing" id="pricing">
        <div><ShieldCheck size={22} /><div><strong>Start with a workspace that fits</strong><p>Individual, freelancer, business, and accountant plans will be available as billing phases are implemented.</p></div></div>
        <Button variant="secondary" onClick={() => onNavigate('/register')}>Create free account</Button>
      </section>

      <footer className="landing-footer"><a className="landing-brand" href="/"><span className="brand-mark">M</span>Manage Invoice</a><p>AI document intelligence and expense management.</p></footer>
    </main>
  )
}
