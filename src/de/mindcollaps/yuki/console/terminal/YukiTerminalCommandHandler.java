package de.mindcollaps.yuki.console.terminal;

import de.mindcollaps.yuki.console.terminal.commands.TerminalCommand;

public class YukiTerminalCommandHandler {

    public static String handleCommand(String raw) {
        if (raw.length() == 0)
            return "";
        String[] args = raw.split(" ");
        if (args.length < 1)
            return "No command argument was found!";

        String invoke = args[0];

        for (TerminalCommand terminalCommand : YukiTerminal.getTerminalCommands()) {
            if (terminalCommand.getCommandInvoke().equalsIgnoreCase(invoke)) {
                return terminalCommand.onExecute(args);
            }
        }

        return "No command " + invoke + " was found! Try to use help to help yourself :D";
    }
}
