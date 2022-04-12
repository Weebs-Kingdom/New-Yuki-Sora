package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.RouteClass;
import de.mindcollaps.yuki.api.lib.RouteField;
import de.mindcollaps.yuki.api.lib.RouteRequest;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;

@RouteClass("findUserById")
public class FindUserById extends RouteRequest<DiscApplicationUser> {

    @RouteField
    private String id;

    public FindUserById(String id) {
        super(DiscApplicationUser.class);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
