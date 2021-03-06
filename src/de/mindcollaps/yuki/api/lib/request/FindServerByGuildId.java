package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.RouteClass;
import de.mindcollaps.yuki.api.lib.RouteField;
import de.mindcollaps.yuki.api.lib.RouteRequest;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;

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
