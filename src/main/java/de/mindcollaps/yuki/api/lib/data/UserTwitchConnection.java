package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.*;

@RouteClass("user-twitch-con")
public class UserTwitchConnection extends RouteData {

    @RouteField
    @ForeignData(DiscApplicationUser.class)
    private DatabaseId user = new DatabaseId();

    @RouteField
    private String twitchChannelId = "";

    @RouteField
    private String[] servers = new String[0];

    public UserTwitchConnection() {
    }

    public DatabaseId getUser() {
        return user;
    }

    public void setUser(DatabaseId user) {
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
