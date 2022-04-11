package yuki.console.terminal.commands;

import yuki.core.YukiProperties;
import yuki.core.YukiSora;

public class TCStart implements TerminalCommand{

    private YukiSora yukiSora;

    public TCStart(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public String onExecute(String[] args) {
        yukiSora.getDiscordApplication().boot();
        return "Started Discord Application";
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
