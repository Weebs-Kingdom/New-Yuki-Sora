package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.route.RouteRequest;
import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;

@RouteClass("findAutoChannelByIds")
public class FindAutoChannelsByIds extends RouteRequest<AutoChannel> {

    @RouteField
    private String serverId;
    @RouteField
    private String channelId;

    public FindAutoChannelsByIds(String guildId, String channelId) {
        super(AutoChannel.class);
        this.serverId = guildId;
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
