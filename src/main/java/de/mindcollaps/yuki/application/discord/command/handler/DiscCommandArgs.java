package de.mindcollaps.yuki.application.discord.command.handler;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscCommandArgs {

    private final HashMap<String, DiscCommandArgument> args;
    private final ArrayList<DiscCommandArgument> array;
    private final String[] nativeArgs;

    public DiscCommandArgs(String[] args) {
        this.nativeArgs = args;
        this.args = new HashMap<>();
        this.array = new ArrayList<>();
    }

    public void addCommandArgument(DiscCommandArgument argument) {
        args.put(argument.getName().toLowerCase(), argument);
        array.add(argument);
    }

    public DiscCommandArgument getArg(String name) {
        name = name.toLowerCase();
        if (!args.containsKey(name))
            return null;
        return args.get(name);
    }

    public DiscCommandArgument getArg(int i) {
        return array.get(i);
    }

    public ArrayList<DiscCommandArgument> getAsArray() {
        return new ArrayList<>(array);
    }

    public String[] getNativeArgs() {
        return nativeArgs;
    }
}
