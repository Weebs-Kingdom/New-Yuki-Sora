package de.mindcollaps.yuki.application.discord.listener;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.data.Vote;
import de.mindcollaps.yuki.api.lib.data.VoteElement;
import de.mindcollaps.yuki.api.lib.manager.LibManager;
import de.mindcollaps.yuki.api.lib.request.FindVoteByMessage;
import de.mindcollaps.yuki.api.lib.request.FindVoteElementsByVoteId;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

public class DiscReactionListener extends ListenerAdapter {

    private final YukiSora yukiSora;

    public DiscReactionListener(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (event.getMember().getUser().isBot())
            return;

        checkForVote(event.getMember(), event.getGuild(), event.getEmoji(), (TextChannel) event.getChannel(), event.getMessageId(), 1);
    }

    @Override
    public void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {
        if (event.getMember().getUser().isBot())
            return;

        checkForVote(event.getMember(), event.getGuild(), event.getEmoji(), (TextChannel) event.getChannel(), event.getMessageId(), -1);
    }

    private void checkForVote(Member member, Guild guild, EmojiUnion emoji, TextChannel textChannel, String messageId, int count) {
        DiscApplicationUser discUser = LibManager.retrieveUser(member.getUser(), yukiSora);
        DiscApplicationServer server = LibManager.retrieveServer(guild, yukiSora);

        if (server == null || discUser == null) {
            if (server == null) {
                YukiLogger.log(new YukiLogInfo("There has been a problem communicating with the database! This issues should be looked at very soon!").error());
                return;
            }
            return;
        }

        Vote v = new FindVoteByMessage(server.getDatabaseId(), textChannel.getId(), messageId).makeRequestSingle(yukiSora);
        if (v != null) {
            VoteElement[] element = v.loadVoteElements(yukiSora);

            for (VoteElement voteElement : element) {
                if (voteElement.getEmote().equals(emoji.getFormatted())) {
                    issueVoteElement(voteElement, v, textChannel, server, member, discUser, count);
                    break;
                }
            }
        }
    }

    private void issueVoteElement(VoteElement element, Vote vote, TextChannel guildChannel, DiscApplicationServer server, Member member, DiscApplicationUser user, int count) {
        switch (vote.getVoteType()) {
            case 0: {
                element.setVotes(element.getVotes() + count);
                element.updateData(yukiSora);

                vote.updateVoteElement(element);

                vote.updateVote(guildChannel);
                break;
            }

            case 1: {
                Role r = null;
                try {
                    r = guildChannel.getGuild().getRoleById(element.getRoleId());
                } catch (Exception e) {
                    YukiLogger.log(
                            new YukiLogInfo(
                                    "The role (" + element.getRoleId() + ") on the guild " + guildChannel.getGuild().getName() + " wasn't found. Maybe this vote needs to be edited!"
                            ).trace(e)
                                    .error()
                    );
                    return;
                }
                if (r != null) {
                    if (count == 1)
                        try {
                            DiscordUtil.assignRolesToMembers(guildChannel.getGuild(), List.of(r), member);
                        } catch (Exception e) {
                            YukiLogger.log(
                                    new YukiLogInfo(
                                            "The role (" + element.getRoleId() + ") on the guild " + guildChannel.getGuild().getName() + " couldn't be added to the member. Maybe this vote needs to be edited!"
                                    ).trace(e)
                                            .error()
                            );
                        }
                    else if (count == -1)
                        try {
                            DiscordUtil.removeRolesFromMembers(guildChannel.getGuild(), List.of(r), member);
                        } catch (Exception e) {
                            YukiLogger.log(
                                    new YukiLogInfo(
                                            "The role (" + element.getRoleId() + ") on the guild " + guildChannel.getGuild().getName() + " couldn't be added to the member. Maybe this vote needs to be edited!"
                                    ).trace(e)
                                            .error()
                            );
                        }
                } else {
                    YukiLogger.log(
                            new YukiLogInfo(
                                    "The role (" + element.getRoleId() + ") on the guild " + guildChannel.getGuild().getName() + " wasn't found. Maybe this vote needs to be edited!"
                            ).error()
                    );
                }
            }
        }
    }
}
