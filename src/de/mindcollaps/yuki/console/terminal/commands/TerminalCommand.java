package de.mindcollaps.yuki.console.terminal.commands;

public interface TerminalCommand {

    String onExecute(String[] args);
    String getCommandInvoke();
    String help();
}
