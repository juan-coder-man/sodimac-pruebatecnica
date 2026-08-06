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

## Swagger UI

Tras arrancar: `http://localhost:8080/swagger-ui.html`
