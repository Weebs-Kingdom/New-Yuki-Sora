package de.mindcollaps.yuki.application.discord.listener;

import de.mindcollaps.yuki.api.lib.LibManager;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandParser;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class DiscCommandListener extends ListenerAdapter {

    private final YukiSora yukiSora;

    public DiscCommandListener(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getChannelType() == ChannelType.TEXT) {
            handleSlashCommand(event);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().length() > 300)
            return;

        if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
            return;

        if (!event.getMessage().getContentRaw().startsWith(YukiProperties.getProperties(YukiProperties.dPDefaultCommandPrefix)))
            return;

        if (event.getChannelType() == ChannelType.PRIVATE) {
            handlePrivateCommand(event);
        } else if (event.getChannelType() == ChannelType.TEXT) {
            handleGuildCommand(event);
        }
    }

    private void handlePrivateCommand(MessageReceivedEvent event) {
        boolean commandWorked = false;
        for (int i = 0; yukiSora.getDiscordApplication().getCommandHandler().commandIvokes.size() > i; i++) {
            if (event.getMessage().getContentRaw().startsWith(yukiSora.getDiscordApplication().getCommandHandler().commandIvokes.get(i))) {
                sendPrivateCommand(event);
                commandWorked = true;
                break;
            }
        }
    }

    private void handleGuildCommand(MessageReceivedEvent event) {
        Member selfUser = event.getGuild().getMemberById(event.getGuild().getJDA().getSelfUser().getId());
        boolean hasPermission = true;
        try {
            hasPermission = selfUser.hasPermission(Permission.ADMINISTRATOR);
        } catch (Exception e) {
            hasPermission = false;
        }
        if (!hasPermission) {
            TextUtil.sendError("Hello? I don't have admin permissions here. I won't operate until I have them :triumph:", event.getChannel());
        } else {
            DiscApplicationUser user = LibManager.retrieveUser(event.getAuthor(), yukiSora);
            DiscApplicationServer server = LibManager.retrieveServer(event.getGuild(), yukiSora);
            try {
                yukiSora.getDiscordApplication().getCommandHandler().handleServerCommand(DiscCommandParser.parseServerMessage(event.getMessage().getContentRaw(), event, server, user, yukiSora));
            } catch (Exception e) {
                TextUtil.sendError("Fatal command error on command: " + event.getMessage().getContentRaw(), event.getChannel());
                YukiLogger.log(new YukiLogInfo("Sending client command failed!", "DiscCommandListener").trace(e));
            }
        }
    }

    private void handleSlashCommand(SlashCommandInteractionEvent event) {
        Member selfUser = event.getGuild().getMemberById(event.getGuild().getJDA().getSelfUser().getId());
        boolean hasPermission = true;
        try {
            hasPermission = selfUser.hasPermission(Permission.ADMINISTRATOR);
        } catch (Exception e) {
            hasPermission = false;
        }
        if (!hasPermission) {
            TextUtil.sendError("Hello? I don't have admin permissions here. I won't operate until I have them :triumph:", event.getChannel());
        } else {
            DiscApplicationUser user = LibManager.retrieveUser(event.getUser(), yukiSora);
            DiscApplicationServer server = LibManager.retrieveServer(event.getGuild(), yukiSora);
            try {
                yukiSora.getDiscordApplication().getCommandHandler().handleSlashCommand(DiscCommandParser.parseSlashMessage(event.getCommandString(), event, server, user, yukiSora));
            } catch (Exception e) {
                TextUtil.sendError("Fatal command error on command: " + event.getCommandString(), event.getChannel());
                YukiLogger.log(new YukiLogInfo("Sending client command failed!", "DiscCommandListener").trace(e));
            }
        }
    }

    private void sendPrivateCommand(MessageReceivedEvent event) {
        DiscApplicationUser user = LibManager.retrieveUser(event.getAuthor(), yukiSora);

        try {
            yukiSora.getDiscordApplication().getCommandHandler().handleClientCommand(DiscCommandParser.parseClientMessage(event.getMessage().getContentRaw(), event, user, yukiSora));
        } catch (Exception e) {
            TextUtil.sendError("Fatal command error on command: " + event.getMessage().getContentRaw(), event.getChannel());
            YukiLogger.log(new YukiLogInfo("Sending client command failed!", "DiscCommandListener").trace(e));
        }
    }
}
