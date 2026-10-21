# Órdenes de soporte

Backend Spring Boot 3 (Java 17+, H2 en memoria) + frontend React/TypeScript (Vite).
Al iniciar, la BD se carga con 5 órdenes de ejemplo (`data.sql`).

## Ejecución
```bash
# Backend (http://localhost:8080) — consola H2: /h2-console, JDBC: jdbc:h2:mem:support
cd backend && ./mvnw spring-boot:run

# Frontend (http://localhost:5173, proxy a /api)
cd frontend && npm install && npm run dev

# Pruebas backend
cd backend && ./mvnw test
```
Ejemplos de API en [`api.http`](api.http).

## API
| Método | Ruta | Respuesta |
|---|---|---|
| POST | `/api/support-orders` | 201 / 400 validación / 409 duplicado activo |
| GET | `/api/support-orders?status=OPEN` | 200 (filtro opcional) |
| GET | `/api/support-orders/{id}` | 200 / 404 |
| PATCH | `/api/support-orders/{id}/status` | 200 / 404 / 409 transición inválida |

Errores: `{ "status": 409, "message": "..." }`.

## Supuestos
- **Orden activa** = `OPEN` o `IN_PROGRESS`. Una `externalReference` solo puede tener una orden activa; si la anterior está `RESOLVED`/`CANCELLED` se permite crear otra.
- `priority` es opcional, por defecto `MEDIUM`.
- Listado ordenado por fecha de creación descendente, sin paginación.

## 1. Arquitectura
Arquitectura limpia con la dependencia apuntando hacia el dominio (`backend/.../support`):

```
api              controller, dto (un record por archivo), mapper, exception (handler global)
application      usecase (un caso de uso por clase), command
domain           model, exception, repository (puerto), policy, factory  -> sin Spring/JPA/HTTP
infrastructure   persistence (entidad JPA + adaptador del puerto), config (ensamblado del dominio)
```
Los DTOs HTTP, el modelo de dominio y la entidad JPA son tres tipos distintos. Es suficiente para este alcance: cada capa tiene una razón de cambio y no se añadieron puertos/capas sin uso.

## 2. Calidad
- **SOLID**:
  - *S*: un caso de uso por clase; el controller solo traduce HTTP↔DTO; el handler es el único que decide códigos de error.
  - *D*: los casos de uso dependen del puerto `SupportOrderRepository` (dominio), implementado por `SupportOrderRepositoryAdapter` (infraestructura).
  - *O*: añadir un estado o transición es crear una `StatusTransitionRule` sin tocar `StatusTransitionPolicy`.
- **Strategy** (`StatusTransitionRule` + `StatusTransitionPolicy`): cada estado de origen encapsula sus transiciones permitidas; evita `if/else` dispersos. Un estado sin regla (`RESOLVED`, `CANCELLED`) es final.
- **Factory** (`SupportOrderFactory`): centraliza las reglas de creación (estado `OPEN`, prioridad por defecto, fecha del servidor vía `Clock`).

## 3. Restricciones deliberadas
Sin autenticación, paginación, historial/auditoría de cambios, migraciones (Flyway) ni Docker; el frontend no tiene pruebas ni router; sin restricción única a nivel de BD (ver punto 4).

## 4. SQL vs. NoSQL
- **Modelo**: tabla `support_orders` (id, external_reference, customer_id, category, description, channel, priority, status, created_at, updated_at) con índice por `external_reference`.
- **Duplicados**: hoy se valida en el service (`existsByExternalReferenceAndStatusIn`). Bajo concurrencia hay una ventana de carrera; en producción: índice único parcial (`WHERE status IN ('OPEN','IN_PROGRESS')`, PostgreSQL) y capturar la violación como 409.
- **SQL es apropiado** por datos estructurados, transiciones transaccionales y filtros simples. Revisaría NoSQL con grandes volúmenes de eventos/historial, payloads variables por categoría o lecturas masivas de historial: se ganaría escalado horizontal y esquema flexible; se perdería integridad transaccional y consultas relacionales.

## 5. Producción
Autenticación/autorización (OAuth2/JWT, rol operador vs. app móvil); idempotencia con cabecera `Idempotency-Key` en el POST; tabla de auditoría de cambios de estado; observabilidad (logs JSON con correlation id, métricas y trazas, Actuator); rate limiting en el API gateway; paginación (`Pageable`: `?page=&size=`); PostgreSQL + Flyway; CI/CD con contenedores y health checks.

## 6. Trabajo en paralelo
Primero se acuerda el contrato OpenAPI (rutas, DTOs, códigos de error, enums y reglas de transición). Después, en paralelo: backend (API + persistencia), frontend (contra un mock del contrato) y QA/pruebas de contrato. Dependencia: la lista de transiciones es única y debe coincidir entre backend y frontend.

## 7. Recursos adicionales
- **DevOps/SRE**: pipeline, despliegue, observabilidad — paralelizable tras fijar la plataforma.
- **QA automation**: pruebas end-to-end y de contrato — paralelizable con el desarrollo una vez exista el contrato.
- **Diseñador UX**: flujo del operador — paralelizable.
- **Arquitecto/tech lead (una sola persona)**: modelo de datos, estrategia de idempotencia y seguridad; son decisiones únicas que requieren coordinación y no deben dividirse.
