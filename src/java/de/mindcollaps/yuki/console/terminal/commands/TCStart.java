package de.mindcollaps.yuki.console.terminal.commands;

import de.mindcollaps.yuki.core.YukiSora;

public class TCStart implements TerminalCommand {

    private final YukiSora yukiSora;

    public TCStart(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public String onExecute(String[] args) {
        yukiSora.getDiscordApplication().boot();
        return "";
    }

    @Override
    public String getCommandInvoke() {
        return "start";
    }

    @Override
    public String help() {
        return "Starts the Discord Bot";
    }
}
