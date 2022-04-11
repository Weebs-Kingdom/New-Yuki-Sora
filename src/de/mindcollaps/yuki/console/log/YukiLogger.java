package yuki.console.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class YukiLogger {

    private static boolean setupDone = false;

    public static Logger logger;
    public static Logger debug;


    public static void setup() throws IOException {
        if (setupDone)
            return;
        setupDone = true;
        logger = LogManager.getLogger("de.mindcollaps.yuki");
        debug = LogManager.getLogger("debug");
    }

    public static void log(YukiLogInfo info) {
        if (info.getLevel().intLevel() <= 400) {
            logger.log(info.getLevel(), info.getMessage());
        } else {
            debug.log(info.getLevel(), info.getMessage());
            if(info.getLevel() == Level.TRACE)
                debug.log(info.getLevel(), getExceptionText(info));
        }
    }

    private static String getExceptionText(YukiLogInfo info){
        StringBuilder b = new StringBuilder();
        b.append("We found and error ;_;\n");
        b.append(info.getException());
        for (StackTraceElement traceElement : info.getException().getStackTrace())
            b.append("\n\tat ").append(traceElement);

        return b.toString();
    }
}