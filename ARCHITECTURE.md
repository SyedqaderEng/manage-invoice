# Architecture

Manage Invoice uses a modular monolith architecture.

## Backend

The Spring Boot API owns all authentication, authorization, validation, domain rules, persistence, and financial calculations. The backend is organized by product modules under `com.manageinvoice`.

All browser-facing operations use REST endpoints under `/api/v1`. Controllers remain thin; controllers delegate workflows to services.

## Frontend

React and TypeScript provide the application shell and call backend REST APIs. React components contain presentation and interaction state, not financial business rules.

## Infrastructure

PostgreSQL is the source of truth. Flyway owns schema changes. Redis supports asynchronous coordination. Private object storage and background processing will be added in their dedicated phases.
