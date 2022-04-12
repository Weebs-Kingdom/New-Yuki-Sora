package de.mindcollaps.yuki.console.log;

import org.apache.logging.log4j.Level;

public class YukiLogInfo {

    private String message;
    private String module;
    private Exception exception;
    private Level level;

    public YukiLogInfo(String message, String module) {
        this.message = message;
        this.module = module;
        this.exception = null;
        this.level = Level.INFO;
    }

    public YukiLogInfo(String message) {
        this.message = message;
        this.module = null;
        this.exception = null;
        this.level = Level.INFO;
    }

    public YukiLogInfo level(Level level){
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    @Override
    public String toString() {
        StringBuilder stack = new StringBuilder();
        if (level == Level.ERROR) {
            if (exception != null) {
                if (exception.getCause() != null)
                    stack.append(exception.getCause().getMessage());
                for (StackTraceElement traceElement : exception.getStackTrace()) {
                    stack.append(traceElement.toString() + "\n");
                }
            } else {
                stack.append("Stack Trace is empty!");
            }
            return
                    "\nModul: " + module +
                            "\nMessage: " + message +
                            "\nStack trace: \n" + stack
                    ;
        }
        if (module != null)
            return "[" + module + "] " + message;
        else
            return message;
    }
}
