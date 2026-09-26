# AGENTS.md

Quarkus REST API (Gradle Kotlin DSL). Package root is `id.my.agungdh`. REST resources live in `id.my.agungdh.resource`. Config lives in `application.yml` (not `.properties`).

## Commands

Use the Gradle wrapper (`./gradlew`) — this is a Gradle project, not Maven.

- `./gradlew quarkusDev` — dev mode with live reload; Dev UI at http://localhost:8080/q/dev/
- `./gradlew build` — package; runnable jar at `build/quarkus-app/quarkus-run.jar`
- `./gradlew test` — run `@QuarkusTest` unit tests
- `./gradlew test --tests id.my.agungdh.GreetingResourceTest` — single test class
- `./gradlew build -Dquarkus.native.enabled=true` — build native image (add `-Dquarkus.native.container-build=true` to build in a container without GraalVM)
- `./gradlew testNative` — run `@QuarkusIntegrationTest` native tests (see Gotchas)

## Gotchas

- **Java 25 required**: `build.gradle.kts` sets `sourceCompatibility = JavaVersion.VERSION_25`.
- **REST stack is `quarkus-rest` (RESTEasy Reactive).** It is incompatible with `quarkus-resteasy` and anything that depends on it — do not add them together.
- **Quarkus/Gradle/Lombok versions live in `gradle.properties`** (`quarkusPluginVersion`, `quarkusPlatformVersion`, `mapstructVersion`, `lombokVersion`), not in `build.gradle.kts`; the build script reads them via `by project`.
- **Lombok is enabled** (`@Getter`, `@Setter`, etc.). It works together with MapStruct via the `lombok-mapstruct-binding` annotation processor (already configured in `build.gradle.kts`). Keep Lombok on the `compileOnly` + `annotationProcessor` classpaths, not `implementation`.
- **Native integration tests are not run by `./gradlew test`.** `src/native-test/` holds `@QuarkusIntegrationTest` (`GreetingResourceIT` extends the unit test) and runs via `./gradlew testNative`, which requires a native image built first. There is no `src/integrationTest/` source set.
- **Config is `application.yml`** (YAML), not `application.properties`. Keep it in `src/main/resources/`. YAML requires the `quarkus-config-yaml` extension (already added to `build.gradle.kts`).
- **Virtual threads: annotate REST endpoints with `@RunOnVirtualThread`** (`io.smallrye.common.annotation.RunOnVirtualThread`). Quarkus has no global "run everything on virtual threads" switch. For CPU-heavy endpoints (future), omit the annotation so they run on worker threads instead.
- **Database: PostgreSQL with Flyway.** Datasource + Flyway configured in `application.yml`. Migrations in `src/main/resources/db/migration/`. Hibernate is set to `generation: none` — schema is managed by Flyway only.
- **Base entity pattern**: `BaseEntity` is a `@MappedSuperclass` providing `id` (BIGINT GENERATED ALWAYS AS IDENTITY), `uuid` (UUID), `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `deletedAt`, `deletedBy`. Extend `BaseEntity` for new entities — do not redeclare these fields.
- **UUID is the public identifier.** Expose `uuid` to FE (not `id`). REST paths use `/{uuid}`. Repository provides `findByUuid(UUID)`.
- **Soft delete.** `deletedAt` column — set to `now()` on delete, filter `deletedAt IS NULL` on reads. Do not physically delete rows.
- **Audit columns are nullable.** `createdBy`/`updatedBy`/`deletedBy` are nullable Long (references pegawai `id`) — all audit columns in `BaseEntity` are nullable.
- **Partial indexes for unique constraints.** When a unique column must be unique among non-deleted rows, use `CREATE UNIQUE INDEX ... WHERE deleted_at IS NULL` instead of `UNIQUE` constraint.
- `build/` and `target/` are gitignored build/dev artifacts.

## Layout

- `src/main/java/id/my/agungdh/resource/` — JAX-RS resources (annotated `@RunOnVirtualThread`)
- `src/main/java/id/my/agungdh/entity/` — domain entities extending `BaseEntity` (Lombok-annotated, Panache)
- `src/main/java/id/my/agungdh/dto/` — request/response records
- `src/main/java/id/my/agungdh/mapper/` — MapStruct mappers (`componentModel = "cdi"`)
- `src/main/java/id/my/agungdh/repository/` — data access (Panache, `PanacheRepositoryBase`)
- `src/main/java/id/my/agungdh/service/` — business logic (CDI beans, `@Transactional`)
- `src/main/resources/db/migration/` — Flyway SQL migrations
- `src/test/java/` — `@QuarkusTest` unit tests (run in JVM mode)
- `src/native-test/java/` — `@QuarkusIntegrationTest` tests (run against packaged/native app)
