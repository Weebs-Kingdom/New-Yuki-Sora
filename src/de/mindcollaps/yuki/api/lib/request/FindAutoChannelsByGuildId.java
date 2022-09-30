package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findAutoChannelsByGuildId")
public class FindAutoChannelsByGuildId extends RouteRequest<AutoChannel> {

    @RouteField
    private String guildId;

    public FindAutoChannelsByGuildId(String guildId) {
        super(AutoChannel.class);
        this.guildId = guildId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String id) {
        this.guildId = id;
    }
}
