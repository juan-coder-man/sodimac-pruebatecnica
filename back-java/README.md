# etq-print (Backend Java)

Submódulo de impresión de ETQ — backend Spring Boot (prueba técnica).

## Requisitos

- Java 25+
- Maven 3.9+

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

## Swagger UI

Tras arrancar: `http://localhost:8080/swagger-ui.html`
