# Frontend Angular — Impresión de ETQ

Aplicación Angular propia del submódulo de impresión de etiquetas (prueba técnica GTL Tienda).
Proyecto limpio (`ng new`), sin plantilla Hub.

## Requisitos

| Herramienta | Versión |
|-------------|---------|
| Node.js | 20+ / 24.x recomendado |
| npm | 10+ |
| Backend Java | `http://localhost:8080` |

## Ejecución

```bash
cd front
npm install
npm start
```

Abre [http://localhost:4200](http://localhost:4200).

| Ruta | Descripción |
|------|-------------|
| `/print` | Consulta LPN + impresión (incluye **Copiar ZPL**) |
| `/history` | Historial de impresiones (incluye **Exportar CSV**) |
| `/` | Redirect a `/print` |

Backend (en otra terminal):

```bash
cd ../back-java
mvn spring-boot:run
```

Swagger del API: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Configuración

[`src/environments/environment.ts`](src/environments/environment.ts):

```ts
apiUrl: 'http://localhost:8080/api/v1'
```

CORS del back debe permitir `http://localhost:4200`.

## Estructura

```
src/app/
├── pages/print/      # Consulta + impresión
├── pages/history/    # Historial + filtros
├── services/         # Etq, Print, History, Health API
├── core/models/      # ApiResponse y DTOs
├── core/utils/       # códigos y mapeo de errores
└── shared/           # shell, etq-detail, print-result, api-failure
```

## Datos de prueba (mocks del back)

| LPN | Uso |
|-----|-----|
| `LPN-000987654` | Impresión OK / reimpresión (`ZONA-PICKING-A`) |
| `LPN-ANULADA-001` | Documento anulado |
| `LPN-DEVUELTA-001` | Documento devuelto |
| `LPN-SIN-STOCK-001` | Inventario insuficiente / no abastecido |
| `LPN-NO-EXISTE` | 404 en consulta |

## Documentación

| Documento | Contenido |
|-----------|-----------|
| [docs/ARQUITECTURA.md](docs/ARQUITECTURA.md) | Capas FE, flujo UI→API, `success` vs HTTP |
| [docs/PRUEBAS.md](docs/PRUEBAS.md) | Cómo correr tests y qué cubren |
| [../back-java/docs/API.md](../back-java/docs/API.md) | Contratos HTTP del backend |

## Pruebas

```bash
npm test -- --watch=false
```

Detalle: [`docs/PRUEBAS.md`](docs/PRUEBAS.md).
