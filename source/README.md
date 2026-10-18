# notificaciones-ms

Microservicio Spring Boot **generado por Scafftrix** (perfil `SPRING_MICROSERVICE`) y
adaptado a un dominio real: el envio y la consulta de notificaciones.

Conserva lo que Scafftrix decide por defecto —enfoque **Contract-First** en el que el
contrato OpenAPI es la fuente de verdad, arquitectura hexagonal, Flyway, MapStruct y
tests— y solo se ha cambiado el dominio: el esqueleto que genera la fabrica aguanto el
cambio sin tocar la arquitectura.

## Stack

- Java 17 y Spring Boot 4.1.1
- Maven
- OpenAPI Generator (interfaces y modelos de la API a partir de `contract/api.yaml`)
- MapStruct (mapeo entre capas)
- Flyway (migraciones de base de datos)
- PostgreSQL en produccion y H2 en memoria para desarrollo y tests

## Arquitectura

El microservicio sigue una arquitectura hexagonal (puertos y adaptadores):

```
             contrato OpenAPI (contract/api.yaml)
                          |
                          v
   api (controllers, DTOs generados, api mappers)   <- adaptador de entrada
                          |
                          v
   application (casos de uso, puertos in/out)        <- logica de aplicacion
                          |
                          v
   domain (modelo y excepciones)                     <- nucleo sin dependencias
                          ^
                          |
   infrastructure (JPA, adaptadores, notifier)       <- adaptadores de salida
```

Regla de dependencias: `domain` no depende de nadie; `application` depende de `domain`;
`api` e `infrastructure` dependen de `application` y `domain`. El modelo de dominio usa
`record` inmutables; las entidades JPA viven solo en `infrastructure`.

## El puerto Notificador

La pieza que desacopla el servicio de quien realmente entrega el mensaje es el puerto de
salida `application/port/out/Notificador`:

```java
public interface Notificador {
    ResultadoEnvio enviar(String destinatario, String asunto, String cuerpo);
}
```

El caso de uso `NotificacionService` registra la notificacion como `PENDIENTE`, llama al
puerto y guarda el estado final (`ENVIADO` o `FALLIDO` con su motivo). El adaptador actual,
`LoggingNotificador`, deja constancia en el log y no requiere ninguna infraestructura.

Sustituirlo por un adaptador real (SMTP, o una cola tipo RabbitMQ con su consumidor) no
toca una sola linea del dominio ni del caso de uso: eso es lo que compra el puerto. Un
fallo de envio ademas no lanza excepcion, devuelve `ResultadoEnvio.fallo(...)`, porque es
un resultado esperado que hay que registrar, no un error que deba tumbar el proceso.

Las notificaciones son inmutables: no se editan ni se borran, porque son un registro de
envio. Si hay que repetir una, se crea otra.

## Como ejecutar

Arrancar en local con H2 en memoria (datos de ejemplo ya cargados por Flyway):

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Consola H2 (perfil `local`): http://localhost:8080/h2-console

## Tests

```bash
mvn test
```

20 tests en verde. Cubren: arranque del contexto con Flyway + validacion de esquema,
mappers (MapStruct, incluido el cruce de enums de estado), servicios de aplicacion
(Mockito, con el `Notificador` simulado), adaptadores de persistencia contra H2
(`@DataJpaTest`), el adaptador de envio y los controladores (`@WebMvcTest`).

## Base de datos con PostgreSQL

```bash
mvn clean package
export POSTGRES_PASSWORD='...'    # la contraseña real, fuera del repo
export DB_PASSWORD="$POSTGRES_PASSWORD"
docker compose up --build
```

El `docker-compose.yml` levanta PostgreSQL y el microservicio conectado a el. **No empaqueta
ninguna contraseña**: si `POSTGRES_PASSWORD` o `DB_PASSWORD` no estan definidas, compose se
niega a arrancar en lugar de arrancar con un valor predecible. La aplicacion se configura por
variables de entorno:

| Variable      | Descripcion                | Por defecto     |
|---------------|----------------------------|-----------------|
| `DB_URL`      | URL JDBC                   | H2 en memoria   |
| `DB_USERNAME` | Usuario de base de datos   | `sa`            |
| `DB_PASSWORD` | Contrasena de base de datos| **obligatoria en compose**, vacio en local con H2 |
| `DB_DRIVER`   | Driver JDBC                | `org.h2.Driver` |
| `SERVER_PORT` | Puerto HTTP                | `8080`          |

## Endpoints

| Metodo | Ruta                  | Descripcion                                        |
|--------|-----------------------|----------------------------------------------------|
| GET    | `/notificaciones`     | Lista las notificaciones registradas               |
| GET    | `/notificaciones/{id}`| Obtiene una notificacion por id                    |
| POST   | `/notificaciones`     | Registra y envia una notificacion (201 con estado) |

El `id` y el `estado` los asigna el servidor: el cliente solo envia `destinatario`,
`asunto` y `cuerpo`.

## Modificar el contrato

1. Edita `src/main/resources/contract/api.yaml`.
2. Ejecuta `mvn generate-sources` (o `mvn compile`).
3. Ajusta los controladores y mappers si cambian las firmas generadas.

Los ficheros generados no se editan a mano: se regeneran en cada build.
