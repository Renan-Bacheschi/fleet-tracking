# Fleet Tracking

Fleet Tracking API with vehicle registration and management.

**Status:** M1 — Vehicle Registration

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

## Vehicle API

All vehicle endpoints use the `/api/v1/vehicles` prefix.

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/v1/vehicles` | Register a vehicle |
| `GET` | `/api/v1/vehicles` | List registered vehicles |
| `GET` | `/api/v1/vehicles/{id}` | Retrieve a vehicle by UUID |
| `PUT` | `/api/v1/vehicles/{id}` | Replace a vehicle's registration data |
| `PATCH` | `/api/v1/vehicles/{id}/status` | Change a vehicle's operational status |

Create a vehicle:

```bash
curl -i -X POST http://localhost:8080/api/v1/vehicles \
  -H 'Content-Type: application/json' \
  -d '{
    "licensePlate": "ABC-1D23",
    "fleetCode": "FLEET-001",
    "brand": "Volkswagen",
    "model": "Delivery",
    "modelYear": 2024,
    "type": "TRUCK"
  }'
```

The API returns HTTP 201 and normalizes the plate:

```json
{
  "id": "7db0ae01-ecb8-41a6-a3a0-8266953e55bc",
  "licensePlate": "ABC1D23",
  "fleetCode": "FLEET-001",
  "brand": "Volkswagen",
  "model": "Delivery",
  "modelYear": 2024,
  "type": "TRUCK",
  "status": "ACTIVE",
  "createdAt": "2026-08-23T18:00:00Z",
  "updatedAt": "2026-08-23T18:00:00Z"
}
```

Change its status without deleting it:

```bash
curl -X PATCH http://localhost:8080/api/v1/vehicles/7db0ae01-ecb8-41a6-a3a0-8266953e55bc/status \
  -H 'Content-Type: application/json' \
  -d '{"status":"INACTIVE"}'
```

Validation failures use RFC 9457 problem details and return HTTP 400. Missing vehicles return HTTP 404, while duplicate license plates or fleet codes return HTTP 409.

See [docs/data-model.md](docs/data-model.md) for the current data model.
