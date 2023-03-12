package de.mindcollaps.yuki.console.terminal.commands;

import de.mindcollaps.yuki.core.YukiProperties;

public class TCDebug implements TerminalCommand {
    @Override
    public String onExecute(String[] args) {
        if (args.length > 1) {
            switch (args[1].toLowerCase()) {
                case "true" -> {
                    YukiProperties.getApplicationSettings().fineDebug = true;
                    return "Changed property fineDebug to true";
                }
                case "false" -> {
                    YukiProperties.getApplicationSettings().fineDebug = false;
                    return "Changed property fineDebug to false";
                }
                default -> {
                    return "Please typ true or false as new value for fineDebug";
                }
            }
        } else {
            return "You need to specify weather the debug is true or false";
        }
    }

    @Override
    public String getCommandInvoke() {
        return "debug";
    }

    @Override
    public String help() {
        return "With this command you can change the fine debug. Fine debug shows all network traffic and all commands issued.";
    }
}
