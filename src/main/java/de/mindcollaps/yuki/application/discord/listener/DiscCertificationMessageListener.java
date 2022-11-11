package de.mindcollaps.yuki.application.discord.listener;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.manager.LibManager;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.application.discord.util.MessageUtil;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.YukiUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class DiscCertificationMessageListener extends ListenerAdapter {

    private final YukiSora yukiSora;

    public DiscCertificationMessageListener(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
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
                    informRemoveCertification(event.getMember(), server);
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

        addMemberRolesToMember(server, member, false);

        user.setTempMember(false);
        user.setMember(true);
        user.updateData(yukiSora);

    }

    private void informRemoveCertification(Member member, DiscApplicationServer server) {
        //TODO: Make this actually inform the user about data deletion and wait for conformation
        DiscApplicationUser user = LibManager.retrieveUser(member.getUser(), yukiSora);
        if (user == null)
            return;

        user.setTempMember(false);
        user.setMember(false);

        removeRolesFromMember(server, member);
    }

    private void addTempMemberCertification(Member member, DiscApplicationServer server) {
        DiscApplicationUser user = LibManager.retrieveUser(member.getUser(), yukiSora);
        if (user == null)
            return;

        addMemberRolesToMember(server, member, true);
        issueWelcomeMessageForMember(user, server, member);

        user.setTempMember(true);
        user.setMember(false);
        user.setDateBecomeTempMember(new Date());
        user.updateData(yukiSora);
    }

    private void issueWelcomeMessageForMember(DiscApplicationUser user, DiscApplicationServer server, Member member) {
        if (user.isSaidHello())
            return;
        if (server.getWelcomeMessageChannelId() == null)
            return;

        MessageChannel welcomeChannel = member.getGuild().getChannelById(MessageChannel.class, server.getWelcomeMessageChannelId());
        if (welcomeChannel == null)
            return;

        MessageEmbed embed = new EmbedBuilder().
                setAuthor("Welcome " + member.getEffectiveName(), null, member.getEffectiveAvatarUrl())
                .setColor(Color.CYAN)
                .setDescription("A new member just joined!\n"
                        + MessageUtil.getWelcomeMessage(member.getEffectiveName()))
                .build();

        user.setSaidHello(true);

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
