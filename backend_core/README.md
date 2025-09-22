# Core Backend

Kotlin + Spring Boot backend for MobiShare’s core services (core APIs, DB/MQTT integration).

## Contents
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Run](#run)
- [Tests](#tests)

## Prerequisites
- JDK 17+
- Maven 3.9+
- Running MySQL (database `mobishare`)
- Running MQTT broker

## Configuration
File `src/main/resources/application.yaml` (default values):
- `spring.datasource.url`: `jdbc:mysql://localhost:3306/mobishare`
- `spring.datasource.username` / `password`: `core_backend` / `core_backend`
- `server.port`: `9000`
- `mqtt.addr` / `mqtt.port`: `0.0.0.0` / `1883`

For different environments, use Spring profiles or env/JVM variables (`-Dspring.datasource.url=...`).

## Run
```bash
cd backend_core
mvn spring-boot:run
```

## Tests
```bash
mvn test
```
JUnit tests live in `src/test/kotlin/.../*Test.kt`.
