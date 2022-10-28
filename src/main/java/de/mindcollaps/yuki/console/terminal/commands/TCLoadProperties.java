package de.mindcollaps.yuki.console.terminal.commands;

import de.mindcollaps.yuki.core.YukiProperties;

public class TCLoadProperties implements TerminalCommand{
    @Override
    public String onExecute(String[] args) {
        YukiProperties.loadBotProperties();
        return "Reloaded the bot properties";
    }

    @Override
    public String getCommandInvoke() {
        return "lproperties";
    }

    @Override
    public String help() {
        return "Reloads the properties file";
    }
}
