package com.example.ticketing.event;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * how many tickets are still available
     */
    private int availableTickets;

    @Version
    private Long version;

    public Event() {
    }

    public Event(String name, int availableTickets) {
        this.name = name;
        this.availableTickets = availableTickets;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }
}
