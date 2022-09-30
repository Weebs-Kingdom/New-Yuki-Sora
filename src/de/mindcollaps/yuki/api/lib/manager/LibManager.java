package de.mindcollaps.yuki.api.lib.manager;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.request.FindServerByGuildId;
import de.mindcollaps.yuki.api.lib.request.FindUserById;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

public class LibManager {

    @Nullable
    public static DiscApplicationServer retrieveServer(Guild g, YukiSora yukiSora) {
        FindServerByGuildId req = new FindServerByGuildId(g.getId());
        DiscApplicationServer server = req.makeRequestSingle(yukiSora);
        if (server == null) {
            YukiLogger.log(new YukiLogInfo("The guild " + g.getName() + " (" + g.getId() + ") was not found in the database! The guild was created by the lib manager.").warning());
            server = new DiscApplicationServer();
            server.setServerId(g.getId());
            server.setServerName(g.getName());
            server.postData(yukiSora);

            if (server.getDatabaseId() == null) {
                YukiLogger.log(new YukiLogInfo("The guild " + g.getName() + " (" + g.getId() + ") could not be created by the lib manager.").warning());
                return null;
            }
        }

        return server;
    }

    @Nullable
    public static DiscApplicationUser retrieveUser(User user, YukiSora yukiSora) {
        FindUserById req = new FindUserById(user.getId());
        DiscApplicationUser discUser = req.makeRequestSingle(yukiSora);
        if (discUser == null) {
            YukiLogger.log(new YukiLogInfo("The user " + user.getName() + " (" + user.getId() + ") was not found in the database! The user was created by the lib manager.").log());
            discUser = new DiscApplicationUser();
            discUser.setUserID(user.getId());
            discUser.setUsername(user.getName());
            discUser.postData(yukiSora);

            if (discUser.getDatabaseId() == null) {
                YukiLogger.log(new YukiLogInfo("The user " + user.getName() + " (" + user.getId() + ") could not be created by the lib manager.").log());
                return null;
            }
        }

        return discUser;
    }
}
