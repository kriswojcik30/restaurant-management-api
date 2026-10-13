# Restaurant Management API (Spring Boot)

REST API for managing a restaurant: tables, waiters, menu, orders and bills. Built as a university project at AGH University of Krakow (Computational Engineering), starting from a real-style client brief: a restaurant owner who ran everything on paper and needed a simple system for a full dining room.

## What the system does

- Shows which tables are free
- Opens an order at a table and assigns a waiter (the table becomes occupied)
- Adds dishes to an order, copying the price at the time of ordering
- Calculates the bill
- Moves the order through its workflow: open → in progress → served → paid (or cancelled); paying frees the table
- Exports the day's orders to a CSV file for Excel

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/menu` | All dishes |
| GET | `/api/menu/{id}` | Dish by id (404 if not found) |
| GET | `/api/menu/available` | Available dishes |
| GET | `/api/menu/category/{category}` | Dishes by category (STARTER, MAIN, DESSERT, DRINK) |
| POST | `/api/menu` | Add a dish (201) |
| DELETE | `/api/menu/{id}` | Delete a dish (204) |
| GET | `/api/tables` | All tables |
| GET | `/api/tables/free` | Free tables |
| POST | `/api/tables` | Add a table (201) |
| PATCH | `/api/tables/{id}/status?status=...` | Change table status |
| GET | `/api/waiters` | All waiters |
| GET | `/api/waiters/{id}` | Waiter by id |
| POST | `/api/waiters` | Add a waiter (201) |
| DELETE | `/api/waiters/{id}` | Delete a waiter (204) |
| POST | `/api/orders` | Open an order: `{ "tableId": 1, "waiterId": 1 }` (201) |
| GET | `/api/orders/{id}` | Order details |
| POST | `/api/orders/{id}/items` | Add a dish: `{ "menuItemId": 1, "quantity": 2 }` |
| GET | `/api/orders/{id}/total` | Bill total |
| PATCH | `/api/orders/{id}/status?status=...` | Change order status (only allowed transitions) |
| GET | `/api/orders/export/csv` | Download orders as `orders.csv` |

## Error handling

All errors return a consistent JSON body from a global exception handler (`@ControllerAdvice`):

    {
      "status": 404,
      "message": "Nie znaleziono dania o id: 9999",
      "timestamp": "2026-01-15T14:30:00"
    }

- **400 Bad Request:** invalid input, e.g. empty name or negative price (`@Valid`, `@NotBlank`, `@Positive`)
- **404 Not Found:** resource does not exist
- **409 Conflict:** operation not allowed in the current state, e.g. reopening a paid order

## Problems found and fixed

Part of the project consisted of reviewing AI-generated code with deliberate bugs, finding them by running the application and fixing them:

- **Returning entities from controllers:** replaced with DTOs to avoid serialization errors and exposing internal fields
- **Wrong status codes:** missing resources returned 500 instead of 404, deletes returned 200 instead of 204
- **Business logic bugs:** bill ignored quantity, status changes were not saved, `Long` values compared with `==`, paying an order did not free the table
- **N+1 queries:** listing orders ran 41 SQL queries for 20 orders; fixed with `JOIN FETCH`
- **Invalid status transitions:** added validation so that, for example, a paid order cannot be reopened
- **Weak tests:** rewrote integration tests that passed without checking real scenarios

## Tests

Integration tests with `@SpringBootTest` and MockMvc cover:

- creating a valid dish → 201 Created
- creating a dish with invalid data → 400 Bad Request
- getting a dish that does not exist → 404 Not Found
- deleting a dish → 204 No Content
- deleting a dish that does not exist → 404 Not Found
- filtering by category → 200 OK with the correct list
- CSV export → `Content-Type: text/csv`

## Tech stack

Java · Spring Boot · Spring Web · Spring Data JPA · Hibernate · Bean Validation · H2 (in-memory) · JUnit 5 · MockMvc · Maven · Postman

## Architecture

Controller → Service → Repository. Controllers handle only HTTP, services hold the business logic, repositories access the database through Spring Data JPA.

## How to run

Requirements: JDK 26. Maven is included through the Maven Wrapper.

    ./mvnw spring-boot:run

The API starts at `http://localhost:8080`. The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`). The database is in memory and resets on every start.

Run the tests:

    ./mvnw test

### Example scenario

    curl -X POST localhost:8080/api/menu -H "Content-Type: application/json" -d '{"name":"Margherita","category":"MAIN","price":32.00,"available":true}'
    curl -X POST localhost:8080/api/tables -H "Content-Type: application/json" -d '{"tableNumber":1,"capacity":4,"status":"FREE","location":"Sala główna"}'
    curl -X POST localhost:8080/api/waiters -H "Content-Type: application/json" -d '{"firstName":"Anna","lastName":"Kowalska","hireDate":"2023-01-15","active":true}'
    curl -X POST localhost:8080/api/orders -H "Content-Type: application/json" -d '{"tableId":1,"waiterId":1}'
    curl -X POST localhost:8080/api/orders/1/items -H "Content-Type: application/json" -d '{"menuItemId":1,"quantity":2}'
    curl localhost:8080/api/orders/1/total
    curl -X PATCH "localhost:8080/api/orders/1/status?status=PAID"

Note: error messages are in Polish.

## What I learned

- Turning a non-technical client brief into a data model and an API
- Designing REST endpoints with correct HTTP methods and status codes
- Keeping layers separate and putting business rules in the service layer
- Writing integration tests that check real scenarios, including errors
- Reviewing AI-generated code critically: code that compiles is not necessarily correct
