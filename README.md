# Ticketing Service

Spring Boot backend that manages ticket inventory for events, supports safe concurrent bookings, and exposes simple REST endpoints.

## Requirements

- Java 17+
- Maven (wrapper included)
- In-memory H2 database (auto-configured)

## Getting Started

```bash
.\mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. H2 console is available at `/h2-console` (user: `sa`, no password).

## API

- `GET /tickets` — list all events with remaining tickets.
- `GET /tickets/{id}` — fetch a single event.
- `POST /tickets/{id}/book?count=N` — book `N` tickets for the event. Returns updated inventory or `400 Bad Request` if not enough stock or invalid count.

Responses are JSON via `EventDto`.

## Concurrency & Error Handling

- Uses optimistic locking on the `Event` entity to prevent overbooking.
- Retries bookings briefly to mitigate lock contention.
- Global exception handler returns structured error responses for not found, booking issues, and unexpected errors.

## Testing

Run the suite:

```bash
.\mvnw test
```

Key test coverage:

- REST endpoint behavior.
- Booking flow updating inventory.
- Error handling for invalid requests.
- Concurrent booking attempts to ensure inventory is never oversold.

## Database Bootstrapping

`data.sql` seeds sample events at startup. Inventory resets on each application run because H2 is in-memory.

## Project Layout

- `src/main/java/com/example/ticketing` — main application code.
- `src/test/java/com/example/ticketing` — JUnit tests.
- `application.properties` — H2 and JPA configuration.
- `TicketServiceConcurrencyTest` — integration tests covering REST and concurrency requirements.

