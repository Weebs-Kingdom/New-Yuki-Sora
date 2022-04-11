package yuki.application.discord.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandOption {

    private OptionType optionType;
    private String name;
    private String description;

    public CommandOption(OptionType optionType, String name, String description) {
        this.optionType = optionType;
        this.name = name;
        this.description = description;
    }

    public OptionData toOptionData(){
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
}
