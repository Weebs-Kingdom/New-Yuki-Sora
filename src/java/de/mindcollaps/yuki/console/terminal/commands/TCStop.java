package de.mindcollaps.yuki.console.terminal.commands;

import de.mindcollaps.yuki.core.YukiSora;

public class TCStop implements TerminalCommand{

    private YukiSora yukiSora;

    public TCStop(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public String onExecute(String[] args) {
        yukiSora.shutdown();
        return "Shutting down";
    }

    @Override
    public String getCommandInvoke() {
        return "stop";
    }

    @Override
    public String help() {
        return "This shuts down the bot...lol?";
    }
}
