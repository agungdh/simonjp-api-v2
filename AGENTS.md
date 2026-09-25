# AGENTS.md

Quarkus REST API (Gradle Kotlin DSL). Package root is `id.my.agungdh`. REST resources: `GreetingResource` (`GET /hello`) and `ProductResource` (`/products` CRUD). Config lives in `application.yml` (not `.properties`).

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
- **Quarkus/Gradle versions live in `gradle.properties`** (`quarkusPluginVersion`, `quarkusPlatformVersion`, `mapstructVersion`), not in `build.gradle.kts`; the build script reads them via `by project`.
- **Native integration tests are not run by `./gradlew test`.** `src/native-test/` holds `@QuarkusIntegrationTest` (`GreetingResourceIT` extends the unit test) and runs via `./gradlew testNative`, which requires a native image built first. There is no `src/integrationTest/` source set.
- **Config is `application.yml`** (YAML), not `application.properties`. Keep it in `src/main/resources/`. YAML requires the `quarkus-config-yaml` extension (already added to `build.gradle.kts`).
- **Virtual threads: annotate REST endpoints with `@RunOnVirtualThread`** (`io.smallrye.common.annotation.RunOnVirtualThread`). Quarkus has no global "run everything on virtual threads" switch. For CPU-heavy endpoints (future), omit the annotation so they run on worker threads instead.
- `build/` and `target/` are gitignored build/dev artifacts.

## Layout

- `src/main/java/` — application code
- `src/test/java/` — `@QuarkusTest` unit tests (run in JVM mode)
- `src/native-test/java/` — `@QuarkusIntegrationTest` tests (run against packaged/native app)
- `src/main/docker/` — Dockerfiles for jvm / native / native-micro / legacy-jar builds
