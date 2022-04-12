package de.mindcollaps.yuki.application.discord.command.handler;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscCommandArgs {

    private final HashMap<String, DiscCommandArgument> args;
    private final ArrayList<DiscCommandArgument> array;

    public DiscCommandArgs() {
        args = new HashMap<>();
        array = new ArrayList<>();
    }

    public void addCommandArgument(DiscCommandArgument argument){
        args.put(argument.getName().toLowerCase(), argument);
        array.add(argument);
    }

    public DiscCommandArgument getArg(String name){
        name = name.toLowerCase();
        return args.get(name);
    }

    public DiscCommandArgument getArg(int i){
        return array.get(i);
    }

    public ArrayList<DiscCommandArgument> getArray() {
        return new ArrayList<>(array);
    }
}
