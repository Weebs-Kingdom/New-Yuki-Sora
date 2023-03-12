package de.mindcollaps.yuki.console.terminal;

import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.terminal.commands.*;
import de.mindcollaps.yuki.core.YukiSora;

@YukiLogModule(name = "Terminal")
public class YukiTerminal {

    private static final boolean setupDone = false;

    private static TerminalCommand[] terminalCommands;

    public static void setup(YukiSora yukiSora) {
        if (setupDone)
            return;

        setupCommands(yukiSora);
        setupListener();
    }

    private static void setupCommands(YukiSora yukiSora) {
        terminalCommands = new TerminalCommand[]{
                new TCList(),
                new TCStart(yukiSora),
                new TCLoadProperties(),
                new TCSaveAll(),
                new TCDebug(),
                new TCStop(yukiSora)
        };
    }

    private static void setupListener() {
        new Thread(new YukiTerminalCommandHandlerThread()).start();
    }

    public static TerminalCommand[] getTerminalCommands() {
        return terminalCommands;
    }
}
