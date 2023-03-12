package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.route.RouteRequest;
import de.mindcollaps.yuki.api.lib.data.UserTwitchConnection;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;

@RouteClass("findTwitchUserConByUser")
public class FindTwitchUserConByUser extends RouteRequest<UserTwitchConnection> {

    @RouteField
    private String user;

    public FindTwitchUserConByUser(String user) {
        super(UserTwitchConnection.class);
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
