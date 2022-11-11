package de.mindcollaps.yuki.console.terminal;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.console.terminal.commands.TerminalCommand;

@YukiLogModule(name = "Terminal Command Handler")
public class YukiTerminalCommandHandler {

    public static String handleCommand(String raw) {
        YukiLogger.log(new YukiLogInfo("Terminal command handler handles command: " + raw).debug());
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
