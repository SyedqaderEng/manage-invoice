# REST API

The API base path is `/api/v1`.

## Phase 0 Endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/v1/health` | Application REST health status |
| GET | `/actuator/health` | Operational health status |
| GET | `/api/v1/openapi` | OpenAPI document |
| GET | `/api/v1/docs` | Swagger UI |

## Phase 1 Endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/v1/auth/register` | Register a user and issue a JWT |
| POST | `/api/v1/auth/login` | Authenticate a user and issue a JWT |
| POST | `/api/v1/auth/logout` | Stateless logout acknowledgement |
| GET | `/api/v1/auth/me` | Return the authenticated user |
| GET | `/api/v1/workspaces` | List workspaces available to the authenticated user |
| POST | `/api/v1/workspaces` | Create a workspace and owner membership |
| GET | `/api/v1/workspaces/{workspaceId}/members` | List members after server-side membership authorization |

Tenant APIs require `Authorization: Bearer <token>`. Workspace-specific resources must additionally resolve and authorize the selected workspace on the backend.

Domain controllers must delegate business operations to backend services. The React application must treat these endpoints as its server contract.
