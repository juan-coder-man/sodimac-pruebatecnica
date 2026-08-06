# Pruebas automatizadas — etq-print

## Cómo ejecutar

```bash
cd back-java
mvn test
```

Requisitos: Java 25+, Maven 3.9+.

## Qué cubre cada tipo de test

| Tipo | Clase | Alcance |
|------|-------|---------|
| Unit | `PrintValidationServiceTest` | Reglas de documento, inventario y reimpresión (repos mockeados) |
| Unit | `EtqMapperTest` | Mapeo Order/Label → `EtqDetailResponse` |
| Slice | `EtqControllerTest` | HTTP consulta: 200 / 400 / 404 vía `@WebMvcTest` |
| Slice | `PrintControllerTest` | HTTP print: 400 validación; 200 éxito; 200 rechazo negocio |
| Integración | `PrintFlowIntegrationTest` | Flujo real contra mocks JSON del classpath |

## Criterio de las 5 reglas de impresión

| # | Regla | Evidencia |
|---|-------|-----------|
| 1 | LPN inexistente | WebMvcTest consulta → 404 `LPN_NOT_FOUND`; print service → 200 `LPN_NOT_FOUND` |
| 2 | Documento `ANULADA` / `DEVUELTA` | Unit `DOCUMENT_INVALID_STATUS`; integración con `LPN-ANULADA-001` |
| 3 | Producto no abastecido (`supplied=false` o sin ítem) | Unit `PRODUCT_NOT_SUPPLIED` |
| 4 | Inventario insuficiente | Unit `INSUFFICIENT_INVENTORY` |
| 5 | Reimpresión si ya hubo impresión exitosa | Unit `reprint=true`; integración `PRINT_OK` → `REPRINT_OK` |

Los mocks semilla viven en `src/main/resources/mocks/` (`orders.json`, `inventory.json`).
