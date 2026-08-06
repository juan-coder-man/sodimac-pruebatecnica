# API — etq-print

Contrato de respuestas y códigos HTTP del submódulo de impresión de ETQ.

## Envelope

Todas las respuestas de negocio usan:

```json
{
  "success": true,
  "code": "ETQ_FOUND",
  "message": "...",
  "data": {},
  "errors": []
}
```

`errors[]` contiene objetos `{ "field", "code", "message" }` cuando hay fallos de validación.

## Criterio HTTP

| Caso | HTTP | `success` | `code` típico |
|------|------|-----------|---------------|
| Body inválido / campos en blanco | 400 | false | `VALIDATION_ERROR` |
| JSON malformado | 400 | false | `VALIDATION_ERROR` |
| Consulta: LPN no existe | 404 | false | `LPN_NOT_FOUND` |
| Impresión: LPN no existe o regla de negocio | **200** | false | ver códigos de print |
| Éxito consulta / impresión | 200 | true | `ETQ_FOUND`, `PRINT_OK`, `REPRINT_OK` |
| Error no controlado | 500 | false | `INTERNAL_ERROR` |

**Importante:** los rechazos de negocio en impresión responden HTTP 200 con `success: false`. Solo la consulta usa 404 para LPN inexistente.

## Ejemplo 400 — validación

Request:

```json
{ "lpn": "LPN-000987654", "requestedBy": "usuario.operacion" }
```

Response (sin `zone`):

```json
{
  "success": false,
  "code": "VALIDATION_ERROR",
  "message": "La solicitud contiene datos invalidos",
  "data": null,
  "errors": [
    {
      "field": "zone",
      "code": "NOTBLANK",
      "message": "La zona es obligatoria"
    }
  ]
}
```

## Endpoints

### `GET /api/v1/health`

Salud del servicio.

### `POST /api/v1/etq/consulta`

Body:

```json
{ "request": { "lpn": "LPN-000987654" } }
```

- 200 + `ETQ_FOUND` si existe
- 404 + `LPN_NOT_FOUND` si no existe
- 400 si falta `request` o `lpn`

### `POST /api/v1/print`

Body:

```json
{
  "lpn": "LPN-000987654",
  "zone": "ZONA-PICKING-A",
  "requestedBy": "usuario.operacion",
  "reprintReason": null
}
```

Campos obligatorios: `lpn`, `zone`, `requestedBy`.

#### Códigos de negocio (impresión)

| `code` | Significado |
|--------|-------------|
| `PRINT_OK` | Impresión exitosa |
| `REPRINT_OK` | Reimpresión exitosa |
| `LPN_NOT_FOUND` | LPN inexistente (HTTP 200) |
| `DOCUMENT_INVALID_STATUS` | Documento ANULADA o DEVUELTA |
| `INSUFFICIENT_INVENTORY` | Stock insuficiente en la zona |
| `PRODUCT_NOT_SUPPLIED` | Producto no abastecido en la zona |
| `VALIDATION_ERROR` | Request inválido (HTTP 400) |

### `GET /api/v1/print/history`

Consulta el historial de impresiones/reimpresiones (auditoría en memoria).

Query params opcionales:

| Param | Descripción |
|-------|-------------|
| `lpn` | Filtrar por LPN |
| `zone` | Filtrar por zona |
| `result` | `EXITOSO` o `RECHAZADO` |

- 200 + `HISTORY_OK` con lista (más reciente primero)
- 400 + `VALIDATION_ERROR` si `result` no es válido

Ejemplo:

```bash
curl -s 'http://localhost:8080/api/v1/print/history?lpn=LPN-000987654&result=EXITOSO'
```

Cada ítem incluye: `etqId`, `lpnId`, `zone`, `requestedBy`, `printedAt`, `result`, `eventType`, `reason`, `reprintReason`.

