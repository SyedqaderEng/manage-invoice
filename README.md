# Manage Invoice

Manage Invoice is an AI document intelligence and expense management SaaS built as a modular monolith.

## Architecture

- React + TypeScript frontend in `frontend/`
- Spring Boot REST API in `backend/`
- PostgreSQL database and Flyway migrations
- Redis for processing coordination
- Docker Compose for local infrastructure

All business logic belongs in backend services. The frontend calls REST endpoints under `/api/v1` and does not implement financial rules.

## Local Development

Start infrastructure:

```bash
docker compose up -d postgres redis
```

Start the API:

```bash
cd backend
mvn spring-boot:run
```

Start the frontend:

```bash
cd frontend
npm install
npm run dev
```

## URLs

- Frontend: http://localhost:5173/
- REST health: http://localhost:8080/api/v1/health
- Spring health: http://localhost:8080/actuator/health
- API docs: http://localhost:8080/api/v1/docs

## Project Documents

Implementation requirements, architecture, database design, API contracts, testing, and the Phase 0 implementation plan are documented in `plan.md` and the root project documents.
