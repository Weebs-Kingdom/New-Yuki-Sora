package de.mindcollaps.yuki.core;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.console.terminal.YukiTerminal;

@YukiLogModule(name = "MAIN")
public class Main {

    public static void main(String[] args) {
        YukiLogger.setup();

        YukiLogger.log(new YukiLogInfo("Logger initialized").debug());
        YukiSora yukiSora = new YukiSora();

        YukiTerminal.setup(yukiSora);

        yukiSora.boot(args);
    }
}
