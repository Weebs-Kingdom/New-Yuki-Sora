package de.mindcollaps.yuki.application.discord.command.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import de.mindcollaps.yuki.application.discord.command.CommandAction;
import de.mindcollaps.yuki.application.discord.command.CommandOption;
import de.mindcollaps.yuki.application.discord.command.DiscCommand;
import de.mindcollaps.yuki.application.discord.command.SubCommand;
import de.mindcollaps.yuki.application.discord.core.DiscordApplication;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscCommandHandler {

    private final String consMsgDef = "[Command Handler]";

    private final DiscordApplication application;
    public HashMap<String, DiscCommand> commands = new HashMap<>();
    public ArrayList<String> commandIvokes = new ArrayList<>();

    public DiscCommandHandler(DiscordApplication application) {
        this.application = application;
    }

    private DiscCommand findCommand(String invoke) {
        return commands.get(invoke);
    }

    private CommandAction findAction(DiscCommand command, String[] args, CommandHandlingReport report) {
        if (command.getAction() != null)
            return command.getAction();

        if (checkCommandOptions(command.getOptions().toArray(new CommandOption[0]), args, 0, report))
            return command.getAction();

        SubCommand subCommand = checkSubCommands(command.getSubCommands().toArray(new SubCommand[0]), args, 0, report);
        if (subCommand == null)
            return null;
        report.setFoundCommand(subCommand);


        return subCommand.getAction();
    }

    private SubCommand checkSubCommands(SubCommand[] commands, String[] args, int checkI, CommandHandlingReport report) {
        if (args.length - 1 >= checkI)
            for (SubCommand command : commands) {
                if (command.getInvoke().equalsIgnoreCase(args[checkI])) {
                    report.foundCommand = command;
                    if (checkCommandOptions(command.getOptions().toArray(new CommandOption[0]), args, checkI + 1, report))
                        return command;
                }
            }
        return null;
    }

    private boolean checkCommandOptions(CommandOption[] options, String[] args, int checkI, CommandHandlingReport report) {
        for (int i = 0; i < options.length; i++) {
            //If options is a sub command
            if (args.length - 1 > checkI)
                if (options[i].getType() == OptionType.SUB_COMMAND)
                    return args[checkI].equalsIgnoreCase(options[i].getName());
                else continue;
            else return false;
        }
        return false;
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

    private void sendHelp(DiscCommand command, SubCommand subCommand, MessageChannel messageChannel) {
        String help = command.getHelp();
        if (help == null) {
            if (subCommand == null) {
                messageChannel.sendMessageEmbeds(buildHelp(command).build()).queue();
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

    private EmbedBuilder buildHelp(DiscCommand command) {
        StringBuilder builder = new StringBuilder("```\n").append(command.getDescription()).append("\n```\n\n");
        ArrayList<String> commandsAdded = new ArrayList<>();

        if (command.getAction() == null) {
            builder.append("List of subcommands:\n");
            for (SubCommand subCommand : command.getSubCommands()) {
                if (commandsAdded.contains(subCommand.getInvoke()))
                    continue;

                builder.append("- ").append(subCommand.getInvoke()).append("\n");
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
        StringBuilder builder = new StringBuilder("```\n").append(command.getDescription()).append("\n```\n\n");

        ArrayList<SubCommand> commands = new ArrayList<>();
        for (SubCommand subCommand : rcommand.getSubCommands()) {
            if (subCommand.getInvoke().equalsIgnoreCase(command.getInvoke()))
                commands.add(subCommand);
        }

        for (SubCommand subCommand : commands) {
            builder.append("**").append(subCommand.getInvoke()).append("**").append(" ");
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
                    User user = message.getMentionedUsers().get(users);
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
                    MessageChannel channel = message.getMentionedChannels().get(channels);
                    if (channel == null)
                        channelError = true;
                    else
                        argument.setMessageChannel(channel);
                } catch (Exception e) {
                    channelError = true;
                }
                if (channelError) {
                    channelError = false;
                    try {
                        MessageChannel channel = yukiSora.getDiscordApplication().getBotJDA().getTextChannelById(arg);
                        if (channel == null)
                            channelError = true;
                        else
                            argument.setMessageChannel(channel);
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
                    Role role = message.getMentionedRoles().get(roles);
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
                    sendHelp(command, report.foundCommand, cmd.event.getChannel());
                    return;
                } else if (cmd.args.length > 1)
                    if (cmd.args[1].equalsIgnoreCase("help")) {
                        sendHelp(command, report.foundCommand, cmd.event.getChannel());
                        return;
                    }

            DiscCommandArgs args = generateCommandArgs(report, command, cmd.args, cmd.event.getChannel(), cmd.event.getMessage(), cmd.yukiSora);
            boolean exe = false;

            if (report.errors.size() > 0 || report.invalid || action == null) {
                if (report.foundCommand == null || report.errors.size() > 0)
                    printErrorMessage("Please check the validity of your command arguments\n\n" + report.build() + "\nUse **-" + command.getInvoke() + " help** if you need further details", cmd.event.getChannel());
                else
                    sendHelp(command, report.foundCommand, cmd.event.getChannel());
                return;
            }

            try {
                exe = action.calledServer(args, cmd.event, cmd.server, cmd.user, cmd.yukiSora);
            } catch (Exception e) {
                YukiLogger.log(new YukiLogInfo("Handle server command had an error while checking if the command can be called!", consMsgDef).trace(e));
            }

            if (exe) {
                try {
                    action.actionServer(args, cmd.event, cmd.server, cmd.user, cmd.yukiSora);
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle server command had an error while performing the command action", consMsgDef).trace(e));
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
                    sendHelp(command, report.foundCommand, cmd.event.getChannel());
                    return;
                } else if (cmd.args.length > 1)
                    if (cmd.args[1].equalsIgnoreCase("help")) {
                        sendHelp(command, report.foundCommand, cmd.event.getChannel());
                        return;
                    }

            DiscCommandArgs args = generateCommandArgs(report, command, cmd.args, cmd.event.getChannel(), cmd.event.getMessage(), cmd.yukiSora);
            boolean exe = false;

            if (report.errors.size() > 0 || report.invalid  || action == null) {
                if (report.foundCommand == null || report.errors.size() > 0)
                    printErrorMessage("Please check the validity of your command arguments!\n\n" + report.build() + "\nUse **-" + command.getInvoke() + " help** if you need further details", cmd.event.getChannel());
                else
                    sendHelp(command, report.foundCommand, cmd.event.getChannel());
                return;
            }

            try {
                exe = action.calledPrivate(args, cmd.event, cmd.user, cmd.yukiSora);
            } catch (Exception e) {
                YukiLogger.log(new YukiLogInfo("Handle client command had an error while checking if the command can be called!", consMsgDef).trace(e));
            }

            if (exe) {
                try {
                    action.actionPrivate(args, cmd.event, cmd.user, cmd.yukiSora);
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Handle client command had an error while performing the command action", consMsgDef).trace(e));
                }
            }

        }
    }

    public void createNewCommand(DiscCommand cmd) {
        if (commandIvokes.contains(cmd.getInvoke().toLowerCase())) {
            YukiLogger.log(new YukiLogInfo("Command " + cmd.getInvoke().toLowerCase() + " already exist!").debug());
        } else {
            commands.put(cmd.getInvoke().toLowerCase(), cmd);
            commandIvokes.add(cmd.getInvoke().toLowerCase());
            YukiLogger.log(new YukiLogInfo("Command " + cmd.getInvoke().toLowerCase() + " added!").debug());
        }
    }

    public void registerCommands(YukiSora yukiSora) {
        ArrayList<CommandData> list = new ArrayList<>();
        for (String s : commandIvokes) {
            DiscCommand cmd = commands.get(s);
            if (!cmd.isCreateSlashCommands())
                continue;
            CommandData data = cmd.toCommandData();
            if (data != null)
                list.add(data);
        }
        yukiSora.getDiscordApplication().getBotJDA().updateCommands().addCommands(list).queue();
    }

    private class CommandHandlingReport {
        private boolean invalid;
        private ArrayList<String> errors;

        private SubCommand foundCommand;

        public CommandHandlingReport() {
            errors = new ArrayList<>();
        }

        public String build() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                builder.append(errors.get(i));
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
    }
}