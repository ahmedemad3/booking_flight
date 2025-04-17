package eg.com.mentor.booking.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eg.com.mentor.booking.dto.TicketRequest;
import eg.com.mentor.booking.service.NoLockService;
import eg.com.mentor.booking.service.OptimisticLockService;
import eg.com.mentor.booking.service.PessimisticLockService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final NoLockService noLockService;
    private final PessimisticLockService pessimisticLockService;
    private final OptimisticLockService optimisticLockService;
    
    @PostMapping("/no-lock/{flightId}")
    public String bookWithoutLock(@PathVariable Long flightId, 
                                @RequestBody TicketRequest request) {
        return noLockService.bookTicket(flightId, request.getFirstName(), request.getLastName());
    }
    
    @PostMapping("/pessimistic/{flightId}")
    public String bookWithPessimisticLock(@PathVariable Long flightId, 
                                        @RequestBody TicketRequest request) {
        return pessimisticLockService.bookTicket(flightId, request.getFirstName(), request.getLastName());
    }
    
    @PostMapping("/optimistic/{flightId}")
    public String bookWithOptimisticLock(@PathVariable Long flightId, 
                                       @RequestBody TicketRequest request) {
        return optimisticLockService.bookTicket(flightId, request.getFirstName(), request.getLastName());
    }
}

