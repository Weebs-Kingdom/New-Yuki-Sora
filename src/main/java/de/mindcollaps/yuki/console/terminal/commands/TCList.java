package de.mindcollaps.yuki.console.terminal.commands;

import de.mindcollaps.yuki.console.terminal.YukiTerminal;

public class TCList implements TerminalCommand {
    @Override
    public String onExecute(String[] args) {
        StringBuilder list = new StringBuilder("Command List:\n");
        for (TerminalCommand terminalCommand : YukiTerminal.getTerminalCommands()) {
            list.append(terminalCommand.getCommandInvoke()).append("\n");
        }

        return list.toString();
    }

    @Override
    public String getCommandInvoke() {
        return "list";
    }

    @Override
    public String help() {
        return "This command shows all the other commands";
    }
}
