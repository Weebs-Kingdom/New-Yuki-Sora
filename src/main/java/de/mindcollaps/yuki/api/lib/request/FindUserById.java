package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findUserById")
public class FindUserById extends RouteRequest<DiscApplicationUser> {

    @RouteField
    private String id;

    public FindUserById(String userId) {
        super(DiscApplicationUser.class);
        this.id = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
