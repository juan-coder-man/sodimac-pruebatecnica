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

Abre [http://localhost:4200](http://localhost:4200). La ruta `/print` incluye un botón para verificar `GET /api/v1/health`.

Backend:

```bash
cd ../back-java
mvn spring-boot:run
```

## Configuración

[`src/environments/environment.ts`](src/environments/environment.ts):

```ts
apiUrl: 'http://localhost:8080/api/v1'
```

CORS del back debe permitir `http://localhost:4200`.

## Rutas (Fase 0)

| Ruta | Descripción |
|------|-------------|
| `/print` | Placeholder + check de health |
| `/` | Redirect a `/print` |

Formulario de impresión e historial: fases siguientes.
