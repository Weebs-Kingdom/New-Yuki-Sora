package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.ForeignData;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteData;
import de.mindcollaps.yuki.api.lib.route.RouteField;

@RouteClass("user-twitch-con")
public class UserTwitchConnection extends RouteData {

    @RouteField
    @ForeignData(DiscApplicationUser.class)
    private String user = "";

    @RouteField
    private String twitchChannelId = "";

    @RouteField
    private String[] servers = new String[0];

    public UserTwitchConnection() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTwitchChannelId() {
        return twitchChannelId;
    }

    public void setTwitchChannelId(String twitchChannelId) {
        this.twitchChannelId = twitchChannelId;
    }

    public String[] getServers() {
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }
}
