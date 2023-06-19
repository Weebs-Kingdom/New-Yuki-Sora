package de.mindcollaps.yuki.application.ai.api.objs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Event {
    private String id;
    private Date createdAt;
    private String type;
    private HashMap<String, Object> payload;

    public Event() {
    }

    public Event(String id, Date createdAt, String type, HashMap<String, Object> payload) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(HashMap<String, Object> payload) {
        this.payload = payload;
    }
}
