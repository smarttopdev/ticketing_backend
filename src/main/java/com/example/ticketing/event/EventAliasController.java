package com.example.ticketing.event;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventAliasController {

    private final TicketService ticketService;
    private final EventRepository eventRepository;

    public EventAliasController(TicketService ticketService, EventRepository eventRepository) {
        this.ticketService = ticketService;
        this.eventRepository = eventRepository;
    }
    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventDto::from)
                .toList();
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
