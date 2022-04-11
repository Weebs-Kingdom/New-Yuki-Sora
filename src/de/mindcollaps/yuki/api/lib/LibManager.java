package yuki.api.lib;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import yuki.api.lib.data.DiscApplicationServer;
import yuki.api.lib.data.DiscApplicationUser;
import yuki.api.lib.request.FindServerByGuildId;
import yuki.api.lib.request.FindUserById;
import yuki.core.YukiSora;

public class LibManager {

    public static DiscApplicationServer retrieveServer(Guild g, YukiSora yukiSora){
        FindServerByGuildId req = new FindServerByGuildId(g.getId());
        DiscApplicationServer server = req.makeRequestSingle(yukiSora);
        if(server == null){
            server = new DiscApplicationServer();
            server.setServerID(g.getId());
            server.setServerName(g.getName());
            server.postData(yukiSora);
        }

        return server;
    }

    public static DiscApplicationUser retrieveUser(User user, YukiSora yukiSora){
        FindUserById req = new FindUserById(user.getId());
        DiscApplicationUser discUser = req.makeRequestSingle(yukiSora);
        if(discUser == null){
            discUser = new DiscApplicationUser();
            discUser.setUserID(user.getId());
            discUser.setUserName(user.getName());
            discUser.postData(yukiSora);
        }

        return discUser;
    }
}
