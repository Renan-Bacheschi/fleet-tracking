# Fleet Tracking

Local foundation for the Fleet Tracking API.

**Status:** M0 — Foundation

## Prerequisites

- Java 21
- Docker Desktop

## Run locally

Start PostgreSQL from the repository root:

```bash
docker compose -f infra/compose.yaml up -d
```

Start the API from `fleet-api`:

```bash
cd fleet-api
./mvnw spring-boot:run
```

Run the tests from `fleet-api`:

```bash
cd fleet-api
./mvnw test
```

The tests start their own PostgreSQL container through Testcontainers.

Check application health after the API starts:

```bash
curl http://localhost:8080/actuator/health
```

The response should return HTTP 200 and contain `"status":"UP"`.
