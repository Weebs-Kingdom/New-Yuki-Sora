package de.mindcollaps.yuki.application.discord.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONObject;
import de.mindcollaps.yuki.core.YukiSora;

public class DiscordUtil {

    public static boolean userHasGuildAdminPermission(Member member, Guild guild, TextChannel textChannel, YukiSora engine) {
        boolean hasPermission = false;
        for (int i = 0; member.getRoles().size() > i; i++) {
            for (int a = 0; member.getRoles().get(i).getPermissions().toArray().length > a; a++) {
                if (member.getRoles().get(i).getPermissions().toArray()[a] == Permission.ADMINISTRATOR) {
                    hasPermission = true;
                    break;
                }
            }
        }
        if (hasPermission) {
            return true;
        } else {
            TextUtil.sendError("You have no permission for this command! You have to be an Admin to use that command!", textChannel);
            return false;
        }
    }

    public static String getAttackInfo(JSONObject at) {
        return at.get("attackName") + " dmg: " + at.get("baseDmg") + " type: " + at.get("attackType") + " status effect: " + at.get("statusEffect");
    }

    public static String getGame(Member m) {
        String game = "";
        for (Activity a : m.getActivities()) {
            if (a.getType() == Activity.ActivityType.PLAYING) {
                game = a.getName();
            }
        }

        switch (game) {
            case "Tom Clancy's Rainbow Six Siege":
                game = "Rainbow";
                break;

            case "":
                game = null;
        }

        return game;
    }

    private static int getNumber(JSONObject o, String r) {
        try {
            return Math.toIntExact((Long) o.get(r));
        } catch (Exception e) {
            return Math.toIntExact(Math.round((Double) o.get(r)));
        }
    }
}
