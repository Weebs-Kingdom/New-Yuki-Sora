package de.mindcollaps.yuki.application.ai.api.objs;

import java.util.List;

public class EventsListResponse {
    private List<Event> events;
    private String nextToken;

    public EventsListResponse() {
    }

    public EventsListResponse(List<Event> events, String nextToken) {
        this.events = events;
        this.nextToken = nextToken;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
