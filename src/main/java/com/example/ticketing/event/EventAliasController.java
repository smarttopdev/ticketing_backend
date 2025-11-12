package com.example.ticketing.event;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventAliasController {

    private final TicketService ticketService;

    public EventAliasController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return EventDto.from(ticketService.getEvent(id));
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<EventDto> book(@PathVariable Long id,
                                         @RequestParam("count") int count) {
        ticketService.bookTickets(id, count);
        return ResponseEntity.ok(EventDto.from(ticketService.getEvent(id)));
    }
}
