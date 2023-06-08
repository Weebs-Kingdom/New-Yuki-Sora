package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.UserTwitchConnection;
import de.mindcollaps.yuki.api.lib.route.DatabaseId;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findTwitchUsersByServer")
public class FindTwitchUserByServer extends RouteRequest<UserTwitchConnection> {

    @RouteField
    private String id;

    public FindTwitchUserByServer(DatabaseId serverId) {
        super(UserTwitchConnection.class);
        this.id = serverId.getDatabaseId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
