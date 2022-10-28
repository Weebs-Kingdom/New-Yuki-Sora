package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.Vote;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findVoteByMessage")
public class FindVoteByMessage extends RouteRequest<Vote> {

    @RouteField
    private String serverId;

    @RouteField
    private String channelId;

    @RouteField
    private String messageId;

    public FindVoteByMessage(String serverId, String channelId, String messageId) {
        super(Vote.class);
        this.serverId = serverId;
        this.channelId = channelId;
        this.messageId = messageId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
