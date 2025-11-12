package com.example.ticketing.event;

public class EventDto {
    private Long id;
    private String name;
    private int availableTickets;

    public EventDto(Long id, String name, int availableTickets) {
        this.id = id;
        this.name = name;
        this.availableTickets = availableTickets;
    }

    public static EventDto from(Event e) {
        return new EventDto(e.getId(), e.getName(), e.getAvailableTickets());
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
}
