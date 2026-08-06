# Submódulo de impresión de ETQ

Prueba técnica GTL Tienda (Sodimac): solución fullstack desacoplada para consultar ETQ/LPN, validar reglas de negocio, simular impresión/reimpresión y consultar historial.

## Levantar en local

### 1. Backend (puerto 8080)

```bash
cd back-java
mvn spring-boot:run
```

Health: [http://localhost:8080/api/v1/health](http://localhost:8080/api/v1/health)  
Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 2. Frontend (puerto 4200)

```bash
cd front
npm install
npm start
```

UI: [http://localhost:4200](http://localhost:4200)

CORS está habilitado en el back para `http://localhost:4200`.

## Módulos

| Carpeta | Rol |
|---------|-----|
| [`back-java/`](back-java/) | API Spring Boot (mocks, reglas, auditoría in-memory) |
| [`front/`](front/) | UI Angular (impresión + historial) |

## Demo rápida

1. En `/print`, consultar `LPN-000987654` (precarga zona).
2. Completar usuario e **Imprimir** → `PRINT_OK`.
3. Imprimir de nuevo → `REPRINT_OK`.
4. Abrir **Historial** y refrescar → eventos visibles.
5. Probar rechazo: `LPN-ANULADA-001` o `LPN-NO-EXISTE`.

## Documentación

| Documento | Contenido |
|-----------|-----------|
| [back-java/README.md](back-java/README.md) | Cómo correr el API y estructura |
| [back-java/docs/API.md](back-java/docs/API.md) | Contratos HTTP y códigos |
| [back-java/docs/ARQUITECTURA.md](back-java/docs/ARQUITECTURA.md) | Capas y C4 del back |
| [front/README.md](front/README.md) | Cómo correr la UI |
| [front/docs/ARQUITECTURA.md](front/docs/ARQUITECTURA.md) | Capas FE y flujo UI→API |
| [front/docs/PRUEBAS.md](front/docs/PRUEBAS.md) | Tests del frontend |

## Fuera de alcance

Generación de ETQ nuevas, impresoras físicas, sistemas corporativos reales (Oracle, Hub auth, APIM).
