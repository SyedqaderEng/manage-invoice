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

## Phase 3 Endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/v1/documents` | Upload a supported document to the workspace in `X-Workspace-Id` |
| GET | `/api/v1/documents` | List non-deleted workspace documents |
| GET | `/api/v1/documents/{documentId}/download` | Download an authorized workspace document |
| DELETE | `/api/v1/documents/{documentId}` | Soft-delete an authorized workspace document |

Uploads are validated from file signatures, not filename extensions. Duplicate SHA-256 checksums are rejected within the workspace. Files are stored through the private object-storage abstraction; callers receive metadata only.

## Phase 4 Endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/v1/documents/{documentId}/processing-status` | Return document and processing job status |
| GET | `/api/v1/documents/{documentId}/extraction` | Return the latest normalized extraction |
| POST | `/api/v1/documents/{documentId}/reprocess` | Queue authorized document reprocessing |

All document processing endpoints require the authenticated user, `X-Workspace-Id`, and workspace membership. OCR uses the configured provider abstraction; extraction confidence below review thresholds routes documents to review-required status.

## Phase 5 Endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | `/api/v1/documents/{documentId}/review` | Return original and corrected review state |
| POST | `/api/v1/documents/{documentId}/review/save` | Save a reviewer draft |
| POST | `/api/v1/documents/{documentId}/review/approve` | Approve corrected extraction data |
| POST | `/api/v1/documents/{documentId}/review/reject` | Record rejection with optional notes |

Review actions preserve original extraction data separately from corrections. Approval changes review state only; Phase 6 owns creation of expense and invoice records.

Domain controllers must delegate business operations to backend services. The React application must treat these endpoints as its server contract.
