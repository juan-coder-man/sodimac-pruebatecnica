# Supuestos y decisiones de diseño

Decisiones tomadas en el backend `etq-print` para la prueba técnica. No introducen requisitos nuevos: documentan lo ya implementado.

## Datos mock

- Las órdenes se cargan desde `classpath:mocks/orders.json` (basado en el material de la prueba + casos semilla).
- El inventario por zona **no venía completo en el zip**; se modeló en `classpath:mocks/inventory.json` (`zone`, `productCode`, `availableQty`, `supplied`).
- La auditoría de impresiones **no** se lee de un JSON histórico: se genera en runtime en memoria.

## Estados de documento

| Status | Impresión |
|--------|-----------|
| `CREADA`, `LIBERADA` | Permitida (si pasan el resto de reglas) |
| `ANULADA`, `DEVUELTA` | Rechazada → `DOCUMENT_INVALID_STATUS` |

## Criterio HTTP

| Caso | HTTP | `success` |
|------|------|-----------|
| Validación Bean Validation / JSON inválido | 400 | false (`VALIDATION_ERROR`) |
| Consulta: LPN inexistente | 404 | false (`LPN_NOT_FOUND`) |
| Print: LPN inexistente o regla de negocio | **200** | false (código de negocio) |
| Éxito consulta / print / history | 200 | true |

Detalle: [API.md](API.md).

## Inventario y zona

- La zona usada en las reglas es la del **request de impresión**, no solo la de la orden.
- Sin ítem en la zona, o `supplied=false` → `PRODUCT_NOT_SUPPLIED`.
- `availableQty < requestedQty` (y abastecido) → `INSUFFICIENT_INVENTORY`.

## Reimpresión

- Si existe al menos un evento de auditoría con `result=EXITOSO` para el mismo LPN → se trata como reimpresión (`REPRINT_OK`, `eventType=REIMPRESION`).
- `reprintReason` es opcional en el contrato; se guarda en auditoría cuando viene informado.

## Persistencia

- Órdenes e inventario: solo lectura desde JSON en classpath.
- Auditoría: `InMemoryPrintAuditRepository` (se pierde al reiniciar la JVM).

## Fuera de alcance

- Generar ETQ o ZPL nuevos (las labels ya vienen pre-generadas en el mock).
- Integración con sistemas reales de tienda / WMS / impresoras físicas.
- Frontend Angular (entregable aparte; CORS ya habilitado para `http://localhost:4200`).
- Docker, H2 persistente, Actuator (opcionales de fase posterior).

## Casos semilla

| LPN | Zona sugerida | Escenario |
|-----|---------------|-----------|
| `LPN-000987654` | `ZONA-PICKING-A` | Feliz → `PRINT_OK` / luego `REPRINT_OK` |
| `LPN-ANULADA-001` | `ZONA-PICKING-A` | Documento anulado |
| `LPN-DEVUELTA-001` | `ZONA-PICKING-B` | Documento devuelto |
| `LPN-SIN-STOCK-001` | `ZONA-PICKING-C` | Producto no abastecido |
| `LPN-NO-EXISTE` | cualquiera | LPN inexistente |
