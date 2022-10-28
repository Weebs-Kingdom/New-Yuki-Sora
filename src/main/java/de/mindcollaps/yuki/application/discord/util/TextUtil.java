package de.mindcollaps.yuki.application.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.util.List;

public class TextUtil {

    public static void sendNormalTxt(String txt, MessageChannel channel) {
        channel.sendMessage(txt).queue();
    }

    public static void sendMessageEmbed(MessageEmbed msg, ResponseInstance res) {
        if (res.isChannel)
            res.getChannel().sendMessageEmbeds(msg).queue();
        else
            res.getInteraction().replyEmbeds(msg).queue();
    }

    public static void sendColoredText(String txt, Color color, ResponseInstance res) {
        MessageEmbed me = new EmbedBuilder().setColor(color).setDescription(txt).build();

        if (res.isChannel)
            res.getChannel().sendMessageEmbeds(me).queue();
        else
            res.getInteraction().replyEmbeds(me).queue();
    }

    public static void sendSuccess(String txt, ResponseInstance res) {
        MessageEmbed me = new EmbedBuilder().setColor(Color.GREEN).setDescription(txt).build();

        if (res.isChannel())
            res.getChannel().sendMessageEmbeds(me).queue();
        else
            res.getInteraction().replyEmbeds(me).queue();
    }

    public static void sendWarning(String txt, ResponseInstance res) {
        MessageEmbed me = new EmbedBuilder().setColor(Color.YELLOW).setDescription(txt).build();

        if (res.isChannel())
            res.getChannel().sendMessageEmbeds(me).queue();
        else
            res.getInteraction().replyEmbeds(me).queue();
    }

    public static void sendError(String txt, ResponseInstance res) {
        MessageEmbed me = new EmbedBuilder().setColor(Color.RED).setDescription(txt).build();

        if (res.isChannel())
            res.getChannel().sendMessageEmbeds(me).queue();
        else
            res.getInteraction().replyEmbeds(me).queue();
    }

    public static void deleteUserMessage(int size, MessageReceivedEvent event) {
        if (event.getChannelType() == ChannelType.TEXT) {
            MessageHistory history = new MessageHistory(event.getChannel());
            List<Message> msgs;

            event.getMessage().delete().queue();

            msgs = history.retrievePast(size).complete();
            try {
                event.getChannel().purgeMessages(msgs);
            } catch (Exception e) {
            }
        }
    }

    public static void sendFiles(ResponseInstance res, FileUpload... uploads) {
        if (res.isChannel)
            res.getChannel().sendFiles(uploads).queue();
        else
            res.getInteraction().replyFiles(uploads).queue();
    }

    public static class ResponseInstance {
        boolean isChannel = true;
        private MessageChannel channel;
        private SlashCommandInteraction interaction;

        public ResponseInstance(MessageChannel channel) {
            this.channel = channel;
            this.isChannel = true;
        }

        public ResponseInstance(MessageReceivedEvent event) {
            this.channel = event.getChannel();
            this.isChannel = true;
        }

        public ResponseInstance(SlashCommandInteractionEvent event) {
            this.interaction = event.getInteraction();
            this.isChannel = false;
        }

        public ResponseInstance(SlashCommandInteraction interaction) {
            this.interaction = interaction;
            this.isChannel = false;
        }

        public MessageChannel getChannel() {
            return channel;
        }

        public SlashCommandInteraction getInteraction() {
            return interaction;
        }

        public boolean isChannel() {
            return isChannel;
        }
    }
}
