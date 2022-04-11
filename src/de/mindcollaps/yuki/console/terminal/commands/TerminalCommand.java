package yuki.console.terminal.commands;

public interface TerminalCommand {

    public String onExecute(String[] args);
    public String getCommandInvoke();
    public String help();
}
