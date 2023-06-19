package de.mindcollaps.yuki.application.ai.api.objs;

public class EventResponse {
    private Event event;

    public EventResponse() {
    }

    public EventResponse(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
