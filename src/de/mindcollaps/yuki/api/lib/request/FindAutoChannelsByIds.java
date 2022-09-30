package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findAutoChannelByIds")
public class FindAutoChannelsByIds extends RouteRequest<AutoChannel> {

    @RouteField
    private String guildId;
    @RouteField
    private String channelId;

    public FindAutoChannelsByIds(String guildId, String channelId) {
        super(AutoChannel.class);
        this.guildId = guildId;
        this.channelId = channelId;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
