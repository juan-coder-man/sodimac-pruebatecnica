# Pruebas — Frontend ETQ

## Cómo ejecutar

```bash
cd front
npm test -- --watch=false
```

También: `npx ng test --watch=false`.

Por defecto el runner es **Vitest** (jsdom).

## Qué cubre

| Grupo | Archivos | Enfoque |
|-------|----------|---------|
| Utils | `core/utils/api-error.util.spec.ts` | `messageForCode`, `isApiResponse`, `toUiFailure` |
| Services | `etq-api`, `print-api`, `history-api` `*.spec.ts` | Contratos HTTP con `HttpTestingController` |
| Página | `pages/print/print-page.spec.ts` | Form inválido no llama API; válido sí (trim) |
| Smoke | `app.spec.ts` | Crea el componente raíz |

## Nota de negocio (print)

Los rechazos de reglas de negocio en `POST /print` responden **HTTP 200** con `success: false`. El service debe emitir en `next`, no en `error`. Solo 4xx/5xx (p. ej. consulta 404) se normalizan a `UiFailure` vía `handleApiCall`.
