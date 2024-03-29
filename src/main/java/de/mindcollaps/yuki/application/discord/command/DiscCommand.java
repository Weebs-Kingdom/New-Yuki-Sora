package de.mindcollaps.yuki.application.discord.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.utils.Checks;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class DiscCommand {

    private final String invoke;
    private final String description;
    private final ArrayList<CommandOption> options;
    private final ArrayList<SubCommand> subCommands;

    private final ArrayList<SubCommandGroup> subCommandGroups;
    private CommandAction action;

    private boolean createSlashCommand = true;
    private boolean adminOnlyCommand = false;

    private boolean isCallableSlash = true;
    private boolean isCallableServer = true;
    private boolean isCallableClient = true;

    public DiscCommand(String invoke, String description) {
        this.invoke = invoke;
        this.description = description;
        subCommands = new ArrayList<>();
        options = new ArrayList<>();
        subCommandGroups = new ArrayList<>();
    }

    @SuppressWarnings("SameReturnValue")
    public abstract String getHelp();

    public CommandData toCommandData() {
        SlashCommandData data = Commands.slash(invoke, description);
        for (SubCommand subCommand : subCommands) {
            data.addSubcommands(subCommand.toSubCommandData());
        }

        for (SubCommandGroup subCommandGroup : subCommandGroups) {
            data.addSubcommandGroups(subCommandGroup.toSubCommandGroupData());
        }

        return data;
    }

    @SuppressWarnings("UnusedReturnValue")
    public DiscCommand addSubcommands(SubCommand... commands) {
        Checks.noneNull(commands, "Subcommands");
        if (commands.length == 0)
            return this;

        boolean allowOption = false;
        Checks.check(commands.length + subCommands.size() <= 25, "Cannot have more than 25 subcommands for a command!");

        subCommands.addAll(Arrays.asList(commands));

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public DiscCommand addSubcommands(SubCommandGroup... commands) {
        Checks.noneNull(commands, "Subcommands");
        if (commands.length == 0)
            return this;

        boolean allowOption = false;
        Checks.check(commands.length + subCommands.size() <= 25, "Cannot have more than 25 subcommands for a command!");

        subCommandGroups.addAll(Arrays.asList(commands));

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public DiscCommand addOption(CommandOption... options) {
        Checks.noneNull(options, "Option");
        Checks.check(options.length + this.options.size() <= 25, "Cannot have more than 25 options for a subcommand!");
        for (CommandOption option : options) {
            Checks.check(option.getType() != OptionType.SUB_COMMAND, "Cannot add a subcommand to a subcommand!");
            Checks.check(option.getType() != OptionType.SUB_COMMAND_GROUP, "Cannot add a subcommand group to a subcommand!");
            this.options.add(option);
        }
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public DiscCommand noCallClient() {
        this.isCallableClient = false;
        return this;
    }

    public DiscCommand noCallServer() {
        this.isCallableServer = false;
        return this;
    }

    public DiscCommand noCallSlash() {
        this.isCallableSlash = false;
        return this;
    }

    public String getInvoke() {
        return invoke;
    }

    public String getDescription() {
        return description;
    }

    public CommandAction getAction() {
        return action;
    }

    public ArrayList<CommandOption> getOptions() {
        return options;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    @SuppressWarnings("UnusedReturnValue")
    public DiscCommand addAction(CommandAction action) {
        this.action = action;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public DiscCommand adminOnlyCommand() {
        this.adminOnlyCommand = true;
        return this;
    }

    public void setAdminOnlyCommand() {
        this.adminOnlyCommand = true;
    }

    public boolean isAdminOnlyCommand() {
        return adminOnlyCommand;
    }

    public void doNotCreateSlashCommand() {
        this.createSlashCommand = false;
    }

    public ArrayList<SubCommandGroup> getSubCommandGroups() {
        return subCommandGroups;
    }

    public boolean isCreateSlashCommand() {
        return createSlashCommand;
    }

    public boolean isCallableSlash() {
        return isCallableSlash;
    }

    public boolean isCallableServer() {
        return isCallableServer;
    }

    public boolean isCallableClient() {
        return isCallableClient;
    }

    @Override
    public String toString() {
        return "DiscCommand{" +
                "invoke='" + invoke + '\'' +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", subCommands=" + subCommands +
                ", subCommandGroups=" + subCommandGroups +
                ", action=" + action +
                ", createSlashCommand=" + createSlashCommand +
                ", adminOnlyCommand=" + adminOnlyCommand +
                '}';
    }
}
