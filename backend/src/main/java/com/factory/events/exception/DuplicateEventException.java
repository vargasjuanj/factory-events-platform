package com.factory.events.exception;

public class DuplicateEventException extends RuntimeException {

    private final String eventId;

    public DuplicateEventException(String eventId) {
        super("El evento '" + eventId + "' ya fue registrado y no se admiten duplicados.");
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
