package eg.com.mentor.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import eg.com.mentor.booking.model.Flight;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="10000")})
    Optional<Flight> findWithPessimisticLockById(Long id);
    
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Flight> findWithOptimisticLockById(Long id);
}
