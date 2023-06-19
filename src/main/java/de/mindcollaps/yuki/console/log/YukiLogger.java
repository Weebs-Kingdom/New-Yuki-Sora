package de.mindcollaps.yuki.console.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@YukiLogModule(name = "Yuki Logger")
public class YukiLogger {

    private static final Logger logger = LogManager.getLogger("de.mindcollaps.yuki");
    private static boolean setupDone = false;

    public static void setup() {
        if (setupDone)
            return;

        setupDone = true;
    }

    public static void log(YukiLogInfo info) {
        if (info.getLevel() == Level.INFO) {
            logger.log(info.getLevel(), info);
        } else {
            LogManager.getRootLogger().log(info.getLevel(), info);
        }
    }
}