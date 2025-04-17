package eg.com.mentor.booking.service;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eg.com.mentor.booking.model.Flight;
import eg.com.mentor.booking.model.Ticket;
import eg.com.mentor.booking.repository.FlightRepository;
import eg.com.mentor.booking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptimisticLockService {
	private final FlightRepository flightRepository;
	private final TicketRepository ticketRepository;

	@Transactional
	public String bookTicket(Long flightId, String firstName, String lastName) {
		try {
			Flight flight = getFlight(flightId);
			Integer ticketCount = countNoOfTicketsPerFlight(flight);
	    	System.out.println("Tickets count: " + ticketCount);
	        if (ticketCount >= flight.getCapacity()) {
	            throw new RuntimeException("Flight capacity exceeded");
	        }

			Ticket ticket = Ticket.buildTicket(firstName, lastName, flight);
			flight.addTicket(ticket);

			ticketRepository.save(ticket);
			return "Ticket booked successfully with optimistic lock";
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new RuntimeException("Another transaction modified this flight. Please try again.");
		}
	}
	
	private Integer countNoOfTicketsPerFlight(Flight flight) {
		return ticketRepository.countTicketsByFlight(flight);
	}

	private Flight getFlight(Long flightId) {
		Flight flight = flightRepository.findWithOptimisticLockById(flightId).orElseThrow(() -> new RuntimeException("Flight not found"));
		return flight;
	}
}
