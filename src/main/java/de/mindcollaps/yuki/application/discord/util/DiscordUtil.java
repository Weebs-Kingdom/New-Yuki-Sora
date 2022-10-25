package de.mindcollaps.yuki.application.discord.util;

import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiscordUtil {

    public static boolean userHasGuildAdminPermission(Member member, Guild guild, TextUtil.ResponseInstance res, YukiSora engine) {
        boolean hasPermission = false;
        for (int i = 0; member.getRoles().size() > i; i++) {
            for (int a = 0; member.getRoles().get(i).getPermissions().toArray().length > a; a++) {
                if (member.getRoles().get(i).getPermissions().toArray()[a] == Permission.ADMINISTRATOR) {
                    hasPermission = true;
                    break;
                }
            }
            if (hasPermission)
                break;
        }
        if (hasPermission) {
            return true;
        } else {
            TextUtil.sendError("You have no permission for this command! You have to be an Admin to use that command!", res);
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

    public static ArrayList<Role> getRolesFromGuild(Guild g, String... roles) {
        ArrayList<Role> roleList = new ArrayList<>();
        for (String role : roles) {
            Role r = g.getRoleById(role);
            if (r != null)
                roleList.add(r);
        }

        return roleList;
    }

    public static void assignRolesToMembers(Guild g, List<Role> roles, Member... members) {
        for (Member member : members) {
            for (Role role : roles) {
                try {
                    g.addRoleToMember(member, role).queue();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void removeRolesFromMembers(Guild g, List<Role> roles, Member... members) {
        for (Member member : members) {
            for (Role role : roles) {
                try {
                    g.removeRoleFromMember(member, role).queue();
                } catch (Exception e) {
                }
            }
        }
    }
}
