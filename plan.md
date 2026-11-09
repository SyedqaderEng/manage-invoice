# MASTER.md
# AI Document Intelligence & Expense Management SaaS

**Document Type:** Master Product Requirements, Architecture, Design System, and Implementation Specification  
**Product Category:** Multi-Tenant SaaS / AI Document Processing / Expense Management / Financial Analytics  
**Initial Market:** United States  
**Architecture:** Modular Monolith  
**Frontend:** React + TypeScript  
**Backend:** Java + Spring Boot  
**Database:** PostgreSQL  
**Primary Users:** Individuals, freelancers, small businesses, contractors, accountants, and finance teams  
**Monetization:** Monthly subscriptions, usage-based billing, and higher-tier plans

---

# 1. MASTER INSTRUCTIONS FOR KIRO

This document is the primary source of truth for the entire project.

Kiro must follow these rules throughout development.

## 1.1 Read Before Implementing

Before starting any implementation:

1. Read this complete MASTER.md file.
2. Understand the business requirements.
3. Understand the system architecture.
4. Understand the database model.
5. Understand the frontend design system.
6. Understand the backend module boundaries.
7. Review the implementation phases.
8. Identify dependencies and prerequisites.

Never implement a feature without understanding how it fits into the complete application.

## 1.2 Implementation Rules

- Follow the documented architecture.
- Build the application incrementally.
- Complete one phase before starting the next.
- Do not skip database migrations.
- Do not use mock data in production functionality.
- Do not create duplicate implementations of existing features.
- Do not introduce unnecessary libraries.
- Do not create microservices for the initial product.
- Do not put business logic inside React components.
- Do not put business logic inside controllers.
- Do not expose sensitive information to the frontend.
- Do not hardcode secrets, API keys, or credentials.
- Do not trust AI-generated financial data without validation.
- Do not automatically approve extracted financial records.
- Do not treat an invoice as proof of payment.
- Do not count an invoice and its associated receipt as two separate expenses.
- Do not allow users from one tenant to access another tenant's information.

## 1.3 Required Development Workflow

For every phase:

1. Review the relevant requirements.
2. Identify affected frontend modules.
3. Identify affected backend modules.
4. Identify required database changes.
5. Identify API changes.
6. Identify security implications.
7. Create or update implementation documentation.
8. Implement the functionality.
9. Write automated tests.
10. Run the application.
11. Verify the complete workflow.
12. Fix all discovered issues.
13. Update the project progress document.

Do not mark a phase complete merely because the code compiles.

A phase is complete only when its acceptance criteria are satisfied.

## 1.4 Documentation Files

Maintain these project documents:

| File | Purpose |
|---|---|
| MASTER.md | Complete product and architecture specification |
| PROJECT_STATUS.md | Current implementation progress |
| ARCHITECTURE.md | Technical architecture |
| DATABASE.md | Database structure and relationships |
| API.md | REST API documentation |
| DESIGN_SYSTEM.md | Frontend design standards |
| SECURITY.md | Security and privacy requirements |
| AI_PROCESSING.md | OCR and AI extraction workflows |
| DEPLOYMENT.md | Deployment and environment configuration |
| TESTING.md | Test strategy and test coverage |
| CHANGELOG.md | Significant implementation changes |

Update relevant documents whenever the implementation changes.

---

# 2. PRODUCT OVERVIEW

## 2.1 Product Vision

Build a modern AI-powered document intelligence and expense management platform.

Users should be able to upload invoices, receipts, bills, purchase orders, statements, forms, and other financial documents.

The platform will:

1. Read uploaded documents and images.
2. Identify document types.
3. Extract structured information using OCR and AI.
4. Convert unstructured documents into standardized financial records.
5. Allow users to review and correct extracted information.
6. Store approved records securely.
7. Organize expenses by vendor, category, date, and other attributes.
8. Provide spending dashboards and financial reports.
9. Export data into Excel, CSV, and accounting-friendly formats.
10. Help users understand where their money is being spent.

The product should make document processing and financial tracking significantly easier for individuals and small businesses.

## 2.2 Core Product Concept

The product combines three major capabilities.

### Capability A: AI Document Intelligence

Convert documents and images into structured data.

Examples:

- Vendor invoices.
- Store receipts.
- Utility bills.
- Restaurant receipts.
- Purchase orders.
- Business expense documents.
- Bank statements.
- Credit card statements.
- Tax-related documents.
- General forms and financial documents.

### Capability B: Expense Management

Maintain a centralized record of financial activity.

Users can:

- View expenses.
- Categorize transactions.
- Track vendors.
- Monitor spending.
- Track invoice amounts.
- Track payment status.
- Identify recurring expenses.
- Search financial records.
- Correct inaccurate information.

### Capability C: Financial Analytics

Provide useful financial insights.

Examples:

- Total monthly spending.
- Spending by category.
- Spending by vendor.
- Monthly spending trends.
- Highest expense categories.
- Recurring payments.
- Unpaid invoices.
- Expense comparisons.
- Downloadable financial reports.

## 2.3 Core Product Principle

**Upload once, extract automatically, review when needed, and understand your spending.**

The system should reduce manual data entry while preserving user control and financial accuracy.

---

# 3. TARGET CUSTOMERS

The platform should support multiple customer types through a flexible workspace architecture.

## 3.1 Individual Users

Examples:

- People tracking household expenses.
- Individuals managing receipts.
- People monitoring monthly spending.
- Individuals organizing bills and invoices.

Important features:

- Personal expense dashboard.
- Receipt scanning.
- Household expense categories.
- Monthly spending reports.
- Vendor history.
- Exportable expense records.

## 3.2 Freelancers and Independent Contractors

Examples:

- Consultants.
- Developers.
- Designers.
- Independent professionals.
- Self-employed contractors.

Important features:

- Business expense tracking.
- Client-related expense organization.
- Vendor management.
- Receipt storage.
- Expense reports.
- Tax preparation exports.

## 3.3 Small Businesses

Examples:

- Construction businesses.
- Consulting companies.
- Retail businesses.
- Service providers.
- Small agencies.

Important features:

- Business expense management.
- Invoice processing.
- Purchase tracking.
- Vendor management.
- Multiple users.
- Approval workflows.
- Monthly reports.

## 3.4 Accountants and Bookkeepers

Important features:

- Multiple client workspaces.
- Document review.
- Bulk exports.
- Data correction.
- Accounting-ready records.
- Review status tracking.

Multi-client accountant functionality should be designed for a later implementation phase.

## 3.5 Future Enterprise Customers

Potential capabilities:

- Advanced user roles.
- Approval chains.
- Department-level expenses.
- Advanced audit logs.
- API integrations.
- Accounting integrations.
- Custom retention policies.

---

# 4. PRODUCT SCOPE

## 4.1 Initial MVP

The initial release must include:

1. Registration and authentication.
2. Multi-tenant workspace support.
3. User dashboard.
4. Document upload.
5. Image and PDF support.
6. Document classification.
7. OCR and AI extraction.
8. Structured document data.
9. Human review and correction.
10. Expense record creation.
11. Vendor management.
12. Expense categories.
13. Monthly spending dashboard.
14. Expense search and filtering.
15. Excel export.
16. CSV export.
17. Document history.
18. Subscription management.
19. Usage tracking.
20. Basic notifications.

## 4.2 Later Features

Implement after the MVP is stable:

- Bank integrations.
- Credit card integrations.
- Automatic transaction matching.
- Accounting integrations.
- Advanced invoice management.
- Multi-level approvals.
- Accountant client management.
- Recurring expense detection.
- Tax preparation reports.
- Advanced financial forecasting.
- Mobile applications.
- API access.
- Webhooks.

## 4.3 Out of Scope for Initial MVP

Do not build these into the initial release:

- Full double-entry accounting.
- Payroll processing.
- Tax filing.
- Payment processing.
- Banking services.
- Automated bill payments.
- Complete ERP functionality.
- Full inventory management.

The product is a document intelligence and expense management platform, not a replacement for a full accounting system.

---

# 5. PRODUCT TERMINOLOGY AND FINANCIAL DATA RULES

The application must distinguish between different financial concepts.

## 5.1 Document

A document is the uploaded original file.

Examples:

- Invoice PDF.
- Receipt image.
- Utility bill.
- Statement.

A document can contain financial information but is not automatically a confirmed expense.

## 5.2 Expense

An expense is a financial record representing a purchase or cost.

Example:

A restaurant receipt for $85.40 can create an expense record for $85.40.

## 5.3 Invoice

An invoice represents a request for payment.

An invoice may be:

- Draft.
- Received.
- Pending.
- Approved.
- Partially paid.
- Paid.
- Overdue.
- Cancelled.

An invoice must not automatically be considered a paid expense.

## 5.4 Payment

A payment represents money actually paid.

An invoice can have multiple payments.

Example:

Invoice amount: $1,000.

Payment 1: $400.

Payment 2: $600.

Remaining balance: $0.

## 5.5 Receipt

A receipt is evidence of a completed purchase or payment.

A receipt may create an expense record.

## 5.6 Duplicate Financial Records

The system must avoid double counting.

Example:

1. User uploads an invoice for $500.
2. User later uploads a receipt showing payment of that invoice.
3. The system identifies a potential relationship.
4. The user reviews the relationship.
5. The system links the receipt to the invoice.
6. The system does not count the same $500 twice in spending reports.

Financial reporting must distinguish:

- Invoice totals.
- Actual paid expenses.
- Outstanding liabilities.
- Payments made.
- Reimbursements.
- Refunds.

---

# 6. USER EXPERIENCE AND DESIGN PRINCIPLES

The application must have a clean, professional, modern SaaS interface.

The design should follow proven patterns from successful financial and productivity applications.

Design inspiration:

- Stripe Dashboard: clear financial information and tables.
- Ramp: expense management workflows.
- Brex: business finance dashboards.
- QuickBooks: financial organization and reporting.
- Linear: clean navigation and interaction patterns.
- Notion: simple, structured interfaces.
- Modern banking applications: readable transaction information.

These are design references, not interfaces to copy.

## 6.1 Design Philosophy

The interface must be:

- Clean.
- Minimal.
- Professional.
- Consistent.
- Responsive.
- Accessible.
- Fast.
- Easy to understand.
- Suitable for individual and business users.

Avoid:

- Excessive colors.
- Unnecessary gradients.
- Overloaded dashboards.
- Excessive cards.
- Too many navigation items.
- Decorative animations.
- Unnecessary modal windows.
- Complicated forms.

## 6.2 Design Tokens

Create centralized design tokens.

Suggested foundation:

| Token | Suggested Value |
|---|---|
| Primary color | Deep navy / blue |
| Accent color | Teal or restrained blue |
| Success | Green |
| Warning | Amber |
| Error | Red |
| Main background | Very light neutral |
| Card background | White |
| Main text | Dark slate |
| Secondary text | Muted slate |
| Border | Light gray |
| Border radius | 8–12px |
| Base spacing | 4px increments |
| Main font | Inter or equivalent |

These are starting values. Centralize them so the entire interface can be adjusted consistently.

## 6.3 Typography

Use a consistent typography hierarchy:

- Page title.
- Section title.
- Card title.
- Table heading.
- Body text.
- Secondary text.
- Metadata.
- Numeric financial values.

Financial numbers should be easy to scan.

Use consistent currency formatting and date formatting throughout the application.

## 6.4 Layout

Desktop:

- Fixed or collapsible left sidebar.
- Top navigation bar.
- Main content area.
- Optional right-side detail panel.

Mobile:

- Responsive navigation.
- Stacked cards.
- Scrollable tables or mobile transaction lists.
- Mobile-friendly upload interface.
- Touch-friendly controls.

## 6.5 Global Application Layout

The application should use:

1. Sidebar navigation.
2. Top navigation.
3. Workspace selector.
4. Main content area.
5. Global search.
6. Notifications.
7. User profile menu.

## 6.6 Sidebar Navigation

Primary navigation:

- Overview
- Documents
- Expenses
- Invoices
- Vendors
- Categories
- Reports
- Exports

Secondary navigation:

- Team
- Subscription & Usage
- Settings
- Help & Support

Hide or disable features that are not yet available. Do not show unfinished features as if they are operational.

---

# 7. COMPLETE FRONTEND REQUIREMENTS

## 7.1 Frontend Technology

Use:

- React.
- TypeScript.
- Vite.
- Tailwind CSS.
- React Router.
- TanStack Query.
- React Hook Form.
- Zod.
- A consistent component library.
- Recharts for charts.
- Lucide icons.
- A supported table/grid implementation.

Do not introduce a second styling system without a documented reason.

## 7.2 Frontend Architecture

Organize the frontend by feature.

Suggested structure:

    frontend/
      src/
        app/
          App.tsx
          router.tsx
          providers/
          layouts/

        components/
          ui/
          common/
          navigation/
          feedback/
          charts/
          tables/

        features/
          auth/
          onboarding/
          dashboard/
          documents/
          extraction/
          review/
          expenses/
          invoices/
          vendors/
          categories/
          reports/
          exports/
          team/
          billing/
          notifications/
          settings/

        hooks/
        services/
        api/
        schemas/
        types/
        utils/
        constants/
        styles/

      public/

## 7.3 Reusable UI Components

Build reusable components before creating large pages.

Required components:

- Button.
- IconButton.
- Input.
- Textarea.
- Select.
- MultiSelect.
- Checkbox.
- RadioGroup.
- DatePicker.
- DateRangePicker.
- SearchInput.
- DropdownMenu.
- Tabs.
- Modal.
- Drawer.
- Tooltip.
- Popover.
- ConfirmationDialog.
- Alert.
- Toast.
- Badge.
- Avatar.
- Card.
- StatCard.
- DataTable.
- Pagination.
- EmptyState.
- LoadingState.
- Skeleton.
- FileUploader.
- FilePreview.
- StatusIndicator.
- CurrencyInput.
- AmountDisplay.
- ChartContainer.
- FilterBar.
- Breadcrumbs.

Every component must have consistent styling, accessibility behavior, loading behavior, disabled behavior, and error handling.

## 7.4 Dashboard

Route:

    /dashboard

Dashboard sections:

### Welcome Header

Display:

- Greeting.
- Current workspace.
- Current date.
- Upload document action.

### Financial Summary

Display:

- Total expenses this month.
- Total expenses last month.
- Month-over-month change.
- Outstanding invoices.
- Documents awaiting review.
- Documents processed.

### Spending Chart

Display:

- Monthly spending.
- Previous month comparison.
- Date range selection.
- Currency formatting.

### Category Breakdown

Display:

- Category name.
- Total amount.
- Percentage of total.
- Transaction count.

### Top Vendors

Display:

- Vendor name.
- Total spending.
- Number of transactions.

### Recent Expenses

Display:

- Date.
- Vendor.
- Description.
- Category.
- Amount.
- Status.

### Processing Activity

Display:

- Recently uploaded documents.
- Processing status.
- Review required.
- Completed documents.

Dashboard requirements:

- Date filters.
- Workspace filters where applicable.
- Loading states.
- Empty states.
- Error states.
- Responsive charts.
- Real backend data.

Do not use hardcoded financial totals.

## 7.5 Documents Module

Route:

    /documents

Features:

- Upload documents.
- Drag-and-drop.
- Multiple file uploads.
- Image upload.
- PDF upload.
- Document list.
- Document preview.
- Document details.
- Processing status.
- Search.
- Filters.
- Sort.
- Pagination.
- Reprocessing where permitted.
- Archive.
- Delete according to retention rules.

Supported initial formats:

- PDF.
- JPG.
- JPEG.
- PNG.
- WEBP.

Document status:

- Uploaded.
- Queued.
- Processing.
- Extracted.
- Review Required.
- Approved.
- Failed.
- Archived.

The UI must clearly show processing progress.

## 7.6 Document Upload Experience

The upload screen should include:

- Drag-and-drop area.
- Browse files button.
- Supported file formats.
- Maximum file size.
- Multiple file support.
- Upload progress.
- File validation.
- Duplicate warning.
- Processing explanation.

After upload:

1. Show the uploaded file.
2. Display its status.
3. Start processing.
4. Show progress.
5. Notify the user when review is required.

Do not block the entire application while documents process.

## 7.7 Document Detail Screen

Display:

- Original document.
- Document name.
- File type.
- Upload date.
- Processing status.
- Extracted fields.
- Extraction confidence.
- Related expense or invoice.
- Review history.
- Processing errors.

Use a split-screen layout where appropriate:

Left side: document preview.

Right side: extracted information.

## 7.8 Human Review Workspace

Route:

    /documents/review

This is one of the most important parts of the product.

The review interface must make it easy to compare the original document with extracted information.

Display:

- Document preview.
- Vendor.
- Invoice number.
- Document date.
- Due date.
- Subtotal.
- Tax.
- Total.
- Currency.
- Payment status where supported.
- Line items.
- Category.
- Description.

Required interactions:

- Edit fields.
- Correct incorrect values.
- Add missing values.
- Remove incorrect values.
- Add line items.
- Correct quantities.
- Correct prices.
- Recalculate totals.
- Approve.
- Reject.
- Send for reprocessing.
- Save draft.
- Navigate to the next document.

Low-confidence fields should be visually highlighted.

The system must preserve the original extracted values and record user corrections.

## 7.9 Expenses Module

Route:

    /expenses

Features:

- Expense listing.
- Expense detail.
- Create manual expense.
- Edit expense.
- Delete or void according to permissions.
- Category assignment.
- Vendor assignment.
- Date filtering.
- Amount filtering.
- Status filtering.
- Search.
- Sorting.
- Pagination.
- Bulk categorization.
- Export.

Expense table columns:

- Date.
- Vendor.
- Description.
- Category.
- Amount.
- Currency.
- Payment status.
- Source document.
- Actions.

Expense detail should show its source document and any associated invoice or payment.

## 7.10 Invoices Module

Route:

    /invoices

Features:

- Invoice listing.
- Invoice detail.
- Invoice number.
- Vendor.
- Invoice date.
- Due date.
- Subtotal.
- Tax.
- Total.
- Amount paid.
- Remaining balance.
- Payment status.
- Associated documents.
- Payment history.

Invoice status:

- Draft.
- Received.
- Pending Review.
- Approved.
- Partially Paid.
- Paid.
- Overdue.
- Cancelled.

Do not assume that an invoice is paid just because it has been uploaded.

## 7.11 Vendors Module

Route:

    /vendors

Features:

- Vendor list.
- Vendor detail.
- Vendor name.
- Contact information where available.
- Tax identifier where permitted.
- Vendor address.
- Total spending.
- Number of invoices.
- Number of expenses.
- Recent activity.
- Related documents.
- Edit vendor.
- Merge duplicate vendors.

Vendor matching must not merge records automatically without appropriate confidence and review.

## 7.12 Categories Module

Route:

    /categories

Default expense categories:

- Office Supplies.
- Software & Subscriptions.
- Travel.
- Meals.
- Transportation.
- Utilities.
- Rent.
- Insurance.
- Marketing.
- Professional Services.
- Equipment.
- Maintenance.
- Shipping.
- Taxes & Fees.
- Healthcare.
- Groceries.
- Entertainment.
- Other.

Allow:

- Custom categories.
- Category editing.
- Category colors.
- Category descriptions.
- Category archival.
- Category-level reporting.

Personal and business workspaces may have different default category sets.

## 7.13 Reports Module

Route:

    /reports

Reports:

- Monthly spending.
- Annual spending.
- Category breakdown.
- Vendor breakdown.
- Expense trends.
- Invoice summary.
- Outstanding invoices.
- Payment summary.
- Tax-related expense summary.

Filters:

- Date range.
- Workspace.
- Category.
- Vendor.
- Document type.
- Payment status.

Reports must use consistent financial definitions.

## 7.14 Export Module

Route:

    /exports

Initial export formats:

- CSV.
- XLSX.

Later:

- PDF reports.
- Accounting-specific formats.
- Direct accounting integrations.

Allow exports of:

- Expenses.
- Invoices.
- Vendors.
- Document extraction data.
- Monthly reports.
- Category summaries.

Export requirements:

- Date range.
- Category selection.
- Vendor selection.
- Column selection where practical.
- Currency handling.
- Export history.
- Download status.

## 7.15 Billing and Usage

Route:

    /settings/billing

Display:

- Current plan.
- Subscription status.
- Monthly document quota.
- Documents processed.
- Remaining quota.
- Usage history.
- Billing history.
- Payment method.
- Upgrade option.
- Downgrade option.
- Cancel subscription.

Never rely on frontend checks alone to enforce subscription limits.

## 7.16 Settings

Route:

    /settings

Sections:

- Profile.
- Workspace.
- Members.
- Roles.
- Notifications.
- Security.
- Data export.
- Data retention.
- Billing.

## 7.17 Notifications

Notification types:

- Document uploaded.
- Processing completed.
- Review required.
- Processing failed.
- Subscription limit approaching.
- Subscription payment failed.
- Invoice approaching due date, if enabled.

Support:

- In-app notifications.
- Read/unread state.
- Notification preferences.

Email notifications can be added through a transactional email provider.

---

# 8. BACKEND ARCHITECTURE

## 8.1 Backend Technology

Use:

- Java 21 or later LTS.
- Spring Boot 3.x or a currently supported stable version.
- Spring Security.
- Spring Data JPA.
- Hibernate.
- PostgreSQL.
- Flyway.
- Bean Validation.
- Spring Web.
- Spring Actuator.
- Redis.
- Object storage compatible with S3.
- Stripe integration.
- OpenAPI documentation.

Use compatible, maintained library versions.

## 8.2 Architecture Style

Use a modular monolith.

Do not start with microservices.

The backend must have clearly separated business modules.

Suggested structure:

    backend/
      src/main/java/com/company/product/

        ProductApplication.java

        common/
          config/
          exception/
          security/
          audit/
          validation/
          pagination/
          events/

        identity/
          controller/
          service/
          repository/
          entity/
          dto/

        tenancy/
          controller/
          service/
          repository/
          entity/
          dto/

        documents/
          controller/
          service/
          repository/
          entity/
          dto/

        processing/
          controller/
          service/
          repository/
          entity/
          dto/

        extraction/
          controller/
          service/
          repository/
          entity/
          dto/

        review/
          controller/
          service/
          repository/
          entity/
          dto/

        expenses/
          controller/
          service/
          repository/
          entity/
          dto/

        invoices/
          controller/
          service/
          repository/
          entity/
          dto/

        vendors/
          controller/
          service/
          repository/
          entity/
          dto/

        categories/
          controller/
          service/
          repository/
          entity/
          dto/

        reports/
          controller/
          service/
          repository/
          entity/
          dto/

        exports/
          controller/
          service/
          repository/

        billing/
          controller/
          service/
          repository/
          entity/
          dto/

        notifications/
          controller/
          service/
          repository/

        audit/
          service/
          repository/
          entity/

      src/main/resources/
        application.yml
        application-dev.yml
        application-test.yml
        db/migration/

## 8.3 Module Responsibilities

### Identity

Responsible for:

- Registration.
- Login.
- Logout.
- Password management.
- Email verification.
- Session management.
- Authentication.
- Account status.

### Tenancy

Responsible for:

- Workspaces.
- Workspace memberships.
- Roles.
- Permissions.
- Invitations.
- Tenant-level configuration.

### Documents

Responsible for:

- Upload.
- Storage.
- File metadata.
- File validation.
- Document retrieval.
- Document lifecycle.
- Document deletion and retention.

### Processing

Responsible for:

- Processing jobs.
- Job status.
- OCR provider integration.
- AI provider integration.
- Retry handling.
- Processing orchestration.

### Extraction

Responsible for:

- Structured extraction.
- Field validation.
- Confidence scores.
- Normalization.
- Extraction versioning.

### Review

Responsible for:

- Human review.
- Corrections.
- Approval.
- Rejection.
- Review history.

### Expenses

Responsible for:

- Expense records.
- Categorization.
- Financial calculations.
- Expense search.
- Expense lifecycle.

### Invoices

Responsible for:

- Invoice records.
- Invoice line items.
- Payment status.
- Outstanding balances.
- Invoice-payment relationships.

### Vendors

Responsible for:

- Vendor profiles.
- Vendor matching.
- Vendor normalization.
- Duplicate detection.

### Reports

Responsible for:

- Financial summaries.
- Aggregations.
- Monthly reports.
- Category analytics.
- Vendor analytics.

### Exports

Responsible for:

- CSV generation.
- XLSX generation.
- Export jobs.
- Download management.

### Billing

Responsible for:

- Plans.
- Subscriptions.
- Usage.
- Stripe integration.
- Quota enforcement.
- Billing webhooks.

### Notifications

Responsible for:

- In-app notifications.
- Email notifications.
- User notification preferences.

---

# 9. MULTI-TENANT SAAS ARCHITECTURE

The application must be multi-tenant from the beginning.

## 9.1 Workspace Model

A user can belong to multiple workspaces.

Examples:

- Personal workspace.
- Consulting business.
- Small business.
- Accountant client workspace.

Each workspace owns its documents, expenses, invoices, vendors, categories, reports, and settings.

## 9.2 Tenant Isolation

Every tenant-owned database record must be associated with a workspace.

Every relevant repository query must enforce workspace authorization.

Never trust a workspace ID sent by the browser without verifying membership and permissions.

## 9.3 Roles

Initial roles:

| Role | Permissions |
|---|---|
| OWNER | Full workspace control |
| ADMIN | Manage workspace, users, and records |
| MEMBER | Upload and manage permitted records |
| REVIEWER | Review and approve documents |
| VIEWER | Read-only access |

Permissions must be enforced in the backend.

The frontend should hide unavailable actions, but backend authorization is mandatory.

## 9.4 Workspace Switching

Users should be able to switch workspaces without logging out.

The selected workspace must be included in the authenticated application context and validated on the backend.

---

# 10. DATABASE DESIGN

Use PostgreSQL.

Use Flyway for all schema migrations.

Use UUID identifiers for primary keys.

Use `created_at` and `updated_at` consistently.

Store monetary values using appropriate decimal types, never floating-point types.

Use explicit currency codes such as USD.

## 10.1 Core Tables

### users

Fields:

- id
- email
- password_hash or external_identity_reference
- first_name
- last_name
- status
- email_verified
- created_at
- updated_at

### workspaces

Fields:

- id
- name
- workspace_type
- owner_user_id
- default_currency
- timezone
- status
- created_at
- updated_at

### workspace_members

Fields:

- id
- workspace_id
- user_id
- role
- status
- joined_at

Unique constraint:

- workspace_id + user_id

### workspace_invitations

Fields:

- id
- workspace_id
- email
- role
- token_hash
- expires_at
- accepted_at
- invited_by
- created_at

### documents

Fields:

- id
- workspace_id
- uploaded_by
- original_filename
- stored_filename
- storage_key
- mime_type
- file_size
- checksum
- document_type
- processing_status
- review_status
- page_count
- uploaded_at
- processed_at
- archived_at
- deleted_at

### processing_jobs

Fields:

- id
- workspace_id
- document_id
- job_type
- status
- attempt_count
- error_code
- error_message
- started_at
- completed_at
- created_at

### document_extractions

Fields:

- id
- workspace_id
- document_id
- extraction_version
- raw_extraction_json
- normalized_data_json
- overall_confidence
- model_provider
- model_name
- created_at

### extraction_fields

Fields:

- id
- workspace_id
- extraction_id
- field_name
- extracted_value
- normalized_value
- confidence_score
- source_page
- source_region
- validation_status
- correction_status

### document_reviews

Fields:

- id
- workspace_id
- document_id
- reviewer_user_id
- review_status
- original_data_json
- corrected_data_json
- review_notes
- reviewed_at

### vendors

Fields:

- id
- workspace_id
- normalized_name
- display_name
- email
- phone
- address
- tax_identifier_encrypted
- default_category_id
- created_at
- updated_at

### categories

Fields:

- id
- workspace_id
- name
- description
- color
- category_type
- is_system
- is_active
- created_at
- updated_at

### expense_transactions

Fields:

- id
- workspace_id
- document_id
- vendor_id
- category_id
- description
- transaction_date
- amount
- tax_amount
- currency
- payment_status
- expense_status
- source_type
- created_by
- approved_by
- created_at
- updated_at

### expense_line_items

Fields:

- id
- workspace_id
- expense_id
- description
- quantity
- unit_price
- tax_amount
- line_total
- category_id

### invoices

Fields:

- id
- workspace_id
- document_id
- vendor_id
- invoice_number
- invoice_date
- due_date
- subtotal
- tax_amount
- total_amount
- amount_paid
- balance_due
- currency
- status
- created_at
- updated_at

### invoice_line_items

Fields:

- id
- workspace_id
- invoice_id
- description
- quantity
- unit_price
- tax_amount
- line_total

### payments

Fields:

- id
- workspace_id
- invoice_id
- payment_date
- amount
- currency
- payment_method
- reference_number
- status
- created_at

### document_relationships

Fields:

- id
- workspace_id
- source_document_id
- related_document_id
- relationship_type
- confidence_score
- confirmation_status
- created_at

Purpose:

Link related invoices, receipts, credit notes, and payment evidence.

### audit_logs

Fields:

- id
- workspace_id
- actor_user_id
- action
- resource_type
- resource_id
- previous_values_json
- new_values_json
- ip_address
- created_at

### subscriptions

Fields:

- id
- workspace_id
- provider
- provider_customer_id
- provider_subscription_id
- plan_id
- status
- current_period_start
- current_period_end
- cancel_at_period_end
- created_at
- updated_at

### usage_records

Fields:

- id
- workspace_id
- usage_type
- quantity
- reference_id
- billing_period
- created_at

### notifications

Fields:

- id
- workspace_id
- user_id
- notification_type
- title
- message
- read_at
- created_at

## 10.2 Database Rules

- Use foreign keys.
- Use appropriate indexes.
- Enforce tenant relationships.
- Use database transactions for financial operations.
- Use optimistic locking where appropriate.
- Prevent duplicate document processing.
- Use soft deletion where records need recoverability.
- Keep audit history for sensitive changes.
- Use Flyway migrations.
- Never modify production schemas manually.

## 10.3 Important Financial Constraints

- Invoice totals must be calculated and validated.
- Payment totals must not exceed invoice totals without an explicit credit/refund workflow.
- Refunds must be represented separately or with explicit negative financial adjustments.
- Expense and invoice relationships must be explicit.
- All reports must use a consistent definition of spending.
- Currency must be stored for every monetary record.
- Currency conversion must not be assumed without a documented exchange rate and date.

---

# 11. DOCUMENT UPLOAD AND STORAGE

## 11.1 Supported Formats

Initial formats:

- PDF
- JPG
- JPEG
- PNG
- WEBP

Later formats:

- TIFF
- DOCX
- XLSX
- CSV
- Email attachments

## 11.2 Upload Validation

Validate:

- File extension.
- Actual file content and MIME type.
- File size.
- Page count.
- Corrupt files.
- Empty files.
- Duplicate checksums.
- Workspace storage quota.

Do not trust the filename extension alone.

## 11.3 Storage

Use private S3-compatible object storage.

Possible providers:

- AWS S3.
- Cloudflare R2.
- Other compatible private object storage.

Never make financial documents publicly accessible.

Use short-lived signed download URLs where appropriate.

## 11.4 Upload Flow

1. User selects documents.
2. Frontend validates basic file properties.
3. Backend verifies authentication and workspace permissions.
4. Backend validates file contents.
5. Backend creates document metadata.
6. File is stored privately.
7. Processing job is created.
8. Background worker processes the document.
9. Frontend displays job status.
10. User receives a review notification when necessary.

Use asynchronous processing.

Do not perform slow OCR or AI extraction inside the upload HTTP request.

---

# 12. AI DOCUMENT INTELLIGENCE

This is the primary product capability.

## 12.1 Processing Pipeline

The processing pipeline must support:

1. File validation.
2. Malware and safety scanning where available.
3. Document preprocessing.
4. PDF page extraction.
5. Image preprocessing.
6. OCR.
7. Document classification.
8. AI extraction.
9. Data normalization.
10. Business validation.
11. Confidence evaluation.
12. Duplicate detection.
13. Review routing.
14. Approval.
15. Financial record creation.

## 12.2 Document Classification

Supported initial types:

- Invoice.
- Receipt.
- Utility Bill.
- Purchase Order.
- Bank Statement.
- Credit Card Statement.
- Expense Form.
- Tax Document.
- Other Financial Document.
- Unknown.

The classifier must be allowed to return UNKNOWN.

Never force a document into an incorrect category.

## 12.3 Extraction Fields

Common fields:

- Document type.
- Vendor name.
- Vendor address.
- Vendor phone.
- Vendor email.
- Document number.
- Invoice number.
- Receipt number.
- Document date.
- Due date.
- Currency.
- Subtotal.
- Tax.
- Discount.
- Shipping.
- Total.
- Payment status when explicitly supported.
- Payment method when available.
- Purchase description.
- Line items.
- Quantities.
- Unit prices.
- Tax rates.

## 12.4 Structured Extraction Schema

All extracted information must be returned in a validated structured format.

Example:

    {
      "documentType": "INVOICE",
      "vendor": {
        "name": "Example Supplier",
        "address": null
      },
      "invoice": {
        "number": "INV-1001",
        "date": "2026-10-15",
        "dueDate": "2026-11-15"
      },
      "currency": "USD",
      "financials": {
        "subtotal": 100.00,
        "tax": 6.00,
        "discount": 0.00,
        "total": 106.00
      },
      "lineItems": [
        {
          "description": "Office supplies",
          "quantity": 2,
          "unitPrice": 50.00,
          "lineTotal": 100.00
        }
      ],
      "confidence": {
        "overall": 0.95
      }
    }

This is an example schema, not hardcoded extraction output.

Use null when information is absent or uncertain.

Never invent missing values.

## 12.5 Confidence Scores

Confidence must be recorded at the field level.

Example:

- Vendor: 0.99
- Invoice number: 0.94
- Date: 0.98
- Subtotal: 0.95
- Tax: 0.78
- Total: 0.99

Suggested review behavior:

| Confidence | Behavior |
|---|---|
| 0.95–1.00 | High confidence; still subject to required review rules |
| 0.80–0.94 | Normal review |
| 0.50–0.79 | Highlight for attention |
| Below 0.50 | Require manual correction |

Confidence is not a guarantee of correctness.

All financial records must follow the configured approval policy.

## 12.6 AI Provider Abstraction

Do not tightly couple the application to one AI provider.

Create interfaces such as:

- DocumentOcrProvider.
- DocumentClassificationProvider.
- DocumentExtractionProvider.

Implement provider adapters.

The system should support changing providers without rewriting business logic.

Provider selection must consider:

- Accuracy.
- Cost.
- Latency.
- Supported file types.
- Data retention policies.
- Privacy.
- Availability.

## 12.7 Extraction Validation

Validate:

- Date formats.
- Currency codes.
- Numeric values.
- Subtotal calculations.
- Tax calculations.
- Total calculations.
- Line item totals.
- Duplicate invoice numbers where relevant.
- Vendor matching.

Example:

Subtotal: $100  
Tax: $6  
Discount: $0  
Total: $106

If the extracted total does not reconcile, flag the record for review.

Do not silently change the original extraction.

## 12.8 AI Security

Treat document content as untrusted input.

A document may contain malicious text or instructions.

AI processing must:

- Treat document content as data.
- Ignore instructions contained inside documents.
- Never reveal system prompts or credentials.
- Never initiate external actions based on document text.
- Never execute embedded code.
- Never use extracted URLs or instructions without validation.
- Avoid sending unnecessary personal information to AI providers.

---

# 13. HUMAN REVIEW AND CORRECTION

Human review is a core feature, not an optional add-on.

## 13.1 Review Lifecycle

    UPLOADED
       |
    PROCESSING
       |
    EXTRACTED
       |
    REVIEW_REQUIRED
       |
    APPROVED
       |
    FINANCIAL_RECORD_CREATED

Alternative outcomes:

- REJECTED.
- FAILED.
- NEEDS_REPROCESSING.

## 13.2 Review Capabilities

Users must be able to:

- Review the original document.
- Compare extracted information.
- Correct individual fields.
- Correct line items.
- Change document classification.
- Change vendor.
- Change category.
- Add missing information.
- Save draft corrections.
- Approve.
- Reject.
- Reprocess.

## 13.3 Review Audit

Store:

- Original extraction.
- Corrected values.
- Reviewer.
- Review date.
- Changed fields.
- Approval result.

Do not overwrite the original extraction without preserving its history.

---

# 14. EXPENSE MANAGEMENT

## 14.1 Expense Creation

An expense may originate from:

- Approved receipt.
- Approved invoice/payment workflow.
- Manual entry.
- Future bank transaction import.
- Future credit card integration.

The source must be recorded.

## 14.2 Expense Status

Initial statuses:

- Pending Review.
- Approved.
- Paid.
- Reimbursed.
- Refunded.
- Void.

Use clear business rules to distinguish approval status from payment status.

## 14.3 Categorization

Support:

- AI-suggested categories.
- User-selected categories.
- Custom categories.
- Vendor-based suggestions.
- Historical categorization suggestions.

AI suggestions must remain editable.

## 14.4 Expense Search

Search by:

- Vendor.
- Description.
- Amount.
- Date.
- Category.
- Document number.
- Source document.

Support filters and pagination.

---

# 15. INVOICE AND PAYMENT MANAGEMENT

The initial product should support basic invoice tracking.

Features:

- Invoice capture.
- Invoice number.
- Vendor.
- Invoice date.
- Due date.
- Amount.
- Tax.
- Balance.
- Payment status.
- Payment history.
- Related receipts.

Payment statuses:

- Unpaid.
- Partially Paid.
- Paid.
- Overdue.
- Cancelled.

The system must not infer payment solely from an invoice.

Users can manually record payment information in the initial release.

---

# 16. DUPLICATE DETECTION

Duplicate detection must occur at multiple levels.

## 16.1 File-Level Duplicate

Use file checksums to identify identical uploads within a workspace.

## 16.2 Document-Level Duplicate

Compare:

- Vendor.
- Invoice number.
- Document date.
- Total amount.
- Document type.

## 16.3 Financial Record Matching

Identify possible relationships between:

- Invoice and receipt.
- Invoice and payment evidence.
- Receipt and card transaction.
- Original invoice and corrected invoice.

Use a confidence score.

Do not automatically merge financial records without appropriate confirmation.

---

# 17. REPORTING AND FINANCIAL ANALYTICS

## 17.1 Dashboard Metrics

Calculate:

- Current month spending.
- Previous month spending.
- Month-over-month change.
- Spending by category.
- Spending by vendor.
- Number of expenses.
- Number of invoices.
- Outstanding invoice balance.
- Documents awaiting review.
- Processed documents.

## 17.2 Spending Reports

Provide:

- Daily spending.
- Weekly spending.
- Monthly spending.
- Quarterly spending.
- Annual spending.
- Category analysis.
- Vendor analysis.
- Recurring spending.

## 17.3 Financial Definitions

Every metric must clearly define what it includes.

For example:

**Paid spending:** Approved expenses representing money actually paid, including supported payment evidence.

**Outstanding invoices:** Approved invoices with a remaining unpaid balance.

**Invoice volume:** Total invoice value regardless of payment status.

Do not combine these metrics into one ambiguous total.

## 17.4 Date and Currency Handling

- Store timestamps consistently.
- Use workspace timezone for reporting periods.
- Store currency codes.
- Avoid mixing currencies without conversion.
- Show the reporting currency clearly.
- Preserve original transaction currency.

---

# 18. EXPORTS AND ACCOUNTING-READY DATA

Initial formats:

- CSV.
- XLSX.

Use:

- Apache POI for Excel.
- Standard CSV generation with correct escaping.

## 18.1 Export Types

- Expense export.
- Invoice export.
- Vendor export.
- Document extraction export.
- Monthly spending report.
- Category summary.
- Vendor summary.

## 18.2 Export Columns

Expense export:

- Transaction Date.
- Vendor.
- Description.
- Category.
- Subtotal.
- Tax.
- Total.
- Currency.
- Payment Status.
- Document Reference.

Invoice export:

- Invoice Number.
- Vendor.
- Invoice Date.
- Due Date.
- Subtotal.
- Tax.
- Total.
- Amount Paid.
- Balance Due.
- Currency.
- Status.

## 18.3 Export Security

- Verify workspace access.
- Generate exports asynchronously for large datasets.
- Use short-lived download links.
- Record export activity.
- Prevent unauthorized access.

---

# 19. SUBSCRIPTION AND MONETIZATION

Use Stripe for subscription billing.

Pricing is a starting proposal and must remain configurable.

## 19.1 Starter Plan

**$19/month**

Target: Individuals and light users.

Illustrative limits:

- 100 documents per month.
- Basic OCR and AI extraction.
- Expense management.
- Basic dashboard.
- CSV export.
- Standard support.

## 19.2 Professional Plan

**$49/month**

Target: Freelancers and growing businesses.

Illustrative limits:

- 500 documents per month.
- Advanced extraction.
- Excel export.
- Advanced reporting.
- Custom categories.
- Vendor analytics.
- Priority processing.

## 19.3 Business Plan

**$99/month**

Target: Small businesses and teams.

Illustrative limits:

- 2,000 documents per month.
- Multiple workspace members.
- Review roles.
- Advanced reports.
- Bulk uploads.
- Extended document history.
- Priority support.

## 19.4 Usage-Based Billing

Future option:

- Pay per additional document.
- Additional AI processing credits.
- Higher-volume plans.

Do not hardcode plan limits.

Use configurable subscription and entitlement records.

## 19.5 Usage Enforcement

Enforce limits on the backend.

Track:

- Documents uploaded.
- Documents processed.
- AI usage where available.
- Storage consumed.
- Export usage if relevant.

Prevent users from bypassing quotas through direct API calls.

---

# 20. SECURITY AND PRIVACY

Financial documents contain sensitive information.

Security must be designed into the application from the beginning.

## 20.1 Authentication

Use Spring Security.

Support:

- Secure registration.
- Login.
- Logout.
- Password reset.
- Email verification.
- Session or token management.
- Account lockout protections.

Use secure password hashing such as BCrypt or Argon2.

## 20.2 Authorization

Enforce:

- Workspace membership.
- Role permissions.
- Resource ownership.
- Tenant isolation.
- Subscription entitlements.

## 20.3 File Security

- Private object storage.
- File content validation.
- Malware scanning where available.
- Signed URLs.
- No public document buckets.
- Controlled file download.
- Retention and deletion rules.

## 20.4 Data Security

- TLS in transit.
- Encryption at rest.
- Secrets management.
- Secure environment configuration.
- Sensitive-field encryption where appropriate.
- Database backups.
- Access logging.

## 20.5 Audit Logging

Audit important actions:

- Login and security events.
- Document uploads.
- Document deletion.
- Extraction corrections.
- Approval.
- Expense changes.
- Invoice changes.
- Role changes.
- Workspace changes.
- Billing changes.
- Exports.

## 20.6 Privacy

Provide:

- Privacy policy.
- Terms of service.
- Data export.
- Account deletion.
- Document deletion.
- Retention configuration where supported.

Disclose AI processing and relevant third-party providers.

Do not use customer financial documents to train models unless there is an explicit, valid authorization and an appropriate privacy policy.

---

# 21. ASYNCHRONOUS PROCESSING

Document processing must be asynchronous.

## 21.1 Why

OCR and AI processing can take several seconds or longer.

The application must remain responsive.

## 21.2 Processing Architecture

    React Frontend
          |
          v
    Spring Boot API
          |
          v
    PostgreSQL
          |
          v
    Processing Job
          |
          v
    Background Worker
          |
          +---- OCR Provider
          |
          +---- AI Extraction Provider
          |
          +---- Validation
          |
          v
    Extraction Results
          |
          v
    Review Workspace

Use Redis and a suitable job mechanism for coordination, retry handling, and status tracking.

The database remains the source of truth for job state.

## 21.3 Retry Rules

- Retry transient provider failures.
- Do not retry invalid files indefinitely.
- Use maximum retry limits.
- Record failure reasons.
- Prevent duplicate processing.
- Support controlled reprocessing.

---

# 22. REST API DESIGN

Use REST APIs with consistent naming and response formats.

Base path:

    /api/v1

## 22.1 Authentication

    POST /auth/register
    POST /auth/login
    POST /auth/logout
    POST /auth/refresh
    POST /auth/forgot-password
    POST /auth/reset-password
    POST /auth/verify-email
    GET  /auth/me

## 22.2 Workspaces

    GET    /workspaces
    POST   /workspaces
    GET    /workspaces/{workspaceId}
    PUT    /workspaces/{workspaceId}
    DELETE /workspaces/{workspaceId}

## 22.3 Members

    GET    /workspaces/{workspaceId}/members
    POST   /workspaces/{workspaceId}/invitations
    PUT    /workspaces/{workspaceId}/members/{memberId}
    DELETE /workspaces/{workspaceId}/members/{memberId}

## 22.4 Documents

    POST   /documents
    POST   /documents/bulk
    GET    /documents
    GET    /documents/{documentId}
    GET    /documents/{documentId}/download
    DELETE /documents/{documentId}
    POST   /documents/{documentId}/reprocess

## 22.5 Processing

    GET /documents/{documentId}/processing-status
    GET /processing/jobs
    GET /processing/jobs/{jobId}

## 22.6 Extraction and Review

    GET  /documents/{documentId}/extraction
    PUT  /documents/{documentId}/extraction
    POST /documents/{documentId}/review/approve
    POST /documents/{documentId}/review/reject
    POST /documents/{documentId}/review/save

## 22.7 Expenses

    GET    /expenses
    POST   /expenses
    GET    /expenses/{expenseId}
    PUT    /expenses/{expenseId}
    DELETE /expenses/{expenseId}

## 22.8 Invoices

    GET    /invoices
    POST   /invoices
    GET    /invoices/{invoiceId}
    PUT    /invoices/{invoiceId}
    POST   /invoices/{invoiceId}/payments

## 22.9 Vendors

    GET    /vendors
    POST   /vendors
    GET    /vendors/{vendorId}
    PUT    /vendors/{vendorId}
    DELETE /vendors/{vendorId}

## 22.10 Categories

    GET    /categories
    POST   /categories
    PUT    /categories/{categoryId}
    DELETE /categories/{categoryId}

## 22.11 Reports

    GET /reports/dashboard
    GET /reports/spending
    GET /reports/categories
    GET /reports/vendors
    GET /reports/invoices

## 22.12 Exports

    POST /exports
    GET  /exports
    GET  /exports/{exportId}
    GET  /exports/{exportId}/download

## 22.13 Billing

    GET  /billing/subscription
    GET  /billing/plans
    GET  /billing/usage
    POST /billing/checkout
    POST /billing/portal
    POST /billing/webhooks/stripe

All endpoints must enforce authentication, authorization, validation, and workspace isolation as applicable.

Use consistent error response structures and pagination.

---

# 23. ERROR HANDLING AND APPLICATION RELIABILITY

The system must handle failures gracefully.

Examples:

- Invalid file.
- Unsupported format.
- File too large.
- OCR unavailable.
- AI provider timeout.
- AI extraction invalid.
- Database unavailable.
- Storage unavailable.
- Subscription limit exceeded.
- Unauthorized workspace access.
- Export generation failed.

Provide useful user-facing error messages without exposing internal stack traces.

Record technical details in server logs.

Use correlation IDs for troubleshooting.

Never silently lose an uploaded document or processing job.

---

# 24. TESTING STRATEGY

Testing is mandatory.

## 24.1 Backend Tests

Use JUnit and Spring Boot testing.

Test:

- Authentication.
- Authorization.
- Tenant isolation.
- Document upload.
- File validation.
- Processing job creation.
- AI response validation.
- Extraction normalization.
- Financial calculations.
- Expense creation.
- Invoice balances.
- Duplicate detection.
- Export generation.
- Billing quotas.

## 24.2 Frontend Tests

Use:

- Vitest.
- React Testing Library.
- Playwright for end-to-end testing.

Test:

- Authentication flows.
- Workspace switching.
- Upload.
- Document review.
- Expense management.
- Invoice management.
- Dashboard.
- Filters.
- Exports.
- Billing.

## 24.3 Integration Tests

Verify complete workflows:

1. Register user.
2. Create workspace.
3. Upload receipt.
4. Process document.
5. Extract fields.
6. Review extraction.
7. Approve document.
8. Create expense.
9. Verify dashboard totals.
10. Export expense data.

## 24.4 Tenant Isolation Tests

Create two independent workspaces.

Verify that users in one workspace cannot access:

- Documents in the other workspace.
- Expenses in the other workspace.
- Vendors in the other workspace.
- Reports in the other workspace.
- Export files in the other workspace.

These tests are release-critical.

---

# 25. OBSERVABILITY AND OPERATIONS

Implement:

- Structured application logs.
- Health checks.
- Database health monitoring.
- Processing queue monitoring.
- AI provider failure monitoring.
- API latency metrics.
- Error tracking.
- Usage metrics.
- Subscription metrics.

Use Spring Actuator for backend health endpoints.

Never expose sensitive information in logs.

Monitor:

- Processing success rate.
- Processing failure rate.
- Average processing time.
- Average AI cost per document.
- Human correction rate.
- Upload volume.
- Storage usage.
- Monthly active workspaces.

---

# 26. DEPLOYMENT ARCHITECTURE

Initial deployment should be simple and maintainable.

Components:

- React frontend.
- Spring Boot backend.
- PostgreSQL database.
- Redis.
- Private object storage.
- Background processing worker.
- AI/OCR provider.
- Stripe.
- Transactional email provider.

Use separate environments:

- Local development.
- Testing/staging.
- Production.

## 26.1 Environment Variables

Examples:

    SPRING_PROFILES_ACTIVE
    DATABASE_URL
    DATABASE_USERNAME
    DATABASE_PASSWORD
    REDIS_HOST
    REDIS_PORT
    STORAGE_ENDPOINT
    STORAGE_BUCKET
    STORAGE_ACCESS_KEY
    STORAGE_SECRET_KEY
    AI_PROVIDER_API_KEY
    STRIPE_SECRET_KEY
    STRIPE_WEBHOOK_SECRET
    EMAIL_PROVIDER_API_KEY

Never commit real credentials.

## 26.2 Deployment Requirements

- HTTPS.
- Database backups.
- Secure secrets.
- Database migrations.
- Application health checks.
- Error monitoring.
- Storage permissions.
- Background worker monitoring.
- Rollback strategy.

---

# 27. IMPLEMENTATION PHASES

Build the product in the following order.

Do not skip phases.

## PHASE 0 — Project Foundation and Architecture

Goal: Establish the complete project foundation.

Tasks:

- Create repository structure.
- Initialize React application.
- Initialize Spring Boot application.
- Configure PostgreSQL.
- Configure Flyway.
- Configure Redis.
- Configure environment profiles.
- Configure API error handling.
- Configure logging.
- Configure OpenAPI.
- Establish coding standards.
- Create documentation files.
- Configure testing frameworks.

Acceptance criteria:

- Frontend starts.
- Backend starts.
- Database connects.
- Migrations execute.
- Health checks work.
- Basic automated tests pass.

## PHASE 1 — Authentication and Multi-Tenancy

Goal: Build secure user and workspace foundations.

Tasks:

- Registration.
- Login.
- Logout.
- Password reset.
- Email verification.
- User profile.
- Workspace creation.
- Workspace switching.
- Workspace membership.
- Roles and permissions.
- Protected routes.
- Tenant authorization.

Acceptance criteria:

- Users can register and log in.
- Users can create workspaces.
- Users can switch workspaces.
- Workspace data is isolated.
- Unauthorized access is rejected.

## PHASE 2 — Frontend Design System and Application Shell

Goal: Build the professional SaaS interface.

Tasks:

- Design tokens.
- Typography.
- Colors.
- Buttons.
- Inputs.
- Cards.
- Tables.
- Modals.
- Drawers.
- Toasts.
- Loading states.
- Empty states.
- Sidebar.
- Top navigation.
- Workspace selector.
- Responsive layouts.

Acceptance criteria:

- Application shell works.
- Components are reusable.
- Navigation works.
- Responsive behavior is verified.
- Visual design is consistent.

## PHASE 3 — Document Upload and Storage

Goal: Allow users to upload and securely store documents.

Tasks:

- Upload UI.
- Backend upload API.
- File validation.
- File storage.
- Document metadata.
- Checksum generation.
- Duplicate upload detection.
- Document listing.
- Document detail.
- Secure download.
- Archive and delete rules.

Acceptance criteria:

- Users can upload supported documents.
- Files are stored privately.
- Metadata is saved.
- Workspace authorization works.
- Upload failures are handled.
- Documents can be retrieved securely.

## PHASE 4 — OCR, AI Classification, and Extraction

Goal: Convert uploaded documents into structured information.

Tasks:

- Processing job architecture.
- Background processing.
- OCR provider integration.
- Image processing.
- PDF processing.
- Document classification.
- AI extraction.
- Structured response schema.
- Data normalization.
- Confidence scoring.
- Extraction validation.
- Retry handling.
- Processing status UI.

Acceptance criteria:

- Receipt images can be processed.
- Invoice PDFs can be processed.
- Fields are extracted into structured data.
- Confidence is recorded.
- Invalid extraction is handled.
- Processing failures can be retried.
- Original files remain unchanged.

## PHASE 5 — Human Review and Approval

Goal: Allow users to verify and correct AI output.

Tasks:

- Review queue.
- Side-by-side preview.
- Field editing.
- Line-item editing.
- Validation.
- Confidence indicators.
- Save draft.
- Approve.
- Reject.
- Reprocess.
- Review audit history.

Acceptance criteria:

- Users can review extracted information.
- Users can correct inaccurate fields.
- Corrections are preserved.
- Approval creates the appropriate financial records.
- Original extraction remains available.
- Review history is recorded.

## PHASE 6 — Expenses, Invoices, Vendors, and Categories

Goal: Build financial record management.

Tasks:

- Expense CRUD.
- Invoice CRUD.
- Payment records.
- Vendor management.
- Vendor matching.
- Category management.
- Expense categorization.
- Search.
- Filters.
- Sorting.
- Pagination.
- Duplicate detection.
- Document relationships.

Acceptance criteria:

- Approved documents create the correct financial records.
- Expenses and invoices remain distinct.
- Payment balances are accurate.
- Vendors can be managed.
- Categories can be customized.
- Duplicate records are identified.

## PHASE 7 — Dashboard, Analytics, and Reporting

Goal: Help users understand their spending.

Tasks:

- Dashboard APIs.
- Monthly spending.
- Category breakdown.
- Vendor breakdown.
- Spending trends.
- Outstanding invoices.
- Review statistics.
- Date filtering.
- Currency-aware reporting.

Acceptance criteria:

- Dashboard uses real data.
- Reports reconcile with underlying records.
- Invoices are not incorrectly counted as paid expenses.
- Date ranges work.
- Empty states work.
- Financial calculations are tested.

## PHASE 8 — Exports

Goal: Convert financial records into usable files.

Tasks:

- CSV export.
- XLSX export.
- Expense export.
- Invoice export.
- Vendor export.
- Monthly reports.
- Export history.
- Secure download.

Acceptance criteria:

- Exports contain correct data.
- CSV escaping works.
- Excel files open correctly.
- Date and currency formatting is consistent.
- Workspace authorization is enforced.

## PHASE 9 — Billing and Subscription Management

Goal: Monetize the application.

Tasks:

- Plan configuration.
- Subscription database.
- Stripe checkout.
- Stripe customer portal.
- Webhook processing.
- Usage tracking.
- Quota enforcement.
- Subscription status.
- Upgrade and downgrade.
- Cancellation.
- Billing history.

Acceptance criteria:

- Users can subscribe.
- Subscription status is synchronized.
- Usage is tracked.
- Backend enforces quotas.
- Webhooks are verified and idempotent.
- Failed payments are handled.

## PHASE 10 — Security, Performance, and Production Readiness

Goal: Prepare for real customers.

Tasks:

- Security review.
- Tenant isolation testing.
- Upload security.
- API security.
- Rate limiting.
- Performance testing.
- Database indexing.
- Backup verification.
- Monitoring.
- Error tracking.
- Accessibility review.
- Deployment documentation.
- Production smoke tests.

Acceptance criteria:

- Security tests pass.
- Tenant isolation tests pass.
- Core workflows pass end-to-end.
- Backups are verified.
- Monitoring is configured.
- Production deployment is documented.

---

# 28. FUTURE ROADMAP

These capabilities should be designed for but not implemented before the MVP is stable.

## 28.1 Bank Integrations

Potential capabilities:

- Connect bank accounts.
- Import transactions.
- Match receipts to transactions.
- Detect missing receipts.
- Identify recurring payments.

Use a supported financial data aggregation provider.

## 28.2 Credit Card Integrations

Potential capabilities:

- Import card transactions.
- Match transactions to receipts.
- Detect duplicate expenses.
- Identify uncategorized spending.

## 28.3 Accounting Integrations

Potential integrations:

- QuickBooks Online.
- Xero.
- Other accounting platforms.

Support mapping between internal categories and accounting categories.

## 28.4 Advanced AI

Potential features:

- Automatic category suggestions.
- Recurring expense detection.
- Unusual spending detection.
- Vendor normalization.
- Duplicate invoice detection.
- Missing receipt identification.
- Natural-language financial queries.

Example:

"How much did I spend on software subscriptions in October?"

The system must answer from authorized financial records, not make up figures.

## 28.5 Accountant Workspace

Potential features:

- Multiple client workspaces.
- Client switching.
- Accountant review.
- Client document requests.
- Bulk exports.
- Accountant-specific roles.

## 28.6 Team Approval Workflows

Potential features:

- Expense approval.
- Manager approval.
- Department-level access.
- Multi-step approval.
- Rejection comments.
- Approval history.

---

# 29. PRODUCT QUALITY STANDARDS

Every feature must meet these standards.

## 29.1 User Experience

- Clear navigation.
- Consistent layouts.
- Meaningful feedback.
- Helpful error messages.
- Responsive behavior.
- Accessible controls.
- No unnecessary complexity.

## 29.2 Data Quality

- Validated financial values.
- Original extraction preservation.
- User corrections preserved.
- Accurate reporting.
- Explicit payment status.
- Duplicate prevention.

## 29.3 Security

- Tenant isolation.
- Backend authorization.
- Secure file storage.
- Safe secrets management.
- Audit logging.
- Secure exports.

## 29.4 Engineering

- Modular code.
- Reusable components.
- Documented APIs.
- Database migrations.
- Automated tests.
- Structured logging.
- Maintainable dependencies.

---

# 30. DEFINITION OF MVP COMPLETE

The MVP is considered complete only when a real user can perform this complete workflow:

1. Register an account.
2. Create a workspace.
3. Log in securely.
4. Open the dashboard.
5. Upload an invoice image.
6. Upload a receipt image.
7. Upload a PDF invoice.
8. See processing status.
9. Have the system classify documents.
10. Extract vendor, date, amount, tax, currency, and other available fields.
11. Review the extracted information.
12. Correct inaccurate values.
13. Approve the records.
14. View expenses.
15. View invoices.
16. Manage vendors.
17. Categorize expenses.
18. See monthly spending.
19. See category spending.
20. See vendor spending.
21. Search and filter records.
22. Export data to CSV.
23. Export data to Excel.
24. View subscription information.
25. Understand monthly document usage.

All financial data must be stored persistently and retrieved from the backend.

The complete workflow must work without relying on hardcoded sample data.

---

# 31. FINAL DEVELOPMENT INSTRUCTIONS

The implementation must prioritize correctness, maintainability, security, and user experience.

The application should be built as a professional multi-tenant SaaS product, not a basic document upload demo.

The most important product capabilities are:

1. Accurate document reading.
2. Reliable structured extraction.
3. Human review and correction.
4. Correct expense and invoice modeling.
5. Useful spending analytics.
6. Secure multi-tenant architecture.
7. Professional frontend design.
8. Reliable subscription and usage management.

Do not sacrifice financial accuracy to make AI processing appear fully automatic.

The user must always be able to review and correct extracted information.

Do not build unnecessary enterprise complexity into the initial MVP. Design the foundations properly, then implement features in the documented phases.

**MASTER DEVELOPMENT RULE:**

Before implementing any phase, review this document, prepare a detailed implementation plan for that phase, identify all required frontend, backend, database, API, security, and testing changes, and then implement the phase completely.

Never start the next phase until the current phase has passed its acceptance criteria.

**End of MASTER.md**
