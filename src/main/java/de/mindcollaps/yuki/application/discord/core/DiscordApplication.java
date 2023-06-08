package de.mindcollaps.yuki.application.discord.core;

import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.manager.LibManager;
import de.mindcollaps.yuki.application.discord.command.commands.*;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandHandler;
import de.mindcollaps.yuki.application.discord.command.special.SCmdRename;
import de.mindcollaps.yuki.application.discord.listener.DiscAutoChannelListener;
import de.mindcollaps.yuki.application.discord.listener.DiscCertificationMessageListener;
import de.mindcollaps.yuki.application.discord.listener.DiscCommandListener;
import de.mindcollaps.yuki.application.discord.listener.DiscReactionListener;
import de.mindcollaps.yuki.application.twitch.TwitchApplicationEngine;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@YukiLogModule(name = "Discord Application")
public class DiscordApplication {

    private final YukiSora yukiSora;
    private final ArrayList<ListenerAdapter> listeners;
    private final DiscCommandHandler commandHandler;
    private boolean isRunning;
    private DiscAutoChannelListener autoChannelListener;
    private JDABuilder builder;
    private JDA botJDA;

    private TwitchApplicationEngine twitchApplication;

    public DiscordApplication(YukiSora yukiSora) {
        this.yukiSora = yukiSora;

        this.listeners = new ArrayList<>();
        this.commandHandler = new DiscCommandHandler(this);
    }

    public void boot() {
        if (isRunning)
            return;

        String discToken = YukiProperties.getProperties(YukiProperties.dPDiscordToken);
        if (discToken == null) {
            YukiLogger.log(new YukiLogInfo("!!!BOT START FAILURE - token invalid").error());
            return;
        }
        YukiLogger.log(new YukiLogInfo("!Bot start initialized!"));
        isRunning = true;

        builder = JDABuilder.create(discToken, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.SCHEDULED_EVENTS, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_INVITES);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableCache(CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY, CacheFlag.EMOJI, CacheFlag.STICKER);

        try {
            botJDA = builder.build();
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("!Login failed! - token could be invalid!").trace(e));
            isRunning = false;
            return;
        }

        twitchApplication = new TwitchApplicationEngine(yukiSora);
        twitchApplication.boot();

        initCommands();
        initListeners();
        initServers();

        yukiSora.getYukiTaskManager().startUpdateThread();

        YukiLogger.log(new YukiLogInfo("Discord Application started successfully!"));
    }

    public void shutdown() {
        if (botJDA != null)
            botJDA.shutdownNow();

        saveAutochans();
    }

    private void saveAutochans() {
        if(!isRunning)
            return;
        
        YukiLogger.log(new YukiLogInfo("Saving autochans...").debug());

        HashMap<String, ArrayList<String>> data = new HashMap<>();

        for (AutoChannel autoChannel : autoChannelListener.getActiveAutoChannels()) {
            if (autoChannel.getVoiceChannel() == null)
                continue;
            if (!data.containsKey(autoChannel.getServer()))
                data.put(autoChannel.getServer().getDatabaseId(), new ArrayList<>());

            data.get(autoChannel.getServer()).add(autoChannel.getVoiceChannel().getId());
        }
        try {
            FileUtils.saveObject(FileUtils.home + "/autochans.dat", data);
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("!Failed to save autochans").trace(e));
        }
    }

    public void initServers() {
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                HashMap<String, ArrayList<String>> autos = null;
                try {
                    autos = (HashMap<String, ArrayList<String>>) FileUtils.loadObject(FileUtils.home + "/autochans.dat");
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Can't find autochans.dat probably no auto channels saved").debug());
                }
                if (autos != null)
                    FileUtils.deleteFile(FileUtils.home + "/autochans.dat");

                YukiLogger.log(new YukiLogInfo("Initializing guilds..."));
                for (Guild g : botJDA.getGuilds()) {
                    DiscApplicationServer s = LibManager.retrieveServer(g, yukiSora);
                    if (s == null)
                        continue;

                    try {
                        s.updateServerStats(yukiSora);
                    } catch (Exception e) {
                        YukiLogger.log(new YukiLogInfo("Failed to init server stats of " + g.getName() + "(" + g.getId() + ")").trace(e));
                    }

                    if (autos != null) {
                        ArrayList<String> gAutoChans = autos.get(s.getDatabaseId());
                        if (gAutoChans != null)
                            autoChannelListener.loadAutoChans(gAutoChans, g, s, yukiSora);
                    }

                    YukiLogger.log(new YukiLogInfo(" - " + s.getGuildName() + " initialized"));
                }

                YukiLogger.log(new YukiLogInfo("Done initializing guilds"));
            }
        };
        t.schedule(tt, 10 * 10 * 10 * 10);
    }

    private void initCommands() {
        YukiLogger.log(new YukiLogInfo("Initializing commands...").debug());
        commandHandler.createNewCommand(new DiscCmdAlexa());
        commandHandler.createNewCommand(new DiscCmdInfo());
        commandHandler.createNewCommand(new DiscCmdJob());
        commandHandler.createNewCommand(new DiscCmdMusic());
        commandHandler.createNewCommand(new DiscCmdSetup());
        commandHandler.createNewCommand(new DiscCmdVote());
        commandHandler.createNewCommand(new DiscCmdWallet());

        commandHandler.createNewCommand(new SCmdRename());

        commandHandler.registerCommands(yukiSora);

        YukiLogger.log(new YukiLogInfo("Done initializing commands").debug());
    }

    private void initListeners() {
        autoChannelListener = new DiscAutoChannelListener(yukiSora);

        listeners.add(autoChannelListener);
        listeners.add(new DiscCommandListener(yukiSora));
        listeners.add(new DiscCertificationMessageListener(yukiSora));
        listeners.add(new DiscReactionListener(yukiSora));
        regListeners();
    }

    private void regListeners() {
        for (ListenerAdapter listener : listeners) {
            botJDA.removeEventListener(listener);
            botJDA.addEventListener(listener);
        }
    }

    public JDA getBotJDA() {
        return botJDA;
    }

    public DiscCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public DiscAutoChannelListener getAutoChannelListener() {
        return autoChannelListener;
    }
}
