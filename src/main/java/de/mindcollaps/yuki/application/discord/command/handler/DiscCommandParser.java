package de.mindcollaps.yuki.application.discord.command.handler;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;

public class DiscCommandParser {

    public static ServerCommandContainer parseServerMessage(String raw, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {

        String beheaded = raw.replaceFirst(YukiProperties.getProperties(YukiProperties.dPDefaultCommandPrefix), "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        Collections.addAll(split, splitBeheaded);

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new ServerCommandContainer(raw, beheaded, splitBeheaded, invoke, args, event, server, user, yukiSora);
    }

    public static SlashCommandContainer parseSlashMessage(String raw, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
        String beheaded = raw.replaceFirst("/", "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        Collections.addAll(split, splitBeheaded);

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new SlashCommandContainer(raw, beheaded, splitBeheaded, invoke, args, event, server, user, yukiSora);
    }

    public static ClientCommandContainer parseClientMessage(String raw, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {

        String beheaded = raw.replaceFirst(YukiProperties.getProperties(YukiProperties.dPDefaultCommandPrefix), "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];
        ArrayList<String> split = new ArrayList<>();
        Collections.addAll(split, splitBeheaded);

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

        public GenericCommandContainer getGeneric() {
            return new GenericCommandContainer(raw, beheaded, splitBeheaded, invoke, args, user, yukiSora, event.getChannel());
        }
    }

    public static class SlashCommandContainer {

        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final SlashCommandInteractionEvent event;
        public final DiscApplicationServer server;
        public final DiscApplicationUser user;
        public final YukiSora yukiSora;

        public SlashCommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
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

        public GenericCommandContainer getGeneric() {
            return new GenericCommandContainer(raw, beheaded, splitBeheaded, invoke, args, user, yukiSora, event.getChannel());
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

        public GenericCommandContainer getGeneric() {
            return new GenericCommandContainer(raw, beheaded, splitBeheaded, invoke, args, user, yukiSora, event.getChannel());
        }
    }

    public static class GenericCommandContainer {
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final DiscApplicationUser user;
        public final YukiSora yukiSora;

        public final MessageChannel messageChannel;

        public GenericCommandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, DiscApplicationUser user, YukiSora yukiSora, MessageChannel messageChannel) {
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.user = user;
            this.yukiSora = yukiSora;
            this.messageChannel = messageChannel;
        }
    }
}
