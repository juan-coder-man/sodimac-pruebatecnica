# Arquitectura — etq-print

Backend Spring Boot del submódulo de impresión de ETQ. Paquete base: `co.homecenter.etq`.

## Capas

```
api            Controllers, DTOs, excepciones, GlobalExceptionHandler
application    Services, mappers
domain         Modelos, enums, puertos (repositorios), reglas
infrastructure Mocks JSON, auditoría in-memory, CORS, OpenAPI
```

Dependencia: `api` → `application` → `domain`; `infrastructure` implementa los puertos de `domain`.

## SOLID (aplicación concreta)

| Principio | Ejemplo |
|-----------|---------|
| S | `PrintValidationService` solo valida reglas; no imprime ni persiste HTTP |
| O / D | Controllers dependen de interfaces (`EtqQueryService`, `PrintService`); repos son puertos |
| L / I | Impl mock e in-memory intercambiables tras el mismo contrato de repositorio |

## Flujo de impresión

1. El cliente consulta ETQ por LPN (`POST /api/v1/etq/consulta`) o envía impresión directa.
2. `PrintService` resuelve orden/label por LPN.
3. `PrintValidationService` aplica reglas de documento e inventario por zona.
4. Se registra auditoría (éxito o rechazo) en memoria.
5. Se responde con envelope unificado (`success`, `code`, `message`, `data`).

---

## C4

### Context

```mermaid
flowchart LR
  operador[Operador_tienda]
  angular[Frontend_Angular]
  api[etq_print_API]
  mocks[Mocks_JSON]
  auditMem[Auditoria_in_memory]

  operador --> angular
  operador -->|"curl_Postman_Swagger"| api
  angular -->|"REST_CORS"| api
  api -->|"lee"| mocks
  api -->|"escribe_consulta"| auditMem
```

### Container

```mermaid
flowchart TB
  operador[Operador]
  subgraph solucion [Solucion_ETQ]
    app["etq_print SpringBoot_8080"]
    json[Classpath_mocks]
    mem[PrintAudit_memoria]
  end

  operador -->|"REST_JSON"| app
  app --> json
  app --> mem
```

### Component

```mermaid
flowchart TB
  ctrl[Controllers]
  svc[Application_services]
  mapper[EtqMapper]
  rules[PrintValidationService]
  orderPort[OrderRepository]
  invPort[InventoryRepository]
  auditPort[PrintAuditRepository]
  mockOrder[OrderMockRepository]
  mockInv[InventoryMockRepository]
  auditImpl[InMemoryPrintAuditRepository]

  ctrl --> svc
  svc --> mapper
  svc --> rules
  svc --> orderPort
  svc --> invPort
  svc --> auditPort
  rules --> invPort
  rules --> auditPort
  orderPort --> mockOrder
  invPort --> mockInv
  auditPort --> auditImpl
```
