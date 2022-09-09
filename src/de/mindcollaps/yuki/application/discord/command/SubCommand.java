package de.mindcollaps.yuki.application.discord.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.utils.Checks;

import java.util.ArrayList;

public class SubCommand {

    private final String invoke;
    private final String description;
    private final ArrayList<CommandOption> options;
    private CommandAction action;

    public SubCommand(String invoke, String description) {
        options = new ArrayList<>();
        this.invoke = invoke;
        this.description = description;
    }

    public SubcommandData toSubCommandData() {
        SubcommandData data = new SubcommandData(invoke, description);
        for (CommandOption option : options) {
            data.addOptions(option.toOptionData());
        }
        return data;
    }

    public SubCommand addOption(CommandOption... options) {
        Checks.noneNull(options, "Option");
        Checks.check(options.length + this.options.size() <= 25, "Cannot have more than 25 options for a subcommand!");
        for (CommandOption option : options) {
            /*
            Checks.check(option.getType() != OptionType.SUB_COMMAND, "Cannot add a subcommand to a subcommand!");
            Checks.check(option.getType() != OptionType.SUB_COMMAND_GROUP, "Cannot add a subcommand group to a subcommand!");
             */
            this.options.add(option);
        }
        return this;
    }

    public SubCommand addOption(OptionType optionType, String name, String description) {
        Checks.noneNull(options, "Option");
        Checks.check(1 + this.options.size() <= 25, "Cannot have more than 25 options for a subcommand!");
        /*
        Checks.check(optionType != OptionType.SUB_COMMAND, "Cannot add a subcommand to a subcommand!");
        Checks.check(optionType != OptionType.SUB_COMMAND_GROUP, "Cannot add a subcommand group to a subcommand!");
         */
        this.options.add(new CommandOption(optionType, name, description));
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

    public SubCommand addAction(CommandAction action) {
        this.action = action;
        return this;
    }

    public ArrayList<CommandOption> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "SubCommand{" +
                "invoke='" + invoke + '\'' +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", action=" + action +
                '}';
    }
}
