package de.mindcollaps.yuki.core;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.console.terminal.YukiTerminal;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            YukiLogger.setup();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't setup the Logger, exiting");
            System.exit(-1);
        }

        YukiLogger.log(new YukiLogInfo("Logger initialized"));
        YukiSora yukiSora = new YukiSora();

        YukiTerminal.setup(yukiSora);

        yukiSora.boot(args);
    }
}
