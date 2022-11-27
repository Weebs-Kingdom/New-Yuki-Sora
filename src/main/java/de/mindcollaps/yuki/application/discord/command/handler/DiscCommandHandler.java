package de.mindcollaps.yuki.application.discord.command.handler;

import de.mindcollaps.yuki.application.discord.command.*;
import de.mindcollaps.yuki.application.discord.command.special.SpecialCommand;
import de.mindcollaps.yuki.application.discord.core.DiscordApplication;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@YukiLogModule(name = "Disc Command Handler")
public class DiscCommandHandler {

    public final HashMap<String, DiscCommand> commands = new HashMap<>();
    public final ArrayList<String> commandIvokes = new ArrayList<>();
    private final DiscordApplication application;

    public DiscCommandHandler(DiscordApplication application) {
        this.application = application;
    }

    private DiscCommand findCommand(String invoke) {
        return commands.get(invoke);
    }

    private CommandAction findAction(DiscCommand command, String[] args, CommandHandlingReport report) {
        if (command.getAction() != null)
            return command.getAction();

        SubCommand subCommand = checkSubCommands(command.getSubCommands().toArray(new SubCommand[0]), args, 0, report);

        if (subCommand == null) {
            subCommand = checkSubCommandGroups(command.getSubCommandGroups().toArray(new SubCommandGroup[0]), args, report);
            if (subCommand == null)
                return null;
        }
        report.setFoundCommand(subCommand);


        return subCommand.getAction();
    }

    private SubCommand checkSubCommands(SubCommand[] commands, String[] args, int checkI, CommandHandlingReport report) {
        if (args.length - 1 >= checkI)
            for (SubCommand command : commands) {
                if (command.getInvoke().equalsIgnoreCase(args[checkI])) {
                    report.foundCommand = command;
                    return command;
                }
            }
        return null;
    }

    private SubCommand checkSubCommandGroups(SubCommandGroup[] commands, String[] args, CommandHandlingReport report) {
        if (args.length - 1 >= 0)
            for (SubCommandGroup command : commands) {
                if (command.getInvoke().equalsIgnoreCase(args[0])) {
                    report.setFoundGroup(command);
                    SubCommand command1 = checkSubCommands(command.getSubCommands().toArray(new SubCommand[0]), args, 0 + 1, report);
                    if (command1 != null)
                        return command1;
                }
            }
        return null;
    }

    private String getReportErrorMessage(CommandHandlingReport report, DiscCommand command) {
        return ":warning: Please check the validity of your command arguments :warning:\n\nErrors:\n" + report.build() + "\n\nUse **-" + command.getInvoke() + " help** if you need further details";
    }

    private void printErrorMessage(String error, MessageChannel messageChannel) {
        messageChannel.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("That didn't work")
                        .setDescription(error)
                        .build()
        ).queue();
    }

    private void printErrorMessage(String error, SlashCommandInteraction interaction) {
        interaction.replyEmbeds(
                new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("That didn't work")
                        .setDescription(error)
                        .build()
        ).queue();
    }

    private void sendHelp(DiscCommand command, SubCommand subCommand, SubCommandGroup subCommandGroup, MessageChannel messageChannel) {
        String help = command.getHelp();
        if (help == null) {
            if (subCommand == null) {
                if (subCommandGroup == null)
                    messageChannel.sendMessageEmbeds(buildHelp(command).build()).queue();
                else
                    messageChannel.sendMessageEmbeds(buildHelp(command, subCommandGroup).build()).queue();
            } else {
                messageChannel.sendMessageEmbeds(buildHelp(command, subCommand).build()).queue();
            }
        } else {
            messageChannel.sendMessageEmbeds(new EmbedBuilder()
                    .setColor(Color.BLUE)
                    .setTitle(command.getInvoke() + " help")
                    .setDescription(help)
                    .build()
            ).queue();
        }
    }

    private void sendHelp(DiscCommand command, SubCommand subCommand, SubCommandGroup subCommandGroup, SlashCommandInteraction interaction) {
        String help = command.getHelp();
        if (help == null) {
            if (subCommand == null) {
                if (subCommandGroup == null)
                    interaction.replyEmbeds(buildHelp(command).build()).queue();
                else
                    interaction.replyEmbeds(buildHelp(command, subCommandGroup).build()).queue();
            } else {
                interaction.replyEmbeds(buildHelp(command, subCommand).build()).queue();
            }
        } else {
            interaction.replyEmbeds(new EmbedBuilder()
                    .setColor(Color.BLUE)
                    .setTitle(command.getInvoke() + " help")
                    .setDescription(help)
                    .build()
            ).queue();
        }
    }

    private EmbedBuilder buildHelp(DiscCommand command) {
        StringBuilder builder = new StringBuilder().append(command.getDescription()).append("\n\n");
        ArrayList<String> commandsAdded = new ArrayList<>();

        if (command.getAction() == null) {
            builder.append("List of subcommands:\n");
            for (SubCommand subCommand : command.getSubCommands()) {
                if (commandsAdded.contains(subCommand.getInvoke()))
                    continue;

                builder.append("... **").append(subCommand.getInvoke()).append("**\n");
                commandsAdded.add(subCommand.getInvoke());
            }
            for (SubCommandGroup subCommand : command.getSubCommandGroups()) {
                if (commandsAdded.contains(subCommand.getInvoke()))
                    continue;

                builder.append("... **").append(subCommand.getInvoke()).append("**\n");
                commandsAdded.add(subCommand.getInvoke());
            }
        } else {
            if (command.getOptions().size() > 0) {
                builder.append(command.getInvoke()).append(" ");
                for (CommandOption option : command.getOptions()) {
                    builder.append(buildOptionHelp(option)).append(" ");
                }
            }
        }

        builder.deleteCharAt(builder.length() - 1);

        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle(command.getInvoke() + " command help")
                .setDescription(builder.toString());
    }

    private EmbedBuilder buildHelp(DiscCommand rcommand, SubCommand command) {
        StringBuilder builder = new StringBuilder("\n").append(command.getDescription()).append("\n\nList of options:\n");

        ArrayList<SubCommand> commands = new ArrayList<>();
        for (SubCommand subCommand : rcommand.getSubCommands()) {
            if (subCommand.getInvoke().equalsIgnoreCase(command.getInvoke()))
                commands.add(subCommand);
        }

        for (SubCommand subCommand : commands) {
            helpBuild(builder, subCommand);
        }

        /**
         if (command.getOptions().size() > 0) {
         for (CommandOption option : command.getOptions()) {
         builder.append("... ");
         builder.append(buildOptionHelp(option)).append(" ");
         }
         }
         **/

        builder.deleteCharAt(builder.length() - 1);

        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle(command.getInvoke() + " subcommand help")
                .setDescription(builder.toString());
    }

    private void helpBuild(StringBuilder builder, SubCommand subCommand) {
        builder.append("... **").append(subCommand.getInvoke()).append("**").append(" ");
        for (CommandOption option : subCommand.getOptions()) {
            builder.append(buildOptionHelp(option)).append(" ");
        }

        String desciption = null;

        for (CommandOption option : subCommand.getOptions()) {
            if (option.getType() == OptionType.SUB_COMMAND)
                desciption = option.getDescription();
        }

        if (desciption != null)
            builder.append(" - _").append(desciption).append("_");

        builder.append("\n");
    }

    private EmbedBuilder buildHelp(DiscCommand rcommand, SubCommandGroup command) {
        StringBuilder builder = new StringBuilder("\n").append(command.getDescription()).append("\n\nList of subcommands:\n");

        for (SubCommand subCommand : command.getSubCommands()) {
            helpBuild(builder, subCommand);
        }

        builder.deleteCharAt(builder.length() - 1);

        return new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle(command.getInvoke() + " subcommand help")
                .setDescription(builder.toString());
    }

    private String buildOptionHelp(CommandOption option) {
        return option.getType() == OptionType.SUB_COMMAND ? "**" + option.getName() + "**" :
                "<" + option.getName() + ">";
    }

    private DiscCommandArgs generateCommandArgs(CommandHandlingReport report, DiscCommand command, String[] args, MessageChannel messageChannel, Message message, YukiSora yukiSora) {
        SubCommand subCommand = report.getFoundCommand();
        DiscCommandArgs discArgs = new DiscCommandArgs(args);

        if (subCommand == null) {
            for (int i = 0; i < command.getOptions().size(); i++) {
                CommandOption option = command.getOptions().get(i);
                DiscCommandArgument argument = new DiscCommandArgument(option.getName(), option.getType());
                if (option.getType() == OptionType.SUB_COMMAND || option.getType() == OptionType.SUB_COMMAND_GROUP)
                    continue;

                testArgValidity(argument, report, args[i], messageChannel, option, message, yukiSora);
                discArgs.addCommandArgument(argument);
            }
            return discArgs;
        }


        discArgs.addCommandArgument(new DiscCommandArgument(subCommand.getInvoke(), OptionType.SUB_COMMAND));
        for (int i = 0; i < subCommand.getOptions().size(); i++) {
            CommandOption option = subCommand.getOptions().get(i);
            DiscCommandArgument argument = new DiscCommandArgument(option.getName(), option.getType());

            if (option.getType() == OptionType.SUB_COMMAND || option.getType() == OptionType.SUB_COMMAND_GROUP) {
                discArgs.addCommandArgument(argument);
                continue;
            }

            if (args.length - 1 >= i)
                testArgValidity(argument, report, args[i], messageChannel, option, message, yukiSora);
            else
                report.invalid = true;
            discArgs.addCommandArgument(argument);
        }

        return discArgs;
    }

    private DiscCommandArgs generateCommandArgs(CommandHandlingReport report, DiscCommand command, String[] args, SlashCommandInteractionEvent event, YukiSora yukiSora) {
        SubCommand subCommand = report.getFoundCommand();
        DiscCommandArgs discArgs = new DiscCommandArgs(args);

        if (subCommand == null) {
            for (int i = 0; i < command.getOptions().size(); i++) {
                CommandOption option = command.getOptions().get(i);
                DiscCommandArgument argument = new DiscCommandArgument(option.getName(), option.getType());
                if (option.getType() == OptionType.SUB_COMMAND || option.getType() == OptionType.SUB_COMMAND_GROUP)
                    continue;

                discArgs.addCommandArgument(argument);
            }
            return discArgs;
        }


        discArgs.addCommandArgument(new DiscCommandArgument(subCommand.getInvoke(), OptionType.SUB_COMMAND));
        for (int i = 0; i < subCommand.getOptions().size(); i++) {
            CommandOption option = subCommand.getOptions().get(i);
            DiscCommandArgument argument = new DiscCommandArgument(option.getName(), option.getType());

            if (option.getType() == OptionType.SUB_COMMAND || option.getType() == OptionType.SUB_COMMAND_GROUP) {
                discArgs.addCommandArgument(argument);
                continue;
            }

            if (args.length - 1 >= i) {
                OptionMapping mapping = event.getOption(option.getName());
                if (mapping == null) {
                    if (!option.isOptional())
                        report.addError("Option **" + option.getName() + "** is empty!");
                } else {
                    testArgValidity(argument, report, option, mapping, yukiSora);
                }
            } else
                report.invalid = true;
            discArgs.addCommandArgument(argument);
        }

        return discArgs;
    }

    private void testArgValidity(DiscCommandArgument argument, CommandHandlingReport report, CommandOption option, OptionMapping optionMapping, YukiSora yukiSora) {
        switch (option.getType()) {
            case ATTACHMENT:
            case MENTIONABLE:
            case UNKNOWN:
            case SUB_COMMAND:
            case SUB_COMMAND_GROUP:
                break;
            case STRING:
                argument.setString(optionMapping.getAsString());
                break;
            case INTEGER:
                try {
                    argument.setInteger(optionMapping.getAsInt());
                } catch (Exception e) {
                    addCastError(report, option, optionMapping.getAsString());
                }
                break;
            case BOOLEAN:
                try {
                    argument.setaBoolean(optionMapping.getAsBoolean());
                } catch (Exception e) {
                    addCastError(report, option, optionMapping.getAsString());
                }
                break;
            case USER:
                boolean userError = false;
                try {
                    User user = optionMapping.getAsUser();
                    if (user == null)
                        userError = true;
                    else
                        argument.setUser(user);
                } catch (Exception e) {
                    userError = true;
                }
                if (userError) {
                    addCastError(report, option, optionMapping.getAsString());
                }
                break;
            case CHANNEL:
                boolean channelError = false;
                try {
                    GuildChannel channel = optionMapping.getAsChannel();
                    if (channel == null)
                        channelError = true;
                    else
                        argument.setGuildChannel(channel);
                } catch (Exception e) {
                    channelError = true;
                }
                if (channelError)
                    addCastError(report, option, optionMapping.getAsString());
                break;
            case ROLE:
                boolean roleError = false;
                try {
                    Role role = optionMapping.getAsRole();
                    if (role == null)
                        roleError = true;
                    else
                        argument.setRole(role);
                } catch (Exception e) {
                    roleError = true;
                }

                if (roleError)
                    addCastError(report, option, optionMapping.getAsString());
                break;
            case NUMBER:
                try {
                    argument.setaDouble(optionMapping.getAsDouble());
                } catch (Exception e) {
                    addCastError(report, option, optionMapping.getAsString());
                }
                break;
        }
    }

    private void testArgValidity(DiscCommandArgument argument, CommandHandlingReport report, String arg, MessageChannel messageChannel, CommandOption option, Message message, YukiSora yukiSora) {
        int users = 0;
        int roles = 0;
        int channels = 0;

        switch (option.getType()) {
            case ATTACHMENT:
            case MENTIONABLE:
            case UNKNOWN:
            case SUB_COMMAND:
            case SUB_COMMAND_GROUP:
                break;
            case STRING:
                argument.setString(arg);
                break;
            case INTEGER:
                try {
                    argument.setInteger(Integer.parseInt(arg));
                } catch (Exception e) {
                    addCastError(report, option, arg);
                }
                break;
            case BOOLEAN:
                try {
                    argument.setaBoolean(Boolean.parseBoolean(arg));
                } catch (Exception e) {
                    addCastError(report, option, arg);
                }
                break;
            case USER:
                boolean userError = false;
                try {
                    User user = message.getMentions().getUsers().get(users);
                    if (user == null)
                        userError = true;
                    else
                        argument.setUser(user);
                } catch (Exception e) {
                    userError = true;
                }
                if (userError) {
                    userError = false;
                    try {
                        User user = yukiSora.getDiscordApplication().getBotJDA().retrieveUserById(arg).complete();
                        if (user == null)
                            userError = true;
                        else
                            argument.setUser(user);
                    } catch (Exception e) {
                        userError = true;
                    }
                    if (userError) {
                        addCastError(report, option, arg);
                    }
                }
                users++;
                break;
            case CHANNEL:
                boolean channelError = false;
                try {
                    GuildChannel channel = message.getMentions().getChannels().get(channels);
                    if (channel == null)
                        channelError = true;
                    else
                        argument.setGuildChannel(channel);
                } catch (Exception e) {
                    channelError = true;
                }
                if (channelError) {
                    channelError = false;
                    try {
                        GuildChannel channel = yukiSora.getDiscordApplication().getBotJDA().getGuildChannelById(arg);
                        if (channel == null)
                            channelError = true;
                        else
                            argument.setGuildChannel(channel);
                    } catch (Exception e) {
                        channelError = true;
                    }
                    if (channelError) {
                        channelError = false;
                        try {
                            VoiceChannel channel = yukiSora.getDiscordApplication().getBotJDA().getVoiceChannelById(arg);
                            if (channel == null)
                                channelError = true;
                            else
                                argument.setVoiceChannel(channel);
                        } catch (Exception e) {
                            channelError = true;
                        }

                        if (channelError)
                            addCastError(report, option, arg);
                    }
                }
                channels++;
                break;
            case ROLE:
                boolean roleError = false;
                try {
                    Role role = message.getMentions().getRoles().get(roles);
                    if (role == null)
                        roleError = true;
                    else
                        argument.setRole(role);
                } catch (Exception e) {
                    roleError = true;
                }

                if (roleError) {
                    roleError = false;
                    try {
                        Role role = yukiSora.getDiscordApplication().getBotJDA().getRoleById(arg);
                        if (role == null)
                            roleError = true;
                        else
                            argument.setRole(role);
                    } catch (Exception e) {
                        roleError = true;
                    }
                    if (roleError)
                        addCastError(report, option, arg);
                }
                break;
            case NUMBER:
                try {
                    argument.setaDouble(Double.parseDouble(arg));
                } catch (Exception e) {
                    addCastError(report, option, arg);
                }
                break;
        }
    }

    private void addCastError(CommandHandlingReport report, CommandOption option, String input) {
        report.errors.add(buildCastErrorMessage(option, input));
    }

    private String buildCastErrorMessage(CommandOption option, String input) {
        return "Your input `" + input + "` for the parameter **" + option.getName() + "** does not match the type **" + option.getType().name() + "**\n";
    }

    public void handleServerCommand(DiscCommandParser.ServerCommandContainer cmd) {
        DiscCommand command = findCommand(cmd.invoke);
        if (command == null) {
            printErrorMessage("Couldn't find a command matching " + cmd.invoke, cmd.event.getChannel());
        } else {
            CommandHandlingReport report = new CommandHandlingReport();
            CommandAction action = findAction(command, cmd.args, report);
            if (cmd.args.length > 0)
                if (cmd.args[0].equalsIgnoreCase("help")) {
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getChannel());
                    return;
                }
            if (cmd.args.length > 1) {
                if (cmd.args[1].equalsIgnoreCase("help")) {
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getChannel());
                    return;
                }
            }
            if (cmd.args.length > 2)
                if (cmd.args[2].equalsIgnoreCase("help")) {
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getChannel());
                    return;
                }

            DiscCommandArgs args = generateCommandArgs(report, command, cmd.args, cmd.event.getChannel(), cmd.event.getMessage(), cmd.yukiSora);
            boolean exe = false;

            if (report.errors.size() > 0 || report.invalid || action == null) {
                if (report.foundCommand == null || report.errors.size() > 0)
                    printErrorMessage(":warning: Please check the validity of your command arguments :warning:\n\nErrors:\n" + report.build() + "\n\nUse **-" + command.getInvoke() + " help** if you need further details", cmd.event.getChannel());
                else
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getChannel());
                return;
            }

            if (command.isCallableServer()) {
                try {
                    exe = action.calledServer(args, cmd.event, cmd.server, cmd.user, cmd.yukiSora);
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle server command had an error while checking if the command can be called!").trace(e));
                }
            }

            if (exe) {
                try {
                    action.actionServer(args, cmd.event, cmd.server, cmd.user, cmd.yukiSora);
                } catch (ActionNotImplementedException e) {
                    printErrorMessage("This command has not been implemented as guild command yet. We are working on it :tools:", cmd.event.getChannel());
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle server command had an error while performing the command action").trace(e));
                }
            }
        }
    }

    public void handleSlashCommand(DiscCommandParser.SlashCommandContainer cmd) {
        DiscCommand command = findCommand(cmd.invoke);
        if (command == null) {
            printErrorMessage("Couldn't find a command matching " + cmd.invoke, cmd.event.getInteraction());
        } else {
            CommandHandlingReport report = new CommandHandlingReport();
            CommandAction action = findAction(command, cmd.args, report);
            if (cmd.args.length > 0)
                if (cmd.args[0].equalsIgnoreCase("help")) {
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getInteraction());
                    return;
                } else if (cmd.args.length > 1)
                    if (cmd.args[1].equalsIgnoreCase("help")) {
                        sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getInteraction());
                        return;
                    }
            DiscCommandArgs args = generateCommandArgs(report, command, cmd.args, cmd.event, cmd.yukiSora);
            boolean exe = false;

            if (report.errors.size() > 0 || report.invalid || action == null) {
                if (report.foundCommand == null || report.errors.size() > 0)
                    printErrorMessage(getReportErrorMessage(report, command), cmd.event.getInteraction());
                else
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getInteraction());
                return;
            }

            if (command.isCallableSlash()) {
                try {
                    exe = action.calledSlash(args, cmd.event, cmd.server, cmd.user, cmd.yukiSora);
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle server command had an error while checking if the command can be called!").trace(e));
                }
            }

            if (exe) {
                try {
                    action.actionSlash(args, cmd.event, cmd.server, cmd.user, cmd.yukiSora);
                } catch (ActionNotImplementedException e) {
                    printErrorMessage("This command has not been implemented as slash command yet. We are working on it :tools:", cmd.event.getInteraction());
                    YukiLogger.log(new YukiLogInfo("The command " + command + " has not been implemented as slash command yet!").trace(e).warning());
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle server command had an error while performing the command action").trace(e));
                }
            }
        }
    }

    public void handleClientCommand(DiscCommandParser.ClientCommandContainer cmd) {
        DiscCommand command = findCommand(cmd.invoke);
        if (command == null) {
            printErrorMessage("Couldn't find a command matching " + cmd.invoke, cmd.event.getChannel());
        } else {
            CommandHandlingReport report = new CommandHandlingReport();
            CommandAction action = findAction(command, cmd.args, report);
            if (cmd.args.length > 0)
                if (cmd.args[0].equalsIgnoreCase("help")) {
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getChannel());
                    return;
                } else if (cmd.args.length > 1)
                    if (cmd.args[1].equalsIgnoreCase("help")) {
                        sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getChannel());
                        return;
                    }

            DiscCommandArgs args = generateCommandArgs(report, command, cmd.args, cmd.event.getChannel(), cmd.event.getMessage(), cmd.yukiSora);
            boolean exe = false;

            if (report.errors.size() > 0 || report.invalid || action == null) {
                if (report.foundCommand == null || report.errors.size() > 0)
                    printErrorMessage(getReportErrorMessage(report, command), cmd.event.getChannel());
                else
                    sendHelp(command, report.foundCommand, report.foundGroup, cmd.event.getChannel());
                return;
            }

            if (command.isCallableClient()) {
                try {
                    exe = action.calledPrivate(args, cmd.event, cmd.user, cmd.yukiSora);
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle client command had an error while checking if the command can be called!").trace(e));
                }
            }

            if (exe) {
                try {
                    action.actionPrivate(args, cmd.event, cmd.user, cmd.yukiSora);
                } catch (ActionNotImplementedException e) {
                    printErrorMessage("This command has not been implemented as private command yet. We are working on it :tools:", cmd.event.getChannel());
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle client command had an error while performing the command action").trace(e));
                }
            }

        }
    }

    public void createNewCommand(DiscCommand cmd) {
        if (commandIvokes.contains(cmd.getInvoke().toLowerCase())) {
            YukiLogger.log(new YukiLogInfo(" - !!!Command " + cmd.getInvoke().toLowerCase() + " already exist!").error());
        } else {
            commands.put(cmd.getInvoke().toLowerCase(), cmd);
            commandIvokes.add(cmd.getInvoke().toLowerCase());
            if (cmd instanceof SpecialCommand)
                YukiLogger.log(new YukiLogInfo(" - Special command " + cmd.getInvoke().toLowerCase() + " added!").debug());
            else
                YukiLogger.log(new YukiLogInfo(" - Command " + cmd.getInvoke().toLowerCase() + " added!").debug());
        }
    }

    public void registerCommands(YukiSora yukiSora) {
        List<CommandData> publicCommands = new ArrayList<>();
        List<CommandData> adminCommands = new ArrayList<>();
        for (String s : commandIvokes) {
            DiscCommand cmd = commands.get(s);
            if (!cmd.isCreateSlashCommand())
                continue;

            CommandData data = cmd.toCommandData();
            if (data != null) {
                if (cmd.isAdminOnlyCommand())
                    adminCommands.add(data);
                else
                    publicCommands.add(data);
            }
        }

        for (CommandData adminCommand : adminCommands) {
            adminCommand.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        }

        List<CommandData> joins = new ArrayList<>(adminCommands);
        joins.addAll(publicCommands);

        yukiSora.getDiscordApplication().getBotJDA().updateCommands().addCommands(joins).queue();
    }

    private class CommandHandlingReport {
        private boolean invalid;
        private ArrayList<String> errors;

        private SubCommand foundCommand;
        private SubCommandGroup foundGroup;

        public CommandHandlingReport() {
            errors = new ArrayList<>();
        }

        public String build() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                builder.append("> " + errors.get(i));
                if (i != errors.size() - 1)
                    builder.append("\n");
            }
            return builder.toString();
        }

        public ArrayList<String> getErrors() {
            return errors;
        }

        public void setErrors(ArrayList<String> errors) {
            this.errors = errors;
        }

        public void addError(String er) {
            errors.add(er);
        }

        public SubCommand getFoundCommand() {
            return foundCommand;
        }

        public void setFoundCommand(SubCommand foundCommand) {
            this.foundCommand = foundCommand;
        }

        public SubCommandGroup getFoundGroup() {
            return foundGroup;
        }

        public void setFoundGroup(SubCommandGroup foundGroup) {
            this.foundGroup = foundGroup;
        }

        @Override
        public String toString() {
            return "CommandHandlingReport{" +
                    "invalid=" + invalid +
                    ", errors=" + errors +
                    ", foundCommand=" + foundCommand +
                    ", foundGroup=" + foundGroup +
                    '}';
        }
    }
}