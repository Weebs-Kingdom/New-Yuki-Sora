package de.mindcollaps.yuki.application.ai.api.objs;

import de.mindcollaps.yuki.api.lib.route.RouteField;

import java.util.List;

public class ConversationsListResponse {

    @RouteField
    private List<Conversation> conversations;

    @RouteField
    private String nextToken;

    public ConversationsListResponse() {
    }

    public ConversationsListResponse(List<Conversation> conversations, String nextToken) {
        this.conversations = conversations;
        this.nextToken = nextToken;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
