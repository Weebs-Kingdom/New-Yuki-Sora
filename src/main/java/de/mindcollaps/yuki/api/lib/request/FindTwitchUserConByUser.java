package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.UserTwitchConnection;
import de.mindcollaps.yuki.api.lib.route.DatabaseId;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findTwitchUserConByUser")
public class FindTwitchUserConByUser extends RouteRequest<UserTwitchConnection> {

    @RouteField
    private String user;

    public FindTwitchUserConByUser(DatabaseId user) {
        super(UserTwitchConnection.class);
        this.user = user.getDatabaseId();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
