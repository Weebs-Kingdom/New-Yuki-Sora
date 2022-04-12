package de.mindcollaps.yuki.application.discord.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.request.FindServerByGuildId;
import de.mindcollaps.yuki.application.discord.command.commands.DiscCmdAlexa;
import de.mindcollaps.yuki.application.discord.command.commands.DiscCmdMusic;
import de.mindcollaps.yuki.application.discord.command.commands.DiscCmdSetup;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandHandler;
import de.mindcollaps.yuki.application.discord.listener.DiscAutoChannelListener;
import de.mindcollaps.yuki.application.discord.listener.DiscCommandListener;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.FileUtils;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordApplication {

    private final String consMsgDef = "[Discord application]";

    private final YukiSora yukiSora;

    private boolean isRunning;

    private final ArrayList<ListenerAdapter> listeners;
    private DiscAutoChannelListener autoChannelListener;

    private final DiscCommandHandler commandHandler;

    private JDABuilder builder;
    private JDA botJDA;

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
            YukiLogger.log(new YukiLogInfo("!!!BOT START FAILURE - token invalid", consMsgDef).error());
            return;
        }
        YukiLogger.log(new YukiLogInfo("!Bot start initialized!", consMsgDef));
        isRunning = true;

        builder = JDABuilder.createDefault(discToken);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);
        builder.enableCache(CacheFlag.CLIENT_STATUS, CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY);

        try {
            botJDA = builder.build();
        } catch (LoginException e) {
            YukiLogger.log(new YukiLogInfo("!Logging in the bot failed! - token could be invalid!", consMsgDef).error());
            isRunning = false;
            return;
        }

        initCommands();
        initListeners();
        initServers();
    }

    public void onShutdown() {
        if (botJDA != null)
            botJDA.shutdownNow();
    }

    public void initServers() {
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                for (Guild g : botJDA.getGuilds()) {
                    DiscApplicationServer s = new FindServerByGuildId(g.getId()).makeRequestSingle(yukiSora);
                    if (s == null)
                        continue;

                    s.updateServerStats(yukiSora);
                    ArrayList<String> autos = null;
                    try {
                        autos = (ArrayList<String>) FileUtils.loadObject(FileUtils.home + "/autochans.dat");
                    } catch (Exception e) {
                    }
                    if (autos == null)
                        autos = new ArrayList<>();
                    autoChannelListener.loadAutoChans(autos, s);

                    System.out.println(consMsgDef + " " + s.getServerName() + " initialized");
                }
            }
        };
        t.schedule(tt, 10 * 10 * 10 * 10);
    }

    private void initCommands() {
        commandHandler.createNewCommand(new DiscCmdAlexa());
        commandHandler.createNewCommand(new DiscCmdMusic());
        commandHandler.createNewCommand(new DiscCmdSetup());

        commandHandler.registerCommands(yukiSora);
    }

    private void initListeners() {
        autoChannelListener = new DiscAutoChannelListener(yukiSora);

        listeners.add(autoChannelListener);
        listeners.add(new DiscCommandListener(yukiSora));
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
}
