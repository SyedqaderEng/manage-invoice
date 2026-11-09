# Database

## Runtime

PostgreSQL 16 is the development database. Flyway applies versioned migrations at application startup.

## Local Services

`compose.yml` starts PostgreSQL and Redis with the following defaults:

- Database: `manage_invoice`
- User: `manage_invoice`
- Password: `manage_invoice`
- PostgreSQL port: `5432`
- Redis port: `6379`

## Rules

- UUID primary keys for tenant-owned data.
- Explicit currency codes.
- Decimal values, never floating point.
- Foreign keys and tenant-scoped relationships.
- No manual production schema changes.
- Flyway is the only schema migration mechanism.
