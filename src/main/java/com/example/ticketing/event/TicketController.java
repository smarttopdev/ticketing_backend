package com.example.ticketing.event;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@CrossOrigin // you can remove if using proxy from Angular
public class TicketController {

    private final TicketService ticketService;
    private final EventRepository eventRepository;

    public TicketController(TicketService ticketService,
                            EventRepository eventRepository) {
        this.ticketService = ticketService;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<EventDto> getAll() {
        return eventRepository.findAll().stream()
                .map(EventDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public EventDto getById(@PathVariable Long id) {
        return EventDto.from(ticketService.getEvent(id));
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<EventDto> book(@PathVariable Long id,
                                         @RequestParam(name = "count") int count) {
        ticketService.bookTickets(id, count);
        // return updated event
        return ResponseEntity.ok(EventDto.from(ticketService.getEvent(id)));
    }
}
