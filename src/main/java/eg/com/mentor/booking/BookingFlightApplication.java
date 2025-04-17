package eg.com.mentor.booking;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import eg.com.mentor.booking.model.Flight;
import eg.com.mentor.booking.repository.FlightRepository;

@SpringBootApplication
public class BookingFlightApplication implements CommandLineRunner {
	
	@Autowired
    private FlightRepository flightRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookingFlightApplication.class, args);
	}

	@Override
    public void run(String... args) {
        // Initialize test data
        if (flightRepository.count() == 0) {
            Flight flight = new Flight();
            flight.setNumber("FL123");
            flight.setDepartureTime(LocalDateTime.now().plusDays(1));
            flight.setCapacity(2); // Only 2 seats available
            flightRepository.save(flight);
        }
    }
}
