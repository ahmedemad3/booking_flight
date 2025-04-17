package eg.com.mentor.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eg.com.mentor.booking.model.Flight;
import eg.com.mentor.booking.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	Integer countTicketsByFlight(Flight flight);
}
