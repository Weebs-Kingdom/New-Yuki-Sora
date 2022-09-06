package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.data.UserTwitchConnection;
import de.mindcollaps.yuki.api.lib.request.FindTwitchUserConByUser;
import de.mindcollaps.yuki.application.discord.command.*;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.YukiUtil;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class DiscCmdSetup extends DiscCommand {

    public DiscCmdSetup() {
        super("setup", "This command is used to setup the server and all of its features");
        isAdminOnlyCommand();

        addSubcommands(
                new SubCommandGroup("channel", "With this sub command you can edit channels")
                        .addSubCommands(
                                new SubCommand("twitch", "Create a twitch channel with this command")
                                        .addOption(
                                                new CommandOption(OptionType.CHANNEL, "channel", "The new twitch channel"))
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                GuildChannel gc = args.getArg("channel").getGuildChannel();
                                                if (gc == null) {
                                                    TextUtil.sendError("Seems like this channel does not exist :hushed:", event.getChannel());
                                                } else {
                                                    server.setTwitchNotifyChannel(gc.getId());
                                                    server.updateData(yukiSora);

                                                    TextUtil.sendSuccess("A new twitch channel is setup and running!\nI will inform anyone when somebody that is listed in my database starts streaming :laughing:", event.getChannel());
                                                }
                                            }
                                        }),
                                new SubCommand("welcome", "Create a welcome channel where the members need to verify that they will behave probably :innocent:")
                                        .addOption(OptionType.CHANNEL, "channel", "The new welcome channel")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                GuildChannel gc = args.getArg("channel").getGuildChannel();
                                                if (gc == null) {
                                                    TextUtil.sendError("Seems like this channel does not exist :hushed:", event.getChannel());
                                                } else {
                                                    server.setWelcomeMessageChannel(gc.getId());
                                                    server.updateData(yukiSora);

                                                    TextUtil.sendSuccess("Changed the welcome channel. You may update the certification message now :relaxed:", event.getChannel());
                                                }
                                            }
                                        })));

        addSubcommands(
                new SubCommandGroup("category", "With this sub command you can edit channels")
                        .addSubCommands(
                                new SubCommand("statistics", "Create a statistics category")
                                        .addOption(OptionType.STRING, "category", "The category where the statistics will be shown")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                String categoryId = args.getArg("category").getString();
                                                boolean error = false;
                                                try {
                                                    Category category = yukiSora.getDiscordApplication().getBotJDA().getCategoryById(categoryId);
                                                    if (category == null)
                                                        error = true;
                                                } catch (Exception e) {
                                                    error = true;
                                                }
                                                if (error) {
                                                    TextUtil.sendError("Seems like this category doesn't exist :hushed:", event.getChannel());
                                                } else {
                                                    server.setStatisticsCategoryId(categoryId);
                                                    server.updateServerStats(yukiSora);
                                                    server.updateData(yukiSora);

                                                    TextUtil.sendSuccess("Okay, now we have a new statistics category, let me set that up real quick!", event.getChannel());
                                                }
                                            }
                                        }),
                                new SubCommand("booster", "Create a booster category")
                                        .addOption(OptionType.STRING, "category", "The category where the boosters can do what they like")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                String categoryId = args.getArg("category").getString();
                                                boolean error = false;
                                                try {
                                                    Category category = yukiSora.getDiscordApplication().getBotJDA().getCategoryById(categoryId);
                                                    if (category == null)
                                                        error = true;
                                                } catch (Exception e) {
                                                    error = true;
                                                }
                                                if (error) {
                                                    TextUtil.sendError("Seems like this category doesn't exist :hushed:", event.getChannel());
                                                } else {
                                                    server.setBoosterCategoryId(categoryId);
                                                    server.updateData(yukiSora);

                                                    TextUtil.sendSuccess("Okay, now we have a new booster category for all the beautiful boosters UwU", event.getChannel());
                                                }
                                            }
                                        })
                        ));

        addSubcommands(
                new SubCommandGroup("boosterrole", "With this sub command you can changed the meaning of a role")
                        .addSubCommands(
                                new SubCommand("set", "Crate a booster role")
                                        .addOption(OptionType.ROLE, "role", "The role the boosters will get when boosting")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                Role role = args.getArg("role").getRole();
                                                if (role == null) {
                                                    TextUtil.sendError("Seems like this role doesn't exist :hushed:", event.getChannel());
                                                } else {
                                                    server.setBoosterRoleId(role.getId());
                                                    server.updateData(yukiSora);
                                                    TextUtil.sendSuccess("Okay our dear boosters have a role which value there donation UwU", event.getChannel());
                                                }
                                            }
                                        })
                        ));

        addSubcommands(
                new SubCommandGroup("defaultrole", "With this sub command you can changed the meaning of a role")
                        .addSubCommands(
                                new SubCommand("add", "Add or remove a default role")
                                        .addOption(OptionType.ROLE, "role", "The role you want to add")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                server.setDefaultRoles(YukiUtil.addToArray(server.getDefaultRoles(), args.getArg("role").getRole().getId()));
                                                server.updateData(yukiSora);
                                            }
                                        }),
                                new SubCommand("remove", "Add a default role")
                                        .addOption(OptionType.ROLE, "role", "The role you want to add")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                server.setDefaultRoles(YukiUtil.removeFromArray(server.getDefaultRoles(), args.getArg("role").getRole().getId()));
                                                server.updateData(yukiSora);
                                            }
                                        }),
                                new SubCommand("list", "Add a default role")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                StringBuilder builder = new StringBuilder();
                                                boolean changed = false;
                                                if (server.getDefaultRoles().length == 0) {
                                                    TextUtil.sendWarning("Seems like there are now default roles yet owo", event.getChannel());
                                                } else {
                                                    for (String defaultRole : server.getDefaultRoles()) {
                                                        Role role = event.getGuild().getRoleById(defaultRole);
                                                        if (role == null) {
                                                            server.setDefaultRoles(YukiUtil.removeFromArray(server.getDefaultRoles(), defaultRole));
                                                            changed = true;
                                                        } else
                                                            builder.append(role.getName()).append(", ");
                                                    }
                                                    if (changed)
                                                        server.updateData(yukiSora);

                                                    builder.delete(builder.length() - 2, builder.length() - 1);

                                                    TextUtil.sendSuccess("List:\n" + builder.toString(), event.getChannel());
                                                }
                                            }
                                        })
                        ));

        addSubcommands(
                new SubCommandGroup("text", "With this sub command you can change different default texts").addSubCommands(
                        new SubCommand("welcome", "This changes the welcome text, which is displayed in the welcome channel")
                                .addOption(OptionType.STRING, "text", "The welcome text")
                                .addAction(new CommandAction() {
                                    @Override
                                    public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                    }

                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                        String text = args.getArg("text").getString();
                                        if (text == null) {
                                            TextUtil.sendError("That text seems not very appropriate :c", event.getChannel());
                                        } else {
                                            server.setWelcomeText(text);
                                            server.updateData(yukiSora);

                                            TextUtil.sendSuccess("Changed the welcome channel message. You may update the certification message now :relaxed:", event.getChannel());
                                        }
                                    }
                                })
                ));

        addSubcommands(
                new SubCommandGroup("twitchmember", "Change how the bot interacts with certain twitch streamers listed in the database")
                        .addSubCommands(
                                new SubCommand("add", "Add a user to the notification list")
                                        .addOption(OptionType.USER, "member", "The member you want to add")
                                        .addOption(OptionType.STRING, "twitch-channel", "The channel name of the member")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                User us = args.getArg("member").getUser();
                                                if (us == null) {
                                                    TextUtil.sendError("That member can't be found!", event.getChannel());
                                                }
                                                String twitchName = args.getArg("twitch channel").getString();
                                                if (twitchName == null) {
                                                    TextUtil.sendError("That twitch channel is not legit!", event.getChannel());
                                                }

                                                UserTwitchConnection con = new FindTwitchUserConByUser(user.getUserID()).makeRequestSingle(yukiSora);
                                                if (con == null) {
                                                    con = new UserTwitchConnection();
                                                    con.setUser(user.getUserID());
                                                    con.setTwitchChannelId(twitchName);
                                                    con.setServers(new String[]{server.getServerID()});
                                                    con.postData(yukiSora);

                                                    TextUtil.sendSuccess(us.getName() + " was added to the twitch database :relaxed:", event.getChannel());
                                                } else {
                                                    boolean found = false;
                                                    for (String conServer : con.getServers()) {
                                                        if (conServer.equals(server.getServerID())) {
                                                            found = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!found) {
                                                        con.setServers(YukiUtil.addToArray(con.getServers(), server.getServerID()));
                                                        con.updateData(yukiSora);
                                                        TextUtil.sendSuccess(server.getServerName() + " was added to the twitch record of " + us.getName() + " :relaxed:", event.getChannel());
                                                    } else {
                                                        TextUtil.sendWarning(server.getServerName() + " is already registered in " + us.getName() + " ", event.getChannel());
                                                    }
                                                }
                                            }
                                        }),

                                new SubCommand("remove", "Add a user to the notification list")
                                        .addOption(OptionType.USER, "member", "The member you want to add")
                                        .addAction(new CommandAction() {
                                            @Override
                                            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel(), yukiSora);
                                            }

                                            @Override
                                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                                                User us = args.getArg("member").getUser();
                                                if (us == null) {
                                                    TextUtil.sendError("That member can't be found!", event.getChannel());
                                                }

                                                UserTwitchConnection con = new FindTwitchUserConByUser(user.getUserID()).makeRequestSingle(yukiSora);
                                                if (con == null) {

                                                } else {
                                                    boolean found = false;
                                                    for (String conServer : con.getServers()) {
                                                        if (conServer.equals(server.getServerID())) {
                                                            found = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!found) {
                                                        con.setServers(YukiUtil.addToArray(con.getServers(), server.getServerID()));
                                                        con.updateData(yukiSora);
                                                    }
                                                }
                                            }
                                        })
                        ));
    }

    @Override
    public String getHelp() {
        return null;
    }
}
