package eg.com.mentor.booking.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "flights")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Flight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String number;
	private LocalDateTime departureTime;
	private Integer capacity;

	@Version
	private Long version;

	@OneToMany(mappedBy = "flight")
	private Set<Ticket> tickets = new HashSet<>();

	// Constructors, getters, setters
	public void addTicket(Ticket ticket) {
		ticket.setFlight(this);
		getTickets().add(ticket);
	}
}
