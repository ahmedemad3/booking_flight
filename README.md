# Flight Booking Concurrency Demo (Spring Boot + PostgreSQL)

This project demonstrates the effects of different data locking strategies in a flight booking system using Spring Boot, JPA, and PostgreSQL. It shows how to handle concurrent seat bookings with **no locking**, **pessimistic locking**, and **optimistic locking**. The project is designed for learning and testing how concurrency issues can be managed in real-world applications.

## Features
- Book flight tickets concurrently using REST API endpoints
- Three booking strategies:
  - **No Locking** (prone to overbooking)
  - **Pessimistic Locking** (locks DB row, prevents overbooking)
  - **Optimistic Locking** (uses versioning, prevents overbooking with retry)
- Demonstrates race conditions and solutions
- Easily testable with Postman, JMeter, or custom scripts

## Technologies Used
- Java 17+
- Spring Boot 3+
- Spring Data JPA (Hibernate)
- PostgreSQL
- Lombok

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd <project-directory>
```

### 2. Database Setup
- Create a PostgreSQL database named `flight_booking`
- Example (psql):
  ```sql
  CREATE DATABASE flight_booking;
  ```
- Update `src/main/resources/application.properties` with your DB credentials:
  ```properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/flight_booking
  spring.datasource.username=postgres
  spring.datasource.password=yourpassword
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  ```

### 3. Build and Run
```bash
./mvnw spring-boot:run
```
The app will start on [http://localhost:8080](http://localhost:8080)

### 4. Initial Data
On first run, a flight with limited capacity will be created automatically. You can add more flights using SQL or by extending the app.

## API Endpoints

### Book a Ticket (No Locking)
```
POST /api/v1/booking/no-lock/{flightId}
```
Body:
```json
{
  "firstName": "John",
  "lastName": "Doe"
}
```

### Book a Ticket (Pessimistic Locking)
```
POST /api/v1/booking/pessimistic/{flightId}
```
Body: same as above

### Book a Ticket (Optimistic Locking)
```
POST /api/v1/booking/optimistic/{flightId}
```
Body: same as above

## How to Test Concurrency

- Use Postman Runner, JMeter, or a shell script to fire multiple requests simultaneously to the same endpoint.
- Example shell script:
  ```bash
  for i in {1..5}; do
    curl -X POST "http://localhost:8080/api/bookings/no-lock/1" \
      -H "Content-Type: application/json" \
      -d '{"firstName":"User'$i'","lastName":"Test"}' &
  done
  wait
  ```
- Check the number of tickets in the database to see if overbooking occurred.

## Project Structure
- `Flight` entity: Represents a flight with capacity
- `Ticket` entity: Represents a booking for a flight
- `FlightRepository`, `TicketRepository`: JPA repositories
- `BookingService`: Contains booking logic for each locking scenario
- `BookingController`: Exposes REST endpoints

## Locking Strategies Explained
- **No Locking**: No concurrency control. Multiple users can overbook flights.
- **Pessimistic Locking**: Locks the flight row in the database during booking. Prevents overbooking but can cause waiting.
- **Optimistic Locking**: Uses a version field. If two users try to book the last seat, one will succeed, the other will get an error and can retry.

## Troubleshooting
- If you see overbooking with no lock, that's expected.
- If you see `ObjectOptimisticLockingFailureException` with optimistic locking, this means a conflict occurred.
- Ensure your database isolation level is at least `READ COMMITTED`.
- For large numbers of tickets, consider batch fetching or pagination.

## License
This project is for educational and demonstration purposes.

---

Feel free to extend the system for more complex scenarios or integrate additional concurrency patterns!
