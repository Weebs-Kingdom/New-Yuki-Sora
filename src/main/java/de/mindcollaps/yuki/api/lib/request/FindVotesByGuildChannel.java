package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.Vote;
import de.mindcollaps.yuki.api.lib.route.DatabaseId;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findVotesByChannel")
public class FindVotesByGuildChannel extends RouteRequest<Vote> {

    @RouteField
    private String serverId;

    @RouteField
    private String channelId;

    public FindVotesByGuildChannel(DatabaseId serverId, String channelId) {
        super(Vote.class);
        this.serverId = serverId.getDatabaseId();
        this.channelId = channelId;
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
}
