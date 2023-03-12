package de.mindcollaps.yuki.application.task.tasks;

import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.User;

public class TaskGiveRole extends Task{

    private String userId;
    private String role;


    public TaskGiveRole() {
        super("giveRole");
    }

    @Override
    public boolean action(YukiSora yukiSora) {
        User user = yukiSora.getDiscordApplication().getBotJDA().retrieveUserById(userId).complete();
        DiscordUtil.giveRoleToMemberOnAllGuilds(user, yukiSora, role);
        return true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
