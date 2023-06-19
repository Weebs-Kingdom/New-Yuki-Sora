package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.ServerUser;
import de.mindcollaps.yuki.api.lib.route.DatabaseId;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findServerUserByUserAServer")
public class FindServerUserByUserAndServer extends RouteRequest<ServerUser> {

    @RouteField
    private String userid;

    @RouteField
    private String server;

    public FindServerUserByUserAndServer(DatabaseId userId, DatabaseId serverId) {
        super(ServerUser.class);
        this.userid = userId.getDatabaseId();
        this.server = serverId.getDatabaseId();
    }

    public String getUserId() {
        return userid;
    }

    public void setUserId(String id) {
        this.userid = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String serverId) {
        this.server = serverId;
    }
}
