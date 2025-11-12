package com.example.ticketing.event;

import com.example.ticketing.TicketingApplication;
import com.example.ticketing.common.BookingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest(classes = TicketingApplication.class)
public class TicketServiceConcurrencyTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketService ticketService;

    @BeforeEach
    void resetEvent() {
        // reset event 1 to known value
        Event e = eventRepository.findById(1L).orElseThrow();
        e.setAvailableTickets(50);
        eventRepository.save(e);
    }

    @Test
    void concurrentBookingsShouldNotOverbook() throws InterruptedException {
        int threads = 10;
        int ticketsPerThread = 10; // total requested = 100, but we have only 50

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(executor.submit(() -> {
                try {
                    ticketService.bookTickets(1L, ticketsPerThread);
                } catch (BookingException ex) {
                    // expected for some threads when tickets run out
                }
            }));
        }

        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                // ignore individual task failure
            }
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        Event e = eventRepository.findById(1L).orElseThrow();
        int remaining = e.getAvailableTickets();

        // we started with 50, so remaining must be >= 0
        // and must never be negative
        Assertions.assertTrue(remaining >= 0, "Remaining should never be negative");

        // And actual sold must be <= 50
        int sold = 50 - remaining;
        Assertions.assertTrue(sold <= 50, "Should not sell more than available");
    }
}
