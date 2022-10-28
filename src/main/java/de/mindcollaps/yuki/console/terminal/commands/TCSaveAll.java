package de.mindcollaps.yuki.console.terminal.commands;

import de.mindcollaps.yuki.core.YukiProperties;

public class TCSaveAll implements TerminalCommand{
    @Override
    public String onExecute(String[] args) {
        YukiProperties.saveBotProperties();
        YukiProperties.saveApplicationSettings();
        return "Saved application settings and bot properties!";
    }

    @Override
    public String getCommandInvoke() {
        return "save";
    }

    @Override
    public String help() {
        return "Saves the bot properties and application settings";
    }
}
