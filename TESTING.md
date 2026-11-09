# Testing Strategy

## Backend

JUnit 5 and Spring Boot Test cover application context startup, REST endpoints, tenant isolation, validation, financial rules, and persistence.

## Frontend

Vitest and React Testing Library cover component behavior. Playwright covers critical end-to-end workflows.

## Release Gate

A phase is complete only when its acceptance criteria pass executable checks, not compilation alone.
