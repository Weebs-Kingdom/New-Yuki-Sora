package de.mindcollaps.yuki.application.ai.api.objs;

public class MessageResponse {
    private Message message;

    public MessageResponse() {
    }

    public MessageResponse(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
