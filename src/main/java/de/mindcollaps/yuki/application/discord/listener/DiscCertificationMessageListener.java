package de.mindcollaps.yuki.application.discord.listener;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.data.ServerUser;
import de.mindcollaps.yuki.api.lib.manager.LibManager;
import de.mindcollaps.yuki.api.lib.request.FindServerUsersByUser;
import de.mindcollaps.yuki.api.lib.request.FindUserById;
import de.mindcollaps.yuki.application.discord.response.Response;
import de.mindcollaps.yuki.application.discord.response.ResponseHandler;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.application.discord.util.MessageUtil;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.YukiUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;
import java.util.List;

@YukiLogModule(name = "Disc Listener - Certification Listener")
public class DiscCertificationMessageListener extends ListenerAdapter {

    private final YukiSora yukiSora;

    public DiscCertificationMessageListener(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        //if member = null, than its a private channel
        if (event.getMember() == null)
            return;
        if (event.getMember().getUser().isBot())
            return;

        DiscApplicationServer server = LibManager.retrieveServer(event.getGuild(), yukiSora);
        if (server == null)
            return;

        if (isCertificationMessage(event.getMessageId(), event.getChannel(), server)) {
            switch (event.getEmoji().getName()) {
                //haken-Emoji
                case "✅":
                    addMemberCertification(event.getMember(), server);
                    removeOtherReactions(event.getMember(), "✅", event.getChannel(), event.getMessageId());
                    break;

                //X-Emoji
                case "❌":
                    informRemoveCertification(event.getMember(), server, yukiSora);
                    removeOtherReactions(event.getMember(), "", event.getChannel(), event.getMessageId());
                    break;

                //Game-Emoji
                case "\uD83C\uDFAE":
                    addTempMemberCertification(event.getMember(), server);
                    removeOtherReactions(event.getMember(), "\uD83C\uDFAE", event.getChannel(), event.getMessageId());
                    break;
            }
        }
    }

    private boolean isCertificationMessage(String messageId, MessageChannel channel, DiscApplicationServer server) {
        return server.getCertificationChannelId().equals(channel.getId()) && server.getCertificationMessageId().equals(messageId);
    }

    private void removeOtherReactions(Member member, String except, MessageChannel channel, String messageId) {
        Message message = channel.retrieveMessageById(messageId).complete();
        if (message == null)
            return;

        for (MessageReaction reaction : message.getReactions()) {
            for (User user : message.retrieveReactionUsers(reaction.getEmoji()).complete()) {
                if (user.getId().equals(member.getId()) && !reaction.getEmoji().getName().equals(except))
                    reaction.removeReaction(member.getUser()).queue();
            }
        }
    }

    private void addMemberCertification(Member member, DiscApplicationServer server) {
        DiscApplicationUser user = LibManager.retrieveUser(member.getUser(), yukiSora);
        if (user == null)
            return;
        ServerUser serverUser = LibManager.retrieveServerUser(server, user, yukiSora);
        if(serverUser == null)
            return;

        addMemberRolesToMember(server, member, false);
        issueWelcomeMessageForMember(user, serverUser, server, member);

        serverUser.setTempMember(false);
        serverUser.setMember(true);
        serverUser.setDateBecomeMember(new Date());
        serverUser.updateData(yukiSora);
    }

    private void informRemoveCertification(Member member, DiscApplicationServer server, YukiSora yukiSora) {
        DiscApplicationUser user = new FindUserById(member.getId()).makeRequestSingle(yukiSora);
        if (user == null)
            return;
        Message msg = member.getUser().openPrivateChannel().complete().sendMessageEmbeds(
                new EmbedBuilder()
                        .setDescription("You have requested a data deletion. We ask you to confirm this action by pressing the ✅-Emoji.\n\n" +
                                "However, if your user account is a member of another server of the **Weebs Kingdom Network**, your account will just loose its certification and data for this guild **, " + server.getGuildName() + "**\" +\n" +
                                "\nIf you want to request a data deletion of all your data stored at the YukiSora-Suit, please visit our website https://weebskingdom.com/privacy\n\n\n")
                        .addField("Pressing ✅", "This will delete all your data from our database, that is stored and linked to this guild **" + server.getGuildName() + "**.", true)
                        .addField("What will be lost 🚮", "All your data we stored about you, including" +
                                "\n- monsters" +
                                "\n- money" +
                                "\n- preferences" +
                                "\n- items and more" +
                                "\nWill be lost unless you are a member of another **Weebs Kingdom Network** guild!", true)


                        .setColor(Color.RED)
                        .setTitle("Data deletion")
                        .build()
        ).complete();

        msg.addReaction(Emoji.fromUnicode("✅")).complete();

        Response r = new Response() {
            @Override
            public void onEmote(MessageReactionAddEvent respondingEvent) {
                if (respondingEvent.getEmoji().equals(Emoji.fromUnicode("✅"))) {
                    deleteUserData(respondingEvent, member, user, server, msg);
                } else {
                    respondingEvent.getChannel().sendMessageEmbeds(new EmbedBuilder()
                            .setColor(Color.YELLOW)
                            .setDescription("You have not confirmed the data deletion. Your data will not be deleted :smiling_face_with_3_hearts:")
                            .build()).queue();
                }
            }
        };
        r.discUserId = member.getUser().getId();
        r.discMessageId = msg.getId();
        r.discChannelId = msg.getChannel().getId();

        ResponseHandler.makeResponse(r);
    }

    private void deleteUserData(MessageReactionAddEvent event, Member member, DiscApplicationUser user, DiscApplicationServer server, Message previousMessage) {
        ServerUser serverUser = LibManager.retrieveServerUser(server, user, yukiSora);
        if (serverUser == null)
            return;

        serverUser.deleteData(yukiSora);

        removeRolesFromMember(server, member);

        ServerUser[] serverUsers = new FindServerUsersByUser(user.getDatabaseId()).makeRequest(yukiSora);
        if (serverUsers.length == 0) {
            user.deleteData(yukiSora);
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setDescription("As you were not previously registered on another server, all the data stored in the **Yuki-Sora-Suit** has been erased.")
                    .build()).queue();
        } else {
            event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setDescription("Since you are registered on another server, your data stored in the Yuki-Sora-Suit has not been completely deleted. However, please note that your data for " + server.getGuildName() + " has been erased.")
                    .build()).queue();
        }
        previousMessage.delete().queue();
    }

    private void addTempMemberCertification(Member member, DiscApplicationServer server) {
        DiscApplicationUser user = LibManager.retrieveUser(member.getUser(), yukiSora);
        if (user == null)
            return;

        ServerUser serverUser = LibManager.retrieveServerUser(server, user, yukiSora);
        if (serverUser == null)
            return;

        addMemberRolesToMember(server, member, true);

        serverUser.setTempMember(true);
        serverUser.setMember(false);
        serverUser.setDateBecomeTempMember(new Date());
        serverUser.updateData(yukiSora);
    }

    private void issueWelcomeMessageForMember(DiscApplicationUser user, ServerUser serverUser, DiscApplicationServer server, Member member) {
        if (serverUser.isSaidHello())
            return;
        if (server.getWelcomeMessageChannelId() == null)
            return;
        if(server.getWelcomeMessageChannelId().length() == 0)
            return;

        MessageChannel welcomeChannel = member.getGuild().getChannelById(MessageChannel.class, server.getWelcomeMessageChannelId());
        if (welcomeChannel == null)
            return;

        MessageEmbed embed = new EmbedBuilder().
                setAuthor("A new member just joined! " + member.getEffectiveName(), null, member.getEffectiveAvatarUrl())
                .setColor(Color.CYAN)
                .setDescription(MessageUtil.getWelcomeMessage(member.getEffectiveName()))
                .build();

        //Don't need to update, because it will be updated in the rest of the issued function
        serverUser.setSaidHello(true);

        welcomeChannel.sendMessageEmbeds(embed).complete();
    }

    private void addMemberRolesToMember(DiscApplicationServer server, Member member, boolean temp) {
        addDefaultRolesToMember(server, member);
        Role role = null;
        if (temp) {
            if (!YukiUtil.isEmpty(server.getDefaultTempMemberRoleId()))
                role = member.getGuild().getRoleById(server.getDefaultTempMemberRoleId());
        } else {
            if (!YukiUtil.isEmpty(server.getDefaultMemberRoleId()))
                role = member.getGuild().getRoleById(server.getDefaultMemberRoleId());
        }

        if (role != null)
            DiscordUtil.assignRolesToMembers(member.getGuild(), List.of(role), member);
    }

    private void addDefaultRolesToMember(DiscApplicationServer server, Member member) {
        DiscordUtil.assignRolesToMembers(member.getGuild(), DiscordUtil.getRolesFromGuild(member.getGuild(), server.getDefaultRoles()), member);
    }

    private void removeDefaultRolesFromMember(DiscApplicationServer server, Member member) {
        DiscordUtil.removeRolesFromMembers(member.getGuild(), DiscordUtil.getRolesFromGuild(member.getGuild(), server.getDefaultRoles()), member);
    }

    private void removeRolesFromMember(DiscApplicationServer server, Member member) {
        removeDefaultRolesFromMember(server, member);
        Role role = null;
        if (!YukiUtil.isEmpty(server.getDefaultTempMemberRoleId()))
            role = member.getGuild().getRoleById(server.getDefaultTempMemberRoleId());

        Role role1 = null;
        if (!YukiUtil.isEmpty(server.getDefaultMemberRoleId()))
            role1 = member.getGuild().getRoleById(server.getDefaultMemberRoleId());

        if (role != null)
            DiscordUtil.removeRolesFromMembers(member.getGuild(), List.of(role), member);
        if (role1 != null)
            DiscordUtil.removeRolesFromMembers(member.getGuild(), List.of(role1), member);
    }

}
