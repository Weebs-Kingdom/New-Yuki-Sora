package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.UserTwitchConnection;
import de.mindcollaps.yuki.api.lib.route.DatabaseId;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findTwitchUserConByUser")
public class FindTwitchUserConByUser extends RouteRequest<UserTwitchConnection> {

    @RouteField
    private String userid;

    public FindTwitchUserConByUser(DatabaseId user) {
        super(UserTwitchConnection.class);
        this.userid = user.getDatabaseId();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}