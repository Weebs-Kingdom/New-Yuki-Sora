package de.mindcollaps.yuki.application.discord.command.handler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;

import java.util.ArrayList;

public class DiscCommandParser {

    public static ServerCommandContainer parseServerMessage(String raw, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {

        String beheaded = raw.replaceFirst(YukiProperties.getProperties(YukiProperties.dPDefaultCommandPrefix), "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        for (String s : splitBeheaded) {
            split.add(s);
        }

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new ServerCommandContainer(raw, beheaded, splitBeheaded, invoke, args, event, server, user, yukiSora);
    }

    public static ClientCommandContainer parseClientMessage(String raw, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {

        String beheaded = raw.replaceFirst(YukiProperties.getProperties(YukiProperties.dPDefaultCommandPrefix), "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        for (String s : splitBeheaded) {
            split.add(s);
        }

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new ClientCommandContainer(raw, beheaded, splitBeheaded, invoke, args, event, user, yukiSora);
    }


    public static class ServerCommandContainer {

        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;
        public final DiscApplicationServer server;
        public final DiscApplicationUser user;
        public final YukiSora yukiSora;

        public ServerCommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
            this.server = server;
            this.user = user;
            this.yukiSora = yukiSora;
        }

    }

    public static class ClientCommandContainer {
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;
        public final DiscApplicationUser user;
        public final YukiSora yukiSora;

        public ClientCommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
            this.user = user;
            this.yukiSora = yukiSora;
        }
    }
}
