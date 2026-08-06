# Arquitectura — Frontend ETQ

UI Angular del submódulo de impresión de ETQ. App propia (sin plantilla Hub ni auth).

## Stack

- Angular 21 (standalone, signals, control flow `@if`/`@for`)
- Reactive Forms
- SCSS propio (sin PrimeNG/Tailwind de plantilla corporativa)
- `HttpClient` tipado contra `http://localhost:8080/api/v1`

## Capas

```
pages/       Orquestación de pantallas (print, history)
services/    HTTP tipado: Etq, Print, History, Health
core/        models (ApiResponse, DTOs) + utils (códigos, toUiFailure)
shared/      app-shell, etq-detail, print-result, api-failure
```

Las pages no hacen HTTP crudo: delegan en services. Los shared son presentacionales.

## Flujo UI → API

```mermaid
sequenceDiagram
  actor Op as Operador
  participant UI as Angular_4200
  participant API as Java_8080
  Op->>UI: Consulta LPN
  UI->>API: POST /etq/consulta
  API-->>UI: detalle + ZPL + documentStatus
  Op->>UI: Imprime LPN + zona + usuario
  UI->>API: POST /print
  API-->>UI: PRINT_OK o rechazo o REPRINT_OK
  Op->>UI: Abre Historial
  UI->>API: GET /print/history
  API-->>UI: lista auditoria
```

## Regla crítica: `success` vs HTTP

| Caso | HTTP | Cómo lo trata el FE |
|------|------|---------------------|
| Print OK / reimpresión | 200 | `success: true` → panel resultado |
| Print rechazo de negocio | **200** | `success: false` → panel rechazo (no es error RxJS) |
| Consulta LPN inexistente | **404** | `handleApiCall` → `UiFailure` |
| Validación body | 400 | `UiFailure` + `fieldErrors` mapeados al form |

La UI de impresión **no** decide solo por `status === 200`.

## C4 — Container

```mermaid
flowchart LR
  operador[Operador_tienda]
  angular[Frontend_Angular_4200]
  api[etq_print_API_8080]
  mocks[Mocks_JSON]
  audit[Auditoria_in_memory]

  operador --> angular
  angular -->|"REST_CORS"| api
  api --> mocks
  api --> audit
```

## Fuera de alcance (FE)

- Generar ETQ nuevas
- Impresora física / envío ZPL a dispositivo
- Login Hub, APIM, tokens
- Persistencia local de historial (el back es in-memory)
