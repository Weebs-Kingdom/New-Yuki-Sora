package de.mindcollaps.yuki.application.discord.response;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.Date;

@SuppressWarnings("EmptyMethod")
public abstract class Response {
    public final Date creationTime = new Date();
    public String discGuildId;
    public String discChannelId;
    public String discUserId;
    public String discMessageId;

    public void onEmote(MessageReactionAddEvent respondingEvent) {
    }

    public void onMessage(MessageReceivedEvent respondingEvent) {
    }

    public void onError(Exception e) {
    }
}
