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
    private CommandAction action;
    private final boolean createSlashCommands;

    public DiscCommand(String invoke, String description, boolean createSlashCommands) {
        this.invoke = invoke;
        this.description = description;
        this.createSlashCommands = createSlashCommands;
        subCommands = new ArrayList<>();
        options = new ArrayList<>();
    }

    public abstract String getHelp();

    public CommandData toCommandData() {
        SlashCommandData data = Commands.slash(invoke, description);
        for (SubCommand subCommand : subCommands) {
            data.addSubcommands(subCommand.toSubCommandData());
        }

        return data;
    }

    public DiscCommand addSubcommands(SubCommand... commands) {
        Checks.noneNull(commands, "Subcommands");
        if (commands.length == 0)
            return this;

        boolean allowOption = false;
        Checks.check(commands.length + subCommands.size() <= 25, "Cannot have more than 25 subcommands for a command!");

        subCommands.addAll(Arrays.asList(commands));

        return this;
    }

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

    public boolean isCreateSlashCommands() {
        return createSlashCommands;
    }

    public DiscCommand addAction(CommandAction action) {
        this.action = action;
        return this;
    }
}
