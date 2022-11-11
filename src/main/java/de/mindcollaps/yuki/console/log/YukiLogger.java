package de.mindcollaps.yuki.console.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class YukiLogger {

    public static Logger logger;
    public static Logger debug;
    private static boolean setupDone = false;

    public static void setup() {
        if (setupDone)
            return;
        setupDone = true;
        logger = LogManager.getLogger("de.mindcollaps.yuki");
        debug = LogManager.getLogger("debug");
    }

    public static void log(YukiLogInfo info) {
        if (info.getLevel() == Level.INFO) {
            logger.log(info.getLevel(), info);
        } else {
            debug.log(info.getLevel(), info);
        }
    }
}