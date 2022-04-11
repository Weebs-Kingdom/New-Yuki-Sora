package yuki.api.lib.request;

import yuki.api.lib.RouteClass;
import yuki.api.lib.RouteField;
import yuki.api.lib.RouteRequest;
import yuki.api.lib.data.DiscApplicationServer;

@RouteClass("findServerById")
public class FindServerByGuildId extends RouteRequest<DiscApplicationServer> {

    @RouteField
    private String id;

    public FindServerByGuildId(String id) {
        super(DiscApplicationServer.class);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
