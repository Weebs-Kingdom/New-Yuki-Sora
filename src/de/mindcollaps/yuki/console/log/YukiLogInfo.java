package de.mindcollaps.yuki.console.log;

import org.apache.logging.log4j.Level;

public class YukiLogInfo {

    private String message;
    private String origin;
    private Exception exception;
    private Level level;

    public YukiLogInfo(String message, String module) {
        this.message = message;
        this.origin = module;
        this.exception = null;
        this.level = Level.INFO;
    }

    public YukiLogInfo(String message) {
        String origin = "";
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        origin = stackTraceElements[stackTraceElements.length -1].getClassName();
        int min = -2;
        while (origin.equals("java.lang.Thread") ||origin.equals("java.util.TimerThread")){
            origin = stackTraceElements[stackTraceElements.length + min].getClassName();
            min --;
        }

        this.message = message;
        this.origin = origin;
        this.exception = null;
        this.level = Level.INFO;
    }

    public YukiLogInfo level(Level level) {
        this.level = level;
        return this;
    }

    public YukiLogInfo warning() {
        this.level = Level.WARN;
        return this;
    }

    public YukiLogInfo debug() {
        this.level = Level.DEBUG;
        return this;
    }

    public YukiLogInfo trace(Exception e) {
        this.level = Level.TRACE;
        this.exception = e;
        return this;
    }

    public YukiLogInfo error() {
        this.level = Level.ERROR;
        return this;
    }

    public YukiLogInfo log() {
        YukiLogger.log(this);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Level getLevel() {
        return level;
    }

    private String getExceptionText() {
        StringBuilder b = new StringBuilder();
        //b.append("We found and error ;_;\n");
        b.append(exception);
        for (StackTraceElement traceElement : exception.getStackTrace())
            b.append("\n\tat ").append(traceElement);

        return b.toString();
    }

    @Override
    public String toString() {
        String stack = "";
        if (level == Level.ERROR) {
            if (exception != null) {
                stack = getExceptionText();
            } else {
                stack = "Stack Trace is empty!";
            }
            return
                    "!We found an error ;_;!\n--------\nOrigin: " + origin +
                            "\nMessage: " + message +
                            "\nStack trace: \n" + stack + "\n--------"
                    ;
        }
        if (origin != null && level.isInRange(Level.DEBUG, Level.TRACE))
            return "[" + origin + "] " + message;
        else
            return message;
    }
}
