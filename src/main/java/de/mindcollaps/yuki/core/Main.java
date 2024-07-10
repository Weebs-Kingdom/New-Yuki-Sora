package de.mindcollaps.yuki.core;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.console.terminal.YukiTerminal;

@YukiLogModule(name = "MAIN")
public class Main {

    private static boolean terminalDisabled = false;

    public static void main(String[] args) {
        YukiLogger.setup();

        handlePreArgs(args);

        YukiLogger.log(new YukiLogInfo("Logger initialized").debug());
        YukiSora yukiSora = new YukiSora();

        if (!terminalDisabled)
            YukiTerminal.setup(yukiSora);

        yukiSora.boot(args);
    }

    private static void handlePreArgs(String[] args) {
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "docker" -> {
                    terminalDisabled = true;
                }
            }
        }
    }
}