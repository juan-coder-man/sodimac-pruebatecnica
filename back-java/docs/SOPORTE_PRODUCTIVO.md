# Soporte productivo — escenario productivo

**Caso del enunciado:** entre las 9:00 y las 10:00 AM, múltiples tiendas reportan que las impresiones fallan de forma intermitente.

Respuesta orientada a las capacidades actuales de `etq-print` (logs SLF4J + MDC `requestId`, health, historial, códigos de negocio).

---

## Diagnóstico

### Qué revisaría primero

1. **Salud del servicio:** `GET /api/v1/health` — descartar caída total vs fallos parciales.
2. **Tipo de fallo:** ¿HTTP 5xx / timeout, HTTP 400 de validación, o HTTP 200 con `success=false`?
   - Rechazos de negocio (`DOCUMENT_INVALID_STATUS`, `PRODUCT_NOT_SUPPLIED`, `INSUFFICIENT_INVENTORY`, `LPN_NOT_FOUND`) no son “caída del sistema”; son reglas aplicadas.
3. **Alcance:** ¿todas las tiendas/zonas o un subconjunto? Pedir LPN, zona, usuario y hora aproximada de 2–3 casos fallidos.
4. **Correlación:** agrupar por ventana 09:00–10:00 y por `zone` / `code` en historial y logs.

### Qué métricas consultaría

En este MVP no hay Actuator/métricas Prometheus; usaría proxies operativos:

| Señal | Cómo obtenerla |
|-------|----------------|
| Tasa éxito vs rechazo | `GET /api/v1/print/history` filtrando `result=EXITOSO` vs `RECHAZADO` en la ventana |
| Códigos dominantes | Contar `code` / `reason` en respuestas y logs (`DOCUMENT_INVALID_STATUS`, etc.) |
| Latencia percibida | Timestamps de logs `Inicio impresion` → `Impresion exitosa/rechazada` |
| Volumen | Conteo de requests en la hora pico |

En producción real sumaría RPS, p95, error rate 5xx y saturación de CPU/memoria/hilos del contenedor.

### Qué logs inspeccionaría

- Prefijo / campos: `requestId` (MDC), `lpn`, `zone`, `code`, `result`, `eventType`.
- Líneas típicas:
  - `Inicio impresion lpn=... zone=...`
  - `Validacion rechazo code=...` / `Validacion OK ... reprint=...`
  - `Impresion rechazada requestId=... code=...`
  - `Impresion exitosa requestId=... code=PRINT_OK|REPRINT_OK`
- Stack traces solo si hay `INTERNAL_ERROR` (HTTP 500).
- Comparar tiendas que fallan vs una tienda control que imprime bien en la misma ventana.

### Cómo identificaría la causa raíz

1. Clasificar: **infra** (timeouts/5xx) vs **datos/reglas** (200 + `success=false`) vs **cliente** (400 / body incompleto).
2. Si es regla de negocio concentrada en una zona → revisar inventario mock / abastecimiento de esa zona.
3. Si es intermitente sin patrón de código → sospechar red, reinicios, o contención de recursos; cruzar con health y horarios de deploy.
4. Confirmar con un LPN semilla conocido (`LPN-000987654` + `ZONA-PICKING-A`) y uno del caso reportado.

---

## Comunicación

### Cómo informaría al usuario operativo

Mensaje corto y accionable, por ejemplo:

> Entre 9:00 y 10:00 se observaron fallos intermitentes de impresión en varias tiendas. Estamos correlacionando por zona y código de rechazo. Mientras tanto: reintente una vez; si el mensaje indica documento anulado/devuelto o sin stock, no es un fallo técnico — revise el LPN/zona con el supervisor. Compartiremos avance en [canal/hora].

Evitar jerga interna; distinguir “rechazo de negocio” de “servicio caído”.

### Contingencia

- Reintento controlado (1–2) ante timeout/5xx; no spamear print.
- No forzar reimpresiones masivas sin motivo (`reprintReason`) mientras se investiga.
- Si una zona está mal abastecida: desviar picking / corregir inventario; no insistir con el mismo LPN.
- Canal alterno temporal: Postman/Swagger o mesa de ayuda con LPN + zona + `requestId` del log/respuesta.
- Congelar cambios de despliegue hasta estabilizar la ventana.

---

## Cierre

Documentaría el incidente en un postmortem breve:

| Campo | Contenido |
|-------|-----------|
| Timeline | Primera alerta → mitigación → resolución |
| Impacto | Tiendas afectadas, volumen de prints fallidos, duración |
| Causa raíz | Hecho verificado (ej. inventario zona X, pico de 5xx, etc.) |
| Fix | Cambio aplicado (datos, config, código, capacidad) |
| Verificación | Health OK + print semilla OK + historial sin pico anómalo |
| Prevención | Alerta por tasa de `RECHAZADO`/`5xx`, dashboard de códigos, runbook con este flujo |

Adjuntar: 2–3 `requestId` de ejemplo, filtros de historial usados y conclusión (negocio vs plataforma).
