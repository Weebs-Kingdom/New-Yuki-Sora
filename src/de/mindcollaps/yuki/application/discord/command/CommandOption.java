package de.mindcollaps.yuki.application.discord.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandOption {

    private final OptionType optionType;
    private final String name;
    private final String description;

    public CommandOption(OptionType optionType, String name, String description) {
        this.optionType = optionType;
        this.name = name;
        this.description = description;
    }

    public OptionData toOptionData() {
        return new OptionData(optionType, name, description);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public OptionType getType() {
        return optionType;
    }

    public SubcommandData toSubCommandData() {
        return new SubcommandData(name, description);
    }
}
