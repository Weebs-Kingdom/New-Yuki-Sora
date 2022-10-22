package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.data.Vote;
import de.mindcollaps.yuki.api.lib.data.VoteElement;
import de.mindcollaps.yuki.api.lib.request.FindVoteElementsByVoteId;
import de.mindcollaps.yuki.api.lib.request.FindVotesByGuildChannel;
import de.mindcollaps.yuki.application.discord.command.*;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.internal.entities.emoji.CustomEmojiImpl;
import okio.GzipSink;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DiscCmdVote extends DiscCommand {
    public DiscCmdVote() {
        super("vote", "Create powerful votes");

        noCallClient();
        adminOnlyCommand();

        addSubcommands(
                new SubCommandGroup("add", "Add content to your votes")
                        .addSubCommands(
                                new SubCommand("role", "Adds a selectable role to a vote. The role will than be assigned to the member")
                                        .addOptions(
                                                new CommandOption(OptionType.CHANNEL, "channel", "The channel where you want to put the selectable role"),
                                                new CommandOption(OptionType.STRING, "emote", "The emote that represents the role"),
                                                new CommandOption(OptionType.STRING, "description", "The description of the role"),
                                                new CommandOption(OptionType.ROLE, "role", "The role you want to add"),
                                                new CommandOption(OptionType.STRING, "id", "The index/title/id of the vote you want to add this role to")
                                        )
                                        .addAction(
                                                new CommandAction() {
                                                    @Override
                                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                                    }

                                                    @Override
                                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                                    }

                                                    @Override
                                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        addRoleToVote(args.getArg("role").getRole(),
                                                                args.getArg("channel").getGuildChannel(),
                                                                args.getArg("id").getString(),
                                                                args.getArg("description").getString(),
                                                                args.getArg("emote").getString(),
                                                                server,
                                                                yukiSora,
                                                                new TextUtil.ResponseInstance(event.getChannel()));
                                                    }

                                                    @Override
                                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        addRoleToVote(args.getArg("role").getRole(),
                                                                args.getArg("channel").getGuildChannel(),
                                                                args.getArg("id").getString(),
                                                                args.getArg("description").getString(),
                                                                args.getArg("emote").getString(),
                                                                server,
                                                                yukiSora,
                                                                new TextUtil.ResponseInstance(event.getInteraction()));
                                                    }
                                                }
                                        ),
                                new SubCommand("line", "Add a line to your vote")
                                        .addOptions(
                                                new CommandOption(OptionType.CHANNEL, "channel", "The channel where you want to put the selectable role"),
                                                new CommandOption(OptionType.STRING, "emote", "The emote that represents the role"),
                                                new CommandOption(OptionType.STRING, "description", "The description of the role"),
                                                new CommandOption(OptionType.STRING, "id", "The index/title/id of the vote you want to add this role to")
                                        )
                                        .addAction(
                                                new CommandAction() {
                                                    @Override
                                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                                    }

                                                    @Override
                                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                                    }

                                                    @Override
                                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        addLineToVote(
                                                                args.getArg("channel").getGuildChannel(),
                                                                args.getArg("id").getString(),
                                                                args.getArg("description").getString(),
                                                                args.getArg("emote").getString(),
                                                                server,
                                                                yukiSora,
                                                                new TextUtil.ResponseInstance(event.getChannel())
                                                        );
                                                    }

                                                    @Override
                                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                        addLineToVote(
                                                                args.getArg("channel").getGuildChannel(),
                                                                args.getArg("id").getString(),
                                                                args.getArg("description").getString(),
                                                                args.getArg("emote").getString(),
                                                                server,
                                                                yukiSora,
                                                                new TextUtil.ResponseInstance(event.getInteraction())
                                                        );
                                                    }
                                                }
                                        )
                        )
        );

        addSubcommands(
                new SubCommand("create", "Create your vote")
                        .addOptions(
                                new CommandOption(OptionType.CHANNEL, "channel", "The channel where you want to put your vote"),
                                new CommandOption(OptionType.STRING, "title", "The title of your vote"),
                                new CommandOption(OptionType.STRING, "color", "Hex color of the vote (leave empty for default)").optional()
                        )
                        .addAction(
                                new CommandAction() {
                                    @Override
                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                    }

                                    @Override
                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                    }

                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        createVote(
                                                args.getArg("channel").getGuildChannel(),
                                                args.getArg("title").getString(),
                                                args.getArg("color").getString(),
                                                server,
                                                yukiSora,
                                                new TextUtil.ResponseInstance(event.getChannel()));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        createVote(
                                                args.getArg("channel").getGuildChannel(),
                                                args.getArg("title").getString(),
                                                args.getArg("color").getString(),
                                                server,
                                                yukiSora,
                                                new TextUtil.ResponseInstance(event.getInteraction()));
                                    }
                                }
                        )
        );

        addSubcommands(
                new SubCommand("delete", "Remove a vote")
                        .addOptions(
                                new CommandOption(OptionType.CHANNEL, "channel", "The channel the vote you want to delete is located in"),
                                new CommandOption(OptionType.STRING, "id", "The index/title/id of the vote you want to delete")
                        )
                        .addAction(
                                new CommandAction() {
                                    @Override
                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                    }

                                    @Override
                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                    }

                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        deleteVote(
                                                args.getArg("channel").getGuildChannel(),
                                                args.getArg("id").getString(),
                                                server,
                                                yukiSora,
                                                new TextUtil.ResponseInstance(event.getChannel())
                                        );
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        deleteVote(
                                                args.getArg("channel").getGuildChannel(),
                                                args.getArg("id").getString(),
                                                server,
                                                yukiSora,
                                                new TextUtil.ResponseInstance(event.getInteraction())
                                        );
                                    }
                                }
                        )
        );
    }

    private void createVote(GuildChannel channel, String title, String color, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        Color messageColor = null;
        if (color != null) {
            if (!color.startsWith("#"))
                color = "#" + color;

            try {
                messageColor = Color.decode(color);
            } catch (Exception e) {
                messageColor = Color.ORANGE;
            }
        } else {
            messageColor = Color.ORANGE;
        }

        Vote[] votes = new FindVotesByGuildChannel(server.getDatabaseId(), channel.getId()).makeRequest(yukiSora);

        //Arrays.sort(votes, (o1, o2) -> Integer.compare(o1.getIndex(), o2.getIndex()));

        Vote vote = new Vote();
        vote.setVoteType(0);
        vote.setServer(server.getDatabaseId());
        vote.setChannelId(channel.getId());
        vote.setVoteColor("#" + Integer.toHexString(messageColor.getRGB()).substring(2));
        if (votes != null)
            vote.setIndex(votes.length);
        else
            vote.setIndex(0);

        vote.setVoteTitle(title);


        Message message = vote.createNewVote(channel);
        vote.setMessageId(message.getId());

        vote.postData(yukiSora);

        TextUtil.sendSuccess("Successfully created the vote :blush:", res);
    }

    private void deleteVote(GuildChannel channel, String index, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        Vote[] votes = new FindVotesByGuildChannel(server.getDatabaseId(), channel.getId()).makeRequest(yukiSora);
        Arrays.sort(votes, (o1, o2) -> Integer.compare(o1.getIndex(), o2.getIndex()));

        Vote vote = null;
        try {
            vote = votes[Integer.parseInt(index)];
        } catch (Exception ignored) {
        }

        if (vote == null) {
            try {
                for (Vote ownVote : votes) {
                    if (ownVote.getVoteTitle().equalsIgnoreCase(index)) {
                        vote = ownVote;
                        break;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        if (vote == null) {
            TextUtil.sendError("I didn't find your vote :hushed:\n\n Use the index to determine which vote you would like to modify.", res);
        } else {
            int voteIndex = vote.getIndex();
            VoteElement[] voteElements = new FindVoteElementsByVoteId(vote.getDatabaseId()).makeRequest(yukiSora);
            for (VoteElement voteElement : voteElements) {
                voteElement.deleteData(yukiSora);
            }
            vote.deleteData(yukiSora);
            vote.deleteVote(channel);

            for (int i = voteIndex + 1; i < votes.length; i++) {
                Vote updatedVote = votes[i];
                updatedVote.setIndex(i - 1);

                updatedVote.updateData(yukiSora);
            }

            TextUtil.sendSuccess("The vote was removed from the channel :ok_hand:", res);
        }
    }

    private Vote checkVote(String emote, String index, GuildChannel channel, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        Vote[] votes = new FindVotesByGuildChannel(server.getDatabaseId(), channel.getId()).makeRequest(yukiSora);

        Arrays.sort(votes, (o1, o2) -> Integer.compare(o1.getIndex(), o2.getIndex()));

        Vote vote = null;
        try {
            vote = votes[Integer.parseInt(index)];
        } catch (Exception ignored) {
        }

        if (vote == null) {
            try {
                for (Vote ownVote : votes) {
                    if (ownVote.getVoteTitle().equalsIgnoreCase(index)) {
                        vote = ownVote;
                        break;
                    }
                }
            } catch (Exception ignored) {
            }
        }

        if (vote == null) {
            for (Vote vote1 : votes) {
                if (vote1.getMessageId().equals(index)) {
                    vote = vote1;
                    break;
                }
            }
        }

        if (vote == null) {
            TextUtil.sendError("I didn't find your vote :hushed:\n\n Use the index to determine which vote you would like to modify.", res);
            return null;
        }

        return vote;
    }

    private void addRoleToVote(Role role, GuildChannel channel, String index, String description, String emote, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        Vote vote = checkVote(emote, index, channel, server, yukiSora, res);

        if (vote != null) {
            vote.setVoteType(1);
            if (!addRoleVoteElementToVote(vote, emote, role, channel.getGuild(), description, yukiSora, res))
                return;

            Message msg = vote.updateVote(channel);
            if (msg != null) {
                TextUtil.sendSuccess("Updated vote :blush:", res);
            } else {
                TextUtil.sendError("Couldn't create vote because the channel is invalid :hushed:", res);
            }
        }
    }

    private boolean addEmoji(String emote, Guild guild, Vote vote, TextUtil.ResponseInstance res){
        try {
            Emoji ourEmoji = null;

            EmojiUnion union = Emoji.fromFormatted(emote);
            TextChannel tc = guild.getTextChannelById(vote.getChannelId());

            if(tc != null){
                tc.addReactionById(vote.getMessageId(), union).complete();
            }
        } catch (Exception e) {
            TextUtil.sendError("The emoji you entered is invalid :open_mouth:", res);
            return false;
        }
        return true;
    }

    private void addLineToVote(GuildChannel channel, String index, String description, String emote, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        Vote vote = checkVote(emote, index, channel, server, yukiSora, res);

        if (vote != null) {
            vote.setVoteType(0);
            if (!addLineVoteElementToVote(vote, emote, channel.getGuild(), description, yukiSora, res))
                return;

            Message msg = vote.updateVote(channel);
            if (msg != null) {
                TextUtil.sendSuccess("Updated vote :blush:", res);
            } else {
                TextUtil.sendError("Couldn't create vote because the channel is invalid :hushed:", res);
            }
        }
    }

    private boolean addRoleVoteElementToVote(Vote vote, String emote, Role role, Guild guild, String description, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if(!addEmoji(emote, guild, vote, res))
            return false;

        VoteElement[] voteElements = vote.loadVoteElements(yukiSora);

        int voteIndex = 0;
        if (voteElements != null)
            voteIndex = voteElements.length;

        VoteElement voteElement = new VoteElement();
        voteElement.setVote(vote.getDatabaseId());
        voteElement.setRoleId(role.getId());
        voteElement.setIndex(voteIndex);
        voteElement.setEmote(emote);
        voteElement.setDescription(description);

        voteElement.postData(yukiSora);

        vote.addVoteElement(voteElement);
        return true;
    }

    private boolean addLineVoteElementToVote(Vote vote, String emote, Guild guild, String description, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if(!addEmoji(emote, guild, vote, res))
            return false;

        VoteElement[] voteElements = vote.loadVoteElements(yukiSora);

        int voteIndex = 0;
        if (voteElements != null)
            voteIndex = voteElements.length;

        VoteElement voteElement = new VoteElement();
        voteElement.setVote(vote.getDatabaseId());
        voteElement.setIndex(voteIndex);
        voteElement.setEmote(emote);
        voteElement.setDescription(description);

        voteElement.postData(yukiSora);

        vote.addVoteElement(voteElement);

        return true;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
