package de.mindcollaps.yuki.application.ai.api.objs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Message {
    private String id;
    private Date createdAt;
    private String type;
    private HashMap<String, Object> payload;
    private String direction;
    private String userId;
    private String conversationId;
    private HashMap<String, String> tags;

    public Message() {
    }

    public Message(String id, Date createdAt, String type, HashMap<String, Object> payload, String direction, String userId, String conversationId, HashMap<String, String> tags) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
        this.payload = payload;
        this.direction = direction;
        this.userId = userId;
        this.conversationId = conversationId;
        this.tags = tags;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }
}

