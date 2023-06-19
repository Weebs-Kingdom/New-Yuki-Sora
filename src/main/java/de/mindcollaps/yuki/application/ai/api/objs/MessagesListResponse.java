package de.mindcollaps.yuki.application.ai.api.objs;

import java.util.List;

public class MessagesListResponse {
    private List<Message> messages;
    private String nextToken;

public MessagesListResponse() {
    }

    public MessagesListResponse(List<Message> messages, String nextToken) {
        this.messages = messages;
        this.nextToken = nextToken;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
