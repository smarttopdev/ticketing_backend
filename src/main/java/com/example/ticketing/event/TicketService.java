package com.example.ticketing.event;

import com.example.ticketing.common.BookingException;
import com.example.ticketing.common.NotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final EventRepository eventRepository;

    public TicketService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event " + id + " not found"));
    }

    /**
     * Try to book tickets with small retry for optimistic locking contention.
     */
    public void bookTickets(Long id, int count) {
        if (count <= 0) {
            throw new BookingException("count must be positive");
        }

        int maxRetries = 5;
        for (int i = 0; i < maxRetries; i++) {
            try {
                doBook(id, count);
                return;
            } catch (OptimisticLockException e) {
                if (i == maxRetries - 1) {
                    throw new BookingException("Could not complete booking due to concurrent updates. Try again.");
                }
            }
        }
    }

    @Transactional
    protected void doBook(Long id, int count) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event " + id + " not found"));

        int remaining = event.getAvailableTickets();
        if (remaining < count) {
            throw new BookingException("Not enough tickets. Remaining: " + remaining);
        }

        event.setAvailableTickets(remaining - count);
        eventRepository.save(event);
    }
}
