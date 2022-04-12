package de.mindcollaps.yuki.application.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class TextUtil {

    public static void sendNormalTxt(String txt, MessageChannel channel){
        channel.sendMessage(txt).queue();
    }

    public static void sendColoredText(String txt, Color color, MessageChannel channel){
        channel.sendMessageEmbeds(
                new EmbedBuilder().setColor(color).setDescription(txt).build()
        ).queue();
    }

    public static void sendSuccess(String txt, MessageChannel channel){
        channel.sendMessageEmbeds(
                new EmbedBuilder().setColor(Color.GREEN).setDescription(txt).build()
        ).queue();
    }

    public static void sendWarning(String txt, MessageChannel channel){
        channel.sendMessageEmbeds(
                new EmbedBuilder().setColor(Color.YELLOW).setDescription(txt).build()
        ).queue();
    }

    public static void sendError(String txt, MessageChannel channel){
        channel.sendMessageEmbeds(
                new EmbedBuilder().setColor(Color.RED).setDescription(txt).build()
        ).queue();
    }

    public static void deleteUserMessage(int size, MessageReceivedEvent event) {
        if(event.getChannelType() == ChannelType.TEXT){
            MessageHistory history = new MessageHistory(event.getChannel());
            List<Message> msgs;

            event.getMessage().delete().queue();

            msgs = history.retrievePast(size).complete();
            try {
                event.getTextChannel().deleteMessages(msgs).queue();
            } catch (Exception e) {
            }
        }
    }
}
