package de.mindcollaps.yuki.application.discord.command.special;

import de.mindcollaps.yuki.application.discord.command.DiscCommand;

public abstract class SpecialCommand extends DiscCommand {


    public SpecialCommand(String invoke, String description) {
        super(invoke, description);

        doNotCreateSlashCommand();
        noCallSlash();
    }
}
