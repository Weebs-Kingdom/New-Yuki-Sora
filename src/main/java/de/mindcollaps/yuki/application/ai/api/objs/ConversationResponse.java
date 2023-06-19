package de.mindcollaps.yuki.application.ai.api.objs;

import de.mindcollaps.yuki.api.lib.route.RouteField;

public class ConversationResponse {

    @RouteField
    private Conversation conversation;

    public ConversationResponse() {
    }

    public ConversationResponse(Conversation conversation) {
        this.conversation = conversation;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}
