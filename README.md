# SaaS Starter

A reusable, multi-tenant SaaS foundation with a React/TypeScript frontend, Spring Boot REST API, PostgreSQL, JWT authentication, workspace roles, and Stripe subscriptions.

## Included

- Public landing and pricing pages.
- Account registration, login, and profile settings.
- Workspace creation, switching, and server-enforced membership boundaries.
- Workspace member administration with owner, admin, and member roles.
- Stripe Checkout, customer portal, and signed webhook processing.
- PostgreSQL schema managed by Flyway and REST API docs.

Product-specific invoice, document processing, OCR, and expense application code has been removed. Historical Flyway migrations are retained to preserve migration checksums and existing database data.

## Requirements

- Java 21 and Maven
- Node.js 20+
- Docker Compose (for local PostgreSQL)
- A Stripe account for subscription checkout and the billing portal

## Local development

1. Copy `.env.example` to `.env` and set a random `JWT_SECRET` of at least 32 characters. Set database credentials and Stripe price IDs as needed.
2. Start PostgreSQL:

   ```bash
   docker compose up -d postgres
   ```

3. Load local environment values and start the API:

   ```bash
   set -a
   . ./.env
   set +a
   cd backend
   mvn spring-boot:run
   ```

4. In another terminal, start the frontend:

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

The frontend is available at http://localhost:5173/. The API runs at http://localhost:8080/.

## Stripe setup

Configure the Stripe secret key, webhook signing secret, and six recurring Price IDs in `.env`. The pricing page has editable Starter, Pro, and Team defaults in `BillingService`; update their displayed prices and descriptions to match the Stripe products you configure. For local webhook testing, forward Stripe events to `http://localhost:8080/api/v1/billing/webhook`.

The billing API is unavailable until matching Stripe secrets and Price IDs are configured. Never commit `.env` or production credentials.

## Project structure

- `frontend/` — React application, reusable UI, public pricing, account and workspace administration.
- `backend/` — Spring Boot modular monolith, authentication, tenancy, Stripe billing.
- `backend/src/main/resources/db/migration/` — Flyway schema history and SaaS billing migration.
- `compose.yml` — local PostgreSQL.
- `API.md`, `ARCHITECTURE.md`, `DATABASE.md`, `TESTING.md` — technical reference.

## Validation

```bash
cd backend && mvn test
cd frontend && npm test && npm run build
```
