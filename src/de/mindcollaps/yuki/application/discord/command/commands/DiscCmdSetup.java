package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.data.UserTwitchConnection;
import de.mindcollaps.yuki.api.lib.request.FindAutoChannelsByIds;
import de.mindcollaps.yuki.api.lib.request.FindTwitchUserConByUser;
import de.mindcollaps.yuki.application.discord.command.*;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.YukiUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscCmdSetup extends DiscCommand {

    private final HashMap<String, ArrayList<Role>> rolesToAdd = new HashMap<>();
    private final HashMap<String, ArrayList<Role>> rolesToRemove = new HashMap<>();

    public DiscCmdSetup() {
        super("setup", "This command is used to setup the server and all of its features");
        adminOnlyCommand();
        noCallClient();

        addSubcommands(
                new SubCommandGroup("channel", "With this sub command you can edit channels")
                        .addSubCommands(
                                new SubCommand("twitch", "Create a twitch channel with this command")
                                        .addOptions(
                                                new CommandOption(OptionType.CHANNEL, "channel", "The new twitch channel"))
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                createTwitchChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                createTwitchChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        }),
                                new SubCommand("welcome", "A channel where every member gets welcomed when it first joins")
                                        .addOptions(OptionType.CHANNEL, "channel", "The new welcome channel")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                createWelcomeChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                createWelcomeChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        }),
                                new SubCommand("certification", "The channel where the members get verified")
                                        .addOptions(OptionType.CHANNEL, "channel", "The new certification channel")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                createCertificationChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                createCertificationChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        })));

        addSubcommands(
                new SubCommandGroup("category", "With this sub command you can edit channels")
                        .addSubCommands(
                                new SubCommand("statistics", "Create a statistics category")
                                        .addOptions(OptionType.STRING, "category", "The category where the statistics will be shown")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                createStatisticsCategory(args.getArg("category").getString(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                createStatisticsCategory(args.getArg("category").getString(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        }),
                                new SubCommand("booster", "Create a booster category")
                                        .addOptions(OptionType.STRING, "category", "The category where the boosters can do what they like")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                createBoosterCategory(args.getArg("category").getString(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                createBoosterCategory(args.getArg("category").getString(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        })
                        ));

        addSubcommands(
                new SubCommandGroup("boosterrole", "With this sub command you can changed the meaning of a role")
                        .addSubCommands(
                                new SubCommand("set", "Crate a booster role")
                                        .addOptions(OptionType.ROLE, "role", "The role the boosters will get when boosting")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                setBoosterRole(args.getArg("role").getRole(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                setBoosterRole(args.getArg("role").getRole(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        })
                        ));

        addSubcommands(
                new SubCommandGroup("defaultrole", "With this sub command you can changed the meaning of a role")
                        .addSubCommands(
                                new SubCommand("add", "Add or remove a default role")
                                        .addOptions(OptionType.ROLE, "role", "The role you want to add")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                addDefaultRole(args.getArg("role").getRole(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                addDefaultRole(args.getArg("role").getRole(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        }),
                                new SubCommand("remove", "Remove a default role")
                                        .addOptions(OptionType.ROLE, "role", "The role you want to add")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                removeDefaultRole(args.getArg("role").getRole(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                removeDefaultRole(args.getArg("role").getRole(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        }),
                                new SubCommand("list", "Show all default roles registered")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                listDefaultRoles(event.getGuild(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                listDefaultRoles(event.getGuild(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        }),
                                new SubCommand("update", "Updates the default roles of each player")
                                        .addAction(new CommandAction() {
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
                                                updateRoles(event.getGuild(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                updateRoles(event.getGuild(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        })
                        ));

        addSubcommands(
                new SubCommandGroup("autochannel", "With this sub command you can edit autochannels").addSubCommands(
                        new SubCommand("create", "This creates a new auto channel from a existing channel")
                                .addOptions(OptionType.CHANNEL, "channel", "The new auto channel")
                                .addAction(new CommandAction() {
                                    @Override
                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                    }

                                    @Override
                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                    }

                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        createAutoChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        createAutoChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                    }
                                }),
                        new SubCommand("remove", "This removes a auto channel and turns it back into a normal channel")
                                .addOptions(OptionType.CHANNEL, "channel", "The auto channel")
                                .addAction(new CommandAction() {
                                    @Override
                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                    }

                                    @Override
                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                    }

                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        removeAutoChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        removeAutoChannel(args.getArg("channel").getGuildChannel(), server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                    }
                                })
                ));

        addSubcommands(
                new SubCommandGroup("renew", "With this subcommand you can renew potentially broken systems").addSubCommands(
                        new SubCommand("certification", "This renews the certification message")
                                .addAction(new CommandAction() {
                                    @Override
                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                    }

                                    @Override
                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                    }

                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        renewCertificationMessage(server, event.getGuild(), yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        renewCertificationMessage(server, event.getGuild(), yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                    }
                                }),
                        new SubCommand("statistics", "This updates the statistics category completely")
                                .addAction(new CommandAction() {
                                    @Override
                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                    }

                                    @Override
                                    public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                    }

                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        renewStatisticsCategory(server, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        renewStatisticsCategory(server, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                    }
                                })
                ));

        addSubcommands(
                new SubCommandGroup("twitchmember", "Change how the bot interacts with certain twitch streamers listed in the database")
                        .addSubCommands(
                                new SubCommand("add", "Add a user to the twitch notification list")
                                        .addOptions(OptionType.USER, "member", "The member you want to add")
                                        .addOptions(OptionType.STRING, "twitch-channel", "The channel name of the member")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                twitchMemberAdd(args.getArg("member").getUser(), args.getArg("twitch channel").getString(), server, user, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                twitchMemberAdd(args.getArg("member").getUser(), args.getArg("twitch channel").getString(), server, user, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        }),

                                new SubCommand("remove", "Remove a user from the twitch notification list")
                                        .addOptions(OptionType.USER, "member", "The member you want to add")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getChannel()), yukiSora);
                                            }

                                            @Override
                                            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), new TextUtil.ResponseInstance(event.getInteraction()), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                twitchMemberRemove(args.getArg("member").getUser(), server, user, yukiSora, new TextUtil.ResponseInstance(event.getChannel()));
                                            }

                                            @Override
                                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                                twitchMemberRemove(args.getArg("member").getUser(), server, user, yukiSora, new TextUtil.ResponseInstance(event.getInteraction()));
                                            }
                                        })
                        ));
    }

    private void createTwitchChannel(GuildChannel gc, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (gc == null) {
            TextUtil.sendError("Seems like this channel does not exist :hushed:", res);
        } else {
            server.setTwitchNotifyChannelId(gc.getId());
            server.updateData(yukiSora);

            TextUtil.sendSuccess("A new twitch channel is setup and running!\nI will inform anyone when somebody that is listed in my database starts streaming :laughing:", res);
        }
    }

    private void createWelcomeChannel(GuildChannel gc, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (gc == null) {
            TextUtil.sendError("Seems like this channel does not exist :hushed:", res);
        } else {

            server.setWelcomeMessageChannelId(gc.getId());
            server.updateData(yukiSora);

            TextUtil.sendSuccess("Changed the welcome channel :relaxed:", res);
        }
    }

    private void createCertificationChannel(GuildChannel gc, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (gc == null) {
            TextUtil.sendError("Seems like this channel does not exist :hushed:", res);
        } else {
            if (!YukiUtil.isEmpty(server.getCertificationChannelId())) {
                MessageChannel channel = gc.getGuild().getChannelById(MessageChannel.class, server.getCertificationChannelId());
                if (channel != null) {
                    if (!YukiUtil.isEmpty(server.getCertificationMessageId())) {
                        Message m = null;
                        try {
                            m = channel.retrieveMessageById(server.getCertificationMessageId()).complete();
                        } catch (Exception e) {
                        }
                        if (m != null)
                            m.delete().queue();
                    }
                }
            }

            server.setCertificationChannelId(gc.getId());
            server.updateData(yukiSora);

            TextUtil.sendSuccess("Changed the certification channel, you may renew the certification message now :relaxed:", res);
        }
    }

    private void createStatisticsCategory(String categoryId, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        boolean error = false;
        try {
            Category category = yukiSora.getDiscordApplication().getBotJDA().getCategoryById(categoryId);
            if (category == null)
                error = true;
        } catch (Exception e) {
            error = true;
        }
        if (error) {
            TextUtil.sendError("Seems like this category doesn't exist :hushed:", res);
        } else {
            server.setStatisticsCategoryId(categoryId);
            server.updateServerStats(yukiSora);
            server.updateData(yukiSora);

            TextUtil.sendSuccess("Okay, now we have a new statistics category, let me set that up real quick!", res);
        }
    }

    private void createBoosterCategory(String categoryId, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        boolean error = false;
        try {
            Category category = yukiSora.getDiscordApplication().getBotJDA().getCategoryById(categoryId);
            if (category == null)
                error = true;
        } catch (Exception e) {
            error = true;
        }
        if (error) {
            TextUtil.sendError("Seems like this category doesn't exist :hushed:", res);
        } else {
            server.setBoosterCategoryId(categoryId);
            server.updateData(yukiSora);

            TextUtil.sendSuccess("Okay, now we have a new booster category for all the beautiful boosters UwU", res);
        }
    }

    private void setBoosterRole(Role role, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (role == null) {
            TextUtil.sendError("Seems like this role doesn't exist :hushed:", res);
        } else {
            server.setBoosterRoleId(role.getId());
            server.updateData(yukiSora);
            TextUtil.sendSuccess("Okay our dear boosters have a role which value there donation UwU", res);
        }
    }

    private void addDefaultRole(Role role, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (checkIfStringPresent(server.getDefaultRoles(), role.getId())) {
            TextUtil.sendWarning("The role `@" + role.getName() + "` is already registered as default role!", res);
        } else {
            server.setDefaultRoles(YukiUtil.addToArray(server.getDefaultRoles(), role.getId()));
            server.updateData(yukiSora);

            checkAndRemoveRole(getRoleListFromGuild(rolesToRemove, role.getGuild()), role);
            checkAndAddRole(getRoleListFromGuild(rolesToAdd, role.getGuild()), role);

            TextUtil.sendSuccess("Added `@" + role.getName() + "` to default roles!\n\nYou may update the roles of each member by using the `defaultrole update` command :wink:", res);
        }
    }

    private void removeDefaultRole(Role role, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (checkIfStringPresent(server.getDefaultRoles(), role.getId())) {
            server.setDefaultRoles(YukiUtil.removeFromArray(server.getDefaultRoles(), role.getId()));
            server.updateData(yukiSora);

            checkAndRemoveRole(getRoleListFromGuild(rolesToAdd, role.getGuild()), role);
            checkAndAddRole(getRoleListFromGuild(rolesToRemove, role.getGuild()), role);

            TextUtil.sendSuccess("Removed `@" + role.getName() + "` from default roles!\n\nYou may update the roles of each member by using the `defaultrole update` command :wink:", res);
        } else {
            TextUtil.sendWarning("The role `@" + role.getName() + "` was not registered as default role!", res);
        }
    }

    private boolean checkIfStringPresent(String[] list, String st) {
        for (String s : list) {
            if (s.equals(st))
                return true;
        }
        return false;
    }

    private void checkAndRemoveRole(ArrayList<Role> list, Role r) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(r.getId()))
                list.remove(i);
        }
    }

    private void checkAndAddRole(ArrayList<Role> list, Role r) {
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(r.getId())) {
                found = true;
                break;
            }
        }
        if (!found)
            list.add(r);
    }

    private ArrayList<Role> getRoleListFromGuild(HashMap<String, ArrayList<Role>> map, Guild g) {
        ArrayList<Role> l = map.get(g.getId());

        if (l == null) {
            map.put(g.getId(), new ArrayList<>());
            return map.get(g.getId());
        } else
            return l;
    }

    //TODO: implement multiple page list framework (not so important)
    private void listDefaultRoles(Guild guild, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        StringBuilder builder = new StringBuilder();
        boolean changed = false;
        if (server.getDefaultRoles().length == 0) {
            TextUtil.sendWarning("Seems like there are now default roles yet owo\n\nYou may add some by using the `defaultrole add` command :blush:", res);
        } else {
            for (String defaultRole : server.getDefaultRoles()) {
                Role role = guild.getRoleById(defaultRole);
                if (role == null) {
                    server.setDefaultRoles(YukiUtil.removeFromArray(server.getDefaultRoles(), defaultRole));
                    changed = true;
                } else
                    builder.append(role.getName()).append(", ");
            }
            if (changed)
                server.updateData(yukiSora);

            builder.delete(builder.length() - 2, builder.length() - 1);

            TextUtil.sendColoredText("List of default roles:\n" + builder, Color.BLUE, res);
        }
    }

    private void twitchMemberAdd(User us, String twitchName, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (us == null) {
            TextUtil.sendError("That member can't be found!", res);
        }
        if (twitchName == null) {
            TextUtil.sendError("That twitch channel is not legit!", res);
        }

        UserTwitchConnection con = new FindTwitchUserConByUser(user.getUserID()).makeRequestSingle(yukiSora);
        if (con == null) {
            con = new UserTwitchConnection();
            con.setUser(user.getUserID());
            con.setTwitchChannelId(twitchName);
            con.setServers(new String[]{server.getServerId()});
            con.postData(yukiSora);

            TextUtil.sendSuccess(us.getName() + " was added to the twitch database :relaxed:", res);
        } else {
            boolean found = false;
            for (String conServer : con.getServers()) {
                if (conServer.equals(server.getServerId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                con.setServers(YukiUtil.addToArray(con.getServers(), server.getServerId()));
                con.updateData(yukiSora);
                TextUtil.sendSuccess(server.getServerName() + " was added to the twitch record of " + us.getName() + " :relaxed:", res);
            } else {
                TextUtil.sendWarning(us.getName() + " is already registered in " + server.getServerName(), res);
            }
        }
    }

    private void twitchMemberRemove(User us, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (us == null) {
            TextUtil.sendError("That member can't be found!", res);
        }

        UserTwitchConnection con = new FindTwitchUserConByUser(user.getUserID()).makeRequestSingle(yukiSora);
        if (con == null) {
            TextUtil.sendError("It seems like this member has no twitch records :person_shrugging:", res);
        } else {
            boolean found = false;
            for (String conServer : con.getServers()) {
                if (conServer.equals(server.getServerId())) {
                    found = true;
                    con.setServers(YukiUtil.removeFromArray(con.getServers(), server.getServerId()));
                    con.updateData(yukiSora);
                    TextUtil.sendSuccess("Removed " + us.getName() + " from the twitch record of this server", res);
                    break;
                }
            }
            if (!found) {
                TextUtil.sendError("It seems like this member has no twitch record on this server :person_shrugging:", res);
            }
        }
    }

    private void updateRoles(Guild g, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        if (getRoleListFromGuild(rolesToRemove, g).size() > 0)
            DiscordUtil.removeRolesFromMembers(g, rolesToRemove.get(g.getId()), g.getMembers().toArray(new Member[0]));
        if (getRoleListFromGuild(rolesToAdd, g).size() > 0)
            DiscordUtil.assignRolesToMembers(g, rolesToAdd.get(g.getId()), g.getMembers().toArray(new Member[0]));

        getRoleListFromGuild(rolesToAdd, g).clear();
        getRoleListFromGuild(rolesToRemove, g).clear();

        TextUtil.sendSuccess("Updated the default roles of each member :confetti_ball:", res);
    }

    private void removeAutoChannel(GuildChannel channel, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance responseInstance) {
        AutoChannel autoChannel = new FindAutoChannelsByIds(channel.getGuild().getId(), channel.getId()).makeRequestSingle(yukiSora);
        if (autoChannel == null) {
            TextUtil.sendWarning("This channel was never a Auto Channel :person_shrugging:", responseInstance);
        } else {
            autoChannel.deleteData(yukiSora);
            TextUtil.sendSuccess("Removed Auto Channel :thumbsup:", responseInstance);
        }
    }

    private void createAutoChannel(GuildChannel channel, DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance responseInstance) {
        AutoChannel autoChannel = new AutoChannel(server.getDatabaseId(), channel.getId(), 0);
        try {
            autoChannel.initBaseChannel(channel.getGuild(), yukiSora);
        } catch (Exception e) {
            TextUtil.sendError("Failed to initialize the Auto channel, are you sure, this is a Voice Channel?", responseInstance);
            YukiLogger.log(new YukiLogInfo("Failed to initialize Auto Channel!").trace(e));
            return;
        }
        autoChannel.postData(yukiSora);
        TextUtil.sendSuccess("Created new Auto Channel :thumbsup:", responseInstance);
    }

    private void renewCertificationMessage(DiscApplicationServer server, Guild g, YukiSora yukiSora, TextUtil.ResponseInstance responseInstance) {
        if (YukiUtil.isEmpty(server.getCertificationChannelId())) {
            TextUtil.sendError("There is no certification message channel selected, try using the `setup channel certification` command.", responseInstance);
            return;
        }
        MessageChannel channel = g.getChannelById(MessageChannel.class, server.getCertificationChannelId());
        if (channel == null) {
            TextUtil.sendError("Seems like this channel does not exist :hushed:", responseInstance);
            return;
        }

        if (!YukiUtil.isEmpty(server.getCertificationMessageId())) {
            Message m = null;
            try {
                m = channel.retrieveMessageById(server.getCertificationMessageId()).complete();
            } catch (Exception e) {
            }
            if (m != null)
                m.delete().queue();
        }

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle("Member Certification")
                .setDescription("By pressing ✅ or \uD83C\uDFAE **bellow this message** you accept\n• the :notebook_with_decorative_cover: **rules** :notebook_with_decorative_cover: of **" + g.getName() + "** and\n• the  **terms of service** of this bot **Yuki Sora**.\n\n\n")
                .addField("Terms of Service", "https://weebskingdom.com/bot-terms-of-service/\n\n\n", false)
                .addField("Pressing ✅", "Become a member of and enjoy all the features of this guild.", true)
                .addField("Pressing ❌", "Remove all your data stored in **Yuki Sora** and remove your rights to use this server.", true)
                .addField("Pressing \uD83C\uDFAE", "Become a temporary member and don't get annoyed by guild announcements, events  etc.", true)
                .build();

        Message message = channel.sendMessageEmbeds(embed).complete();

        message.addReaction(Emoji.fromUnicode("✅")).complete();
        message.addReaction(Emoji.fromUnicode("❌")).complete();
        message.addReaction(Emoji.fromUnicode("\uD83C\uDFAE")).complete();

        server.setCertificationMessageId(message.getId());
        server.updateData(yukiSora);

        TextUtil.sendSuccess("Renewed the certification message :fire:", responseInstance);
    }

    private void renewStatisticsCategory(DiscApplicationServer server, YukiSora yukiSora, TextUtil.ResponseInstance responseInstance) {
        try {
            server.updateServerStats(yukiSora);
        } catch (Exception e) {
            TextUtil.sendError("There was an error while renewing the statistics category!", responseInstance);
            return;
        }
        TextUtil.sendSuccess("Renewed the statistics category :thumbsup:", responseInstance);
    }

    @Override
    public String getHelp() {
        return null;
    }
}
