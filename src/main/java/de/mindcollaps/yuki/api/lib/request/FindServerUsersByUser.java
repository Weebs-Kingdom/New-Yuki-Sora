package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.ServerUser;
import de.mindcollaps.yuki.api.lib.route.DatabaseId;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findServerUsersByUser")
public class FindServerUsersByUser extends RouteRequest<ServerUser> {

    @RouteField
    private String userid;

    public FindServerUsersByUser(DatabaseId user) {
        super(ServerUser.class);
        this.userid = user.getDatabaseId();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String id) {
        this.userid = id;
    }
}
