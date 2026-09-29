# AGENTS.md

Quarkus REST API (Gradle Kotlin DSL). Package root is `id.my.agungdh`. REST resources live in `id.my.agungdh.resource`. Config lives in `application.yml` (not `.properties`).

## Commands

Use the Gradle wrapper (`./gradlew`) — this is a Gradle project, not Maven.

- `./gradlew quarkusDev` — dev mode with live reload; Dev UI at http://localhost:8080/q/dev/
- `./gradlew build` — package; runnable jar at `build/quarkus-app/quarkus-run.jar`
- `./gradlew test` — run `@QuarkusTest` unit tests
- `./gradlew build -Dquarkus.native.enabled=true` — build native image (add `-Dquarkus.native.container-build=true` to build in a container without GraalVM)
- `./gradlew testNative` — run `@QuarkusIntegrationTest` native tests (see Gotchas)

### Makefile

Self-documenting help targets. Default `make` shows available commands.

- `make start` — start dev server
- `make reset-db` — drop + recreate postgres volume
- `make fresh-start` — reset DB then start dev server

## Gotchas

- **Java 25 required**: `build.gradle.kts` sets `sourceCompatibility = JavaVersion.VERSION_25`.
- **REST stack is `quarkus-rest` (RESTEasy Reactive).** It is incompatible with `quarkus-resteasy` and anything that depends on it — do not add them together.
- **Quarkus/Gradle/Lombok versions live in `gradle.properties`** (`quarkusPluginVersion`, `quarkusPlatformVersion`, `mapstructVersion`, `lombokVersion`), not in `build.gradle.kts`; the build script reads them via `by project`.
- **Lombok is enabled** (`@Getter`, `@Setter`, etc.). It works together with MapStruct via the `lombok-mapstruct-binding` annotation processor (already configured in `build.gradle.kts`). Keep Lombok on the `compileOnly` + `annotationProcessor` classpaths, not `implementation`.
- **Native integration tests are not run by `./gradlew test`.** `src/native-test/` holds `@QuarkusIntegrationTest` (`GreetingResourceIT` extends the unit test) and runs via `./gradlew testNative`, which requires a native image built first. There is no `src/integrationTest/` source set.
- **Config is `application.yml`** (YAML), not `application.properties`. Keep it in `src/main/resources/`. YAML requires the `quarkus-config-yaml` extension (already added to `build.gradle.kts`).
- **Virtual threads: annotate REST endpoints with `@RunOnVirtualThread`** (`io.smallrye.common.annotation.RunOnVirtualThread`). Quarkus has no global "run everything on virtual threads" switch. For CPU-heavy endpoints (future), omit the annotation so they run on worker threads instead.
- **Database: PostgreSQL (`simonjp`) with Flyway.** Datasource + Flyway configured in `application.yml`. Migrations in `src/main/resources/db/migration/`. Hibernate is set to `generation: none` — schema is managed by Flyway only. DB name is `simonjp` (matches `docker-compose.yml` and `application.yml`).
- **Base entity pattern**: `BaseEntity` is a `@MappedSuperclass` providing `id` (BIGINT GENERATED ALWAYS AS IDENTITY), `uuid` (UUID), `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `deletedAt`, `deletedBy`. Extend `BaseEntity` for new entities — do not redeclare these fields.
- **Entity fields are `public`** (Panache style, no getters/setters in entity usage).
- **`@PrePersist` / `@PreUpdate`** lifecycle callbacks handle `createdAt`/`updatedAt` timestamps automatically.
- **UUID is the public identifier.** Expose `uuid` to FE (not `id`). REST paths use `/{uuid}`. Repository provides `findByUuid(UUID)`.
- **Soft delete with `@SQLRestriction`.** `deletedAt` column — set on delete, ORM auto-excludes from reads via `@SQLRestriction("deleted_at IS NULL")` on `BaseEntity`. Do not physically delete rows. Do NOT add manual `WHERE deletedAt IS NULL` to queries — it's automatic.
- **Audit columns are nullable.** `createdBy`/`updatedBy`/`deletedBy` are nullable Long (references pegawai `id`) — all audit columns in `BaseEntity` are nullable.
- **Audit columns are DB/entity-only.** They must NOT appear in request or response DTOs.
- **Partial indexes for unique constraints.** When a unique column must be unique among non-deleted rows, use `CREATE UNIQUE INDEX ... WHERE deleted_at IS NULL` instead of `UNIQUE` constraint.
- **FK to primary key when partial index is needed.** PostgreSQL FK constraints require a full unique index as reference target. If the referenced column uses a partial unique index (e.g. `WHERE deleted_at IS NULL`), FK must reference the primary key (`id`) instead of that column. E.g. `users.pegawai_id → pegawais(id)` rather than `users.username → pegawais(nip)`.
- **FK columns must always have indexes.** When a FK is created or changed, ensure an index exists on the FK column.
- `build/` and `target/` are gitignored build/dev artifacts.

## Layout

- `src/main/java/id/my/agungdh/resource/` — JAX-RS resources (annotated `@RunOnVirtualThread`)
- `src/main/java/id/my/agungdh/entity/` — domain entities extending `BaseEntity` (Lombok-annotated, Panache, `public` fields)
- `src/main/java/id/my/agungdh/dto/` — request/response records (Java Records, business fields only — no audit columns)
- `src/main/java/id/my/agungdh/mapper/` — MapStruct mappers (`componentModel = "cdi"`)
- `src/main/java/id/my/agungdh/repository/` — data access (Panache, `PanacheRepositoryBase`)
- `src/main/java/id/my/agungdh/service/` — business logic (CDI beans, `@Transactional` on write methods only)
- `src/main/java/id/my/agungdh/validation/` — custom validation annotations and validators (e.g. `@Unique`)
- `src/main/java/id/my/agungdh/exception/` — exception mappers (e.g. `ConstraintViolationExceptionMapper`)
- `src/main/resources/db/migration/` — Flyway SQL migrations (sequential: V1, V2, ...)
- `src/test/java/` — `@QuarkusTest` unit tests (run in JVM mode)
- `src/native-test/java/` — `@QuarkusIntegrationTest` tests (run against packaged/native app)

## Domain Model

- **Pegawai** (exposed via REST at `/pegawai`): `nip`, `nama`, `jabatan`. Partial unique index on `nip WHERE deleted_at IS NULL`.
- **User** (internal, no REST endpoints): `pegawaiId` (FK → pegawais.id), `username`, `password`. Managed entirely through PegawaiService — create/update/delete pegawai automatically manages the associated user.
- User is an internal entity: only has JPA entity + Flyway migration. No DTO, mapper, service, or resource of its own.
- Sensitive fields (e.g. `password`) must never appear in response DTOs.

## Architecture Patterns

- **Layered architecture**: entity → DTO (records) → mapper → repository → service → resource. Resource only handles HTTP concerns.
- **Service methods accept the full request DTO** as a parameter, not individual fields.
- **1:1 related entities**: primary entity's CRUD manages the secondary entity's lifecycle automatically in a single transaction. No separate API for the secondary entity.
- **Validation**: custom annotations in `validation` package, exception mappers in `exception` package. Validation errors return 422 with `Map<String, List<String>>` errors (not logged).
- **FK referential actions**: always explicit `ON UPDATE RESTRICT` and `ON DELETE RESTRICT` unless specified otherwise.
