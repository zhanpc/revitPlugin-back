# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 2.2.6 backend application for a Revit plugin, integrating BIM functionality with IoT capabilities (WebSocket, MQTT, Netty). Uses MyBatis-Plus for database operations with MySQL and Redis for caching.

## Build and Run Commands

```bash
# Build the project
./mvnw clean package

# Run the application (uses 'local' profile by default, port 8303)
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run tests
./mvnw test
```

## Database Setup

The application requires MySQL and Redis to be running:
- MySQL: localhost:3306, database: `revitPlugin`, user: `root`, password: `root`
- Redis: localhost:6379

## Code Generation

Use the MyBatis-Plus generator to create Entity, Mapper, and Service classes from database tables:

1. Update table name in `src/test/java/com/mapletestone/revitPlugin/MybatisPlusGenerator.java` (line 34)
2. Run the `mySqlGenerator()` test method
3. Generated files:
   - Entity: `src/main/java/com/mapletestone/revitPlugin/dao/entity/`
   - Mapper interface: `src/main/java/com/mapletestone/revitPlugin/dao/mapper/`
   - Mapper XML: `src/main/resources/mapper/`
   - Service: `src/main/java/com/mapletestone/revitPlugin/service/impl/`

Note: Entities extend `BaseEntity` (provides id, created_user, created_time, last_modified_user, last_modified_time). Services extend `BaseIService`.

## Architecture

**Package Structure**: `com.mapletestone.revitPlugin`
- `controller/` - REST endpoints
- `service/` and `service/impl/` - Business logic layer
- `dao/entity/` - JPA entities (extend BaseEntity)
- `dao/mapper/` - MyBatis mapper interfaces
- `config/` - Spring configuration (CORS, MyBatis-Plus)
- `utils/` - Utility classes
- `pojo/` - DTOs and response objects (ResponseVO, RspVo)
- `constant/` - Enums and constants

**Key Patterns**:
- MyBatis-Plus for ORM with XML mappers in `src/main/resources/mapper/`
- Standard response wrapper: `ResponseVO` with `ResponseStatusEnum`
- File uploads stored at path configured in `web.file.root` (default: Z:/revitPlugin)
- Uses Lombok annotations (@Data, @Builder, etc.)

**Configuration Profiles**: local (default), dev, test, prod
- Profile-specific configs in `src/main/resources/config/application-{profile}.yml`
- Active profile set in `application.yml`

## Key Dependencies

- BIMface SDK 3.2.5 - BIM/Revit file processing
- MyBatis-Plus 3.3.1 with join support (mybatis-plus-join)
- WebSocket (Spring + Java-WebSocket client)
- Netty 4.1.90
- MQTT (Eclipse Paho 1.2.2)
- JWT authentication (jjwt + java-jwt)
- Apache POI 3.17 - Excel operations
