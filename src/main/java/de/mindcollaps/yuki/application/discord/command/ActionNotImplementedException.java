package de.mindcollaps.yuki.application.discord.command;

public class ActionNotImplementedException extends Exception {

    public ActionNotImplementedException() {
        super("The action is not implemented!");
    }
}
