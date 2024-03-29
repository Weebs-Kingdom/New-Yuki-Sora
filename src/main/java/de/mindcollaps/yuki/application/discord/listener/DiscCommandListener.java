package de.mindcollaps.yuki.application.discord.listener;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.manager.LibManager;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandParser;
import de.mindcollaps.yuki.application.discord.response.ResponseHandler;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@YukiLogModule(name = "Disc Listener - Command Listener")
public class DiscCommandListener extends ListenerAdapter {

    private final YukiSora yukiSora;

    public DiscCommandListener(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
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

        if (ResponseHandler.lookForResponse(event))
            return;

        if (!event.getMessage().getContentRaw().startsWith(YukiProperties.getProperties(YukiProperties.dPDefaultCommandPrefix)) &&
                !event.getMessage().getContentRaw().startsWith(YukiProperties.getProperties(YukiProperties.dPSpecialCommandPrefix)))
            return;

        if (event.getChannelType() == ChannelType.PRIVATE) {
            handlePrivateCommand(event);
        } else if (event.getChannelType() == ChannelType.TEXT) {
            handleGuildCommand(event);
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        ResponseHandler.lookForResponse(event);
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
            TextUtil.sendError("Hello? I don't have admin permissions here. I won't operate until I have them :triumph:", new TextUtil.ResponseInstance(event.getChannel()));
        } else {
            DiscApplicationUser user = LibManager.retrieveUser(event.getAuthor(), yukiSora);
            DiscApplicationServer server = LibManager.retrieveServer(event.getGuild(), yukiSora);

            if (user == null || server == null) {
                TextUtil.sendError("There was an error communicating with the database, this issue has been logged and will be fixed ASAP :face_with_spiral_eyes:", new TextUtil.ResponseInstance(event.getChannel()));
                YukiLogger.log(new YukiLogInfo("There has been a problem communicating with the database! This issues should be looked at very soon!").error());
                return;
            }
            String log = "---CMD---\nCMD_ORIGIN: guild_message, CMD: " + event.getMessage().getContentRaw();
            if (YukiProperties.getApplicationSettings().fineDebug)
                YukiLogger.log(new YukiLogInfo(log));
            try {
                yukiSora.getDiscordApplication().getCommandHandler().handleServerCommand(DiscCommandParser.parseServerMessage(event.getMessage().getContentRaw(), event, server, user, yukiSora));
            } catch (Exception e) {
                TextUtil.sendError("Fatal command error on command: " + event.getMessage().getContentRaw(), new TextUtil.ResponseInstance(event.getChannel()));
                YukiLogger.log(new YukiLogInfo("Sending client command failed!\n" + log).trace(e));
            }
            if (YukiProperties.getApplicationSettings().fineDebug)
                YukiLogger.log(new YukiLogInfo("--CMD-END--\n"));
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
            TextUtil.sendError("Hello? I don't have admin permissions here. I won't operate until I have them :triumph:", new TextUtil.ResponseInstance(event.getChannel()));
        } else {
            DiscApplicationUser user = LibManager.retrieveUser(event.getUser(), yukiSora);
            DiscApplicationServer server = LibManager.retrieveServer(event.getGuild(), yukiSora);

            if (user == null || server == null) {
                TextUtil.sendError("There was an error communicating with the database, this issue has been logged and will be fixed ASAP :face_with_spiral_eyes:", new TextUtil.ResponseInstance(event.getChannel()));
                YukiLogger.log(new YukiLogInfo("There has been a problem communicating with the database! This issues should be looked at very soon!").error());
                return;
            }

            String log = "---CMD---\nCMD_ORIGIN: slash, CMD: " + event.getCommandString();
            if (YukiProperties.getApplicationSettings().fineDebug)
                YukiLogger.log(new YukiLogInfo(log));
            try {
                yukiSora.getDiscordApplication().getCommandHandler().handleSlashCommand(DiscCommandParser.parseSlashMessage(event.getCommandString(), event, server, user, yukiSora));
            } catch (Exception e) {
                TextUtil.sendError("Fatal command error on command: " + event.getCommandString(), new TextUtil.ResponseInstance(event.getChannel()));
                YukiLogger.log(new YukiLogInfo("Sending client command failed!\n" + log).trace(e));
            }
            if (YukiProperties.getApplicationSettings().fineDebug)
                YukiLogger.log(new YukiLogInfo("--CMD-END--\n"));
        }
    }

    private void sendPrivateCommand(MessageReceivedEvent event) {
        DiscApplicationUser user = LibManager.retrieveUser(event.getAuthor(), yukiSora);
        if (user == null) {
            TextUtil.sendError("There was an error communicating with the database, this issue has been logged and will be fixed ASAP :face_with_spiral_eyes:", new TextUtil.ResponseInstance(event.getChannel()));
            YukiLogger.log(new YukiLogInfo("There has been a problem communicating with the database! This issues should be looked at very soon!").error());
            return;
        }

        String log = "---CMD---\nCMD_ORIGIN: private, CMD: " + event.getMessage().getContentRaw();
        if (YukiProperties.getApplicationSettings().fineDebug)
            YukiLogger.log(new YukiLogInfo(log));
        try {
            yukiSora.getDiscordApplication().getCommandHandler().handleClientCommand(DiscCommandParser.parseClientMessage(event.getMessage().getContentRaw(), event, user, yukiSora));
        } catch (Exception e) {
            TextUtil.sendError("Fatal command error on command: " + event.getMessage().getContentRaw(), new TextUtil.ResponseInstance(event.getChannel()));
            YukiLogger.log(new YukiLogInfo("Sending client command failed!\n" + log).trace(e));
        }
        if (YukiProperties.getApplicationSettings().fineDebug)
            YukiLogger.log(new YukiLogInfo("--CMD-END--\n"));
    }
}
