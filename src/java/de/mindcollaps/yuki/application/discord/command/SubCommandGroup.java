package de.mindcollaps.yuki.application.discord.command;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.Arrays;

public class SubCommandGroup {

    private final String invoke;
    private final String description;
    private final ArrayList<SubCommand> subCommands;

    public SubCommandGroup(String invoke, String description) {
        this.invoke = invoke;
        this.description = description;
        this.subCommands = new ArrayList<>();
    }

    public SubCommandGroup addSubCommands(SubCommand... commands) {
        subCommands.addAll(Arrays.asList(commands));

        return this;
    }

    public SubcommandGroupData toSubCommandGroupData() {
        SubcommandGroupData data = new SubcommandGroupData(invoke, description);
        for (SubCommand subCommand : subCommands) {
            data.addSubcommands(subCommand.toSubCommandData());
        }

        return data;
    }

    public String getInvoke() {
        return invoke;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public String toString() {
        return "SubCommandGroup{" +
                "invoke='" + invoke + '\'' +
                ", description='" + description + '\'' +
                ", subCommands=" + subCommands +
                '}';
    }
}
