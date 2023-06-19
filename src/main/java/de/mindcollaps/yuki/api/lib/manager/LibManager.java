package de.mindcollaps.yuki.api.lib.manager;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.data.ServerUser;
import de.mindcollaps.yuki.api.lib.request.FindServerByGuildId;
import de.mindcollaps.yuki.api.lib.request.FindServerUserByUserAndServer;
import de.mindcollaps.yuki.api.lib.request.FindUserById;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

@YukiLogModule(name = "Lib Manager")
public class LibManager {

    @Nullable
    public static DiscApplicationServer retrieveServer(Guild g, YukiSora yukiSora) {
        FindServerByGuildId req = new FindServerByGuildId(g.getId());
        DiscApplicationServer server = req.makeRequestSingle(yukiSora);
        if (server == null) {
            YukiLogger.log(new YukiLogInfo("The guild " + g.getName() + " (" + g.getId() + ") was not found in the database! The guild was created by the lib manager.").debug());
            server = new DiscApplicationServer();
            server.setGuildId(g.getId());
            server.setGuildName(g.getName());
            server.postData(yukiSora);

            if (server.getDatabaseId() == null) {
                YukiLogger.log(new YukiLogInfo("The guild " + g.getName() + " (" + g.getId() + ") could not be created by the lib manager.").error());
                return null;
            }
        }

        return server;
    }

    @Nullable
    public static DiscApplicationUser retrieveUser(User user, YukiSora yukiSora) {
        return retrieveUser(user.getId(), user.getName(), yukiSora);
    }

    @Nullable
    public static DiscApplicationUser retrieveUser(String userId, String userName, YukiSora yukiSora) {
        FindUserById req = new FindUserById(userId);
        DiscApplicationUser discUser = req.makeRequestSingle(yukiSora);
        if (discUser == null) {
            YukiLogger.log(new YukiLogInfo("The user " + userName + " (" + userId + ") was not found in the database! The user was created by the lib manager.").debug());
            discUser = new DiscApplicationUser();
            discUser.setUserID(userId);
            discUser.setUsername(userName);
            discUser.postData(yukiSora);

            if (discUser.getDatabaseId() == null) {
                YukiLogger.log(new YukiLogInfo("The user " + userName + " (" + userId + ") could not be created by the lib manager.").error());
                return null;
            }
        }

        return discUser;
    }

    public static ServerUser retrieveServerUser(DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
        ServerUser serverUser = new FindServerUserByUserAndServer(user.getDatabaseId(), server.getDatabaseId()).makeRequestSingle(yukiSora);
        if(serverUser == null){
            YukiLogger.log(new YukiLogInfo("The server user " + user.getUsername() + " (" + user.getUserID() + ") was not found in the database! The user was created by the lib manager.").debug());
            serverUser = new ServerUser();
            serverUser.setServerId(server.getDatabaseId());
            serverUser.setUser(user.getDatabaseId());
            serverUser.postData(yukiSora);

            if (serverUser.getDatabaseId() == null) {
                YukiLogger.log(new YukiLogInfo("The server user " + user.getUsername() + " (" + user.getUserID() + ") could not be created by the lib manager.").error());
                return null;
            }
        }

        return serverUser;
    }

    @Nullable
    public static DiscApplicationUser findUser(String userId, YukiSora yukiSora) {
        FindUserById req = new FindUserById(userId);

        return req.makeRequestSingle(yukiSora);
    }
}
