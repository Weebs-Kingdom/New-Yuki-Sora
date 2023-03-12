package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.route.RouteRequest;
import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;

@RouteClass("findAutoChannelsByGuildId")
public class FindAutoChannelsByGuildId extends RouteRequest<AutoChannel> {

    @RouteField
    private String serverId;

    public FindAutoChannelsByGuildId(String guildId) {
        super(AutoChannel.class);
        this.serverId = guildId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String id) {
        this.serverId = id;
    }
}
