package de.mindcollaps.yuki.application.ai.api.objs;

import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.YukiRoute;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.application.ai.api.route.BPRoute;

import java.util.Date;
import java.util.HashMap;

@RouteClass("chat/conversations")
public class Conversation extends BPRoute {

    @RouteField
    private String id;

    @RouteField
    private Date createdAt;

    @RouteField
    private Date updatedAt;

    @RouteField
    private String channel;

    @RouteField
    private String integration;

    @RouteField
    private HashMap<String, Object> tags;

    public Conversation() {
    }

    public Conversation(String id, Date createdAt, Date updatedAt, String channel, String integration, HashMap<String, Object> tags) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.channel = channel;
        this.integration = integration;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public HashMap<String, Object> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, Object> tags) {
        this.tags = tags;
    }
}

