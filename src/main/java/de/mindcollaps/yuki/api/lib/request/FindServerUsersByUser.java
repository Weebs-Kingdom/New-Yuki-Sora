package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.ServerUser;
import de.mindcollaps.yuki.api.lib.route.DatabaseId;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findServerUsersByUser")
public class FindServerUsersByUser extends RouteRequest<ServerUser> {

    @RouteField
    private String user;

    public FindServerUsersByUser(DatabaseId userId) {
        super(ServerUser.class);
        this.user = userId.getDatabaseId();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String id) {
        this.user = id;
    }
}
