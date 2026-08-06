# etq-print (Backend Java)

Submódulo de impresión de ETQ — backend Spring Boot (prueba técnica).

## Requisitos

- Java 25+
- Maven 3.9+

Verificación:

```bash
java -version
mvn -v
```

## Ejecución local

```bash
cd back-java
mvn spring-boot:run
```

La aplicación queda en `http://localhost:8080`.

## Health check

```bash
curl http://localhost:8080/api/v1/health
```

Respuesta esperada: HTTP 200 con `"status": "UP"`.

## Consulta ETQ por LPN

```bash
curl -s -X POST http://localhost:8080/api/v1/etq/consulta \
  -H 'Content-Type: application/json' \
  -d '{"request":{"lpn":"LPN-000987654"}}'
```

LPN de ejemplo (caso feliz): `LPN-000987654`.  
LPN inexistente responde HTTP 404 con `code: LPN_NOT_FOUND`.

## Impresión de ETQ

```bash
curl -s -X POST http://localhost:8080/api/v1/print \
  -H 'Content-Type: application/json' \
  -d '{
    "lpn": "LPN-000987654",
    "zone": "ZONA-PICKING-A",
    "requestedBy": "usuario.operacion",
    "reprintReason": null
  }'
```

Casos semilla:

| LPN | Zona | Resultado esperado |
|-----|------|--------------------|
| `LPN-000987654` | `ZONA-PICKING-A` | `PRINT_OK` / reimpresión `REPRINT_OK` al repetir |
| `LPN-ANULADA-001` | `ZONA-PICKING-A` | `DOCUMENT_INVALID_STATUS` |
| `LPN-DEVUELTA-001` | `ZONA-PICKING-B` | `DOCUMENT_INVALID_STATUS` |
| `LPN-SIN-STOCK-001` | `ZONA-PICKING-C` | inventario / no abastecido |
| `LPN-NO-EXISTE` | cualquier | `LPN_NOT_FOUND` |

Los rechazos de negocio responden HTTP 200 con `success: false`. Campos obligatorios faltantes → HTTP 400.

## Swagger UI

Tras arrancar: `http://localhost:8080/swagger-ui.html`
