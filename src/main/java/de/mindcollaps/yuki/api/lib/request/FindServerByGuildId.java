package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findServerById")
public class FindServerByGuildId extends RouteRequest<DiscApplicationServer> {

    @RouteField
    private String id;

    public FindServerByGuildId(String guildId) {
        super(DiscApplicationServer.class);
        this.id = guildId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
