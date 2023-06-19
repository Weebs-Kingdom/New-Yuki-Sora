package de.mindcollaps.yuki.application.ai.api.objs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class User {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private HashMap<String, Object> tags;
    private String integrationName;

    public User() {}

    public User(String id, Date createdAt, Date updatedAt, HashMap<String, Object> tags, String integrationName) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tags = tags;
        this.integrationName = integrationName;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public HashMap<String, Object> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, Object> tags) {
        this.tags = tags;
    }

    public String getIntegrationName() {
        return integrationName;
    }

    public void setIntegrationName(String integrationName) {
        this.integrationName = integrationName;
    }
}

