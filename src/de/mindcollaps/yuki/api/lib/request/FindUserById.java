package yuki.api.lib.request;

import yuki.api.lib.RouteClass;
import yuki.api.lib.RouteField;
import yuki.api.lib.RouteRequest;
import yuki.api.lib.data.DiscApplicationServer;
import yuki.api.lib.data.DiscApplicationUser;

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
