# SaaS Starter Status

## Included foundations

- Spring Boot REST API with JWT registration, login, logout, and authenticated profile management.
- PostgreSQL/Flyway persistence and workspace membership isolation.
- Workspace creation, switching, membership management, and owner/admin/member roles.
- Responsive React app shell, public landing/pricing, profile settings, team admin, and billing views.
- Stripe Checkout, customer portal, signed webhook verification, and idempotent event handling.

## Configuration required

- `JWT_SECRET` must be a random value of at least 32 bytes.
- Configure Stripe API and webhook secrets plus the recurring price IDs for Starter, Pro, and Team.
- Edit the sample plans/prices in `backend/src/main/java/com/saasstarter/billing/BillingService.java` to match product configuration.

## Current limitations

- Team management can add an account that already exists; invitation email delivery is not configured.
- The displayed price values and plan copy are starter defaults, not a commercial recommendation.
- Password reset and email verification are not included.
- Historical invoice/document Flyway migrations remain for database compatibility, but application code for those domains is removed.
