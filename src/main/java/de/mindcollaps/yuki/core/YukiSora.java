package de.mindcollaps.yuki.core;

import de.mindcollaps.yuki.NetworkManager;
import de.mindcollaps.yuki.api.ApiManagerOld;
import de.mindcollaps.yuki.api.ApiRequestHandler;
import de.mindcollaps.yuki.application.discord.core.DiscordApplication;
import de.mindcollaps.yuki.application.task.YukiTaskManager;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.util.Date;

@YukiLogModule(name = "Yuki Sora")
public class YukiSora {
    private final YukiTaskManager yukiTaskManager;
    private NetworkManager networkManager;
    private ApiRequestHandler requestHandler;
    private ApiManagerOld apiManagerOld;
    private DiscordApplication application;

    public YukiSora() {
        yukiTaskManager = new YukiTaskManager(this);
    }

    public void boot(String[] args) {
        YukiLogger.log(new YukiLogInfo("\n▄██   ▄   ███    █▄     ▄█   ▄█▄  ▄█  \n" +
                "███   ██▄ ███    ███   ███ ▄███▀ ███  \n" +
                "███▄▄▄███ ███    ███   ███▐██▀   ███▌ \n" +
                "▀▀▀▀▀▀███ ███    ███  ▄█████▀    ███▌ \n" +
                "▄██   ███ ███    ███ ▀▀█████▄    ███▌ \n" +
                "███   ███ ███    ███   ███▐██▄   ███  \n" +
                "███   ███ ███    ███   ███ ▀███▄ ███  \n" +
                " ▀█████▀  ████████▀    ███   ▀█▀ █▀   \n" +
                "                       ▀              \n\n" +
                "\n   ▄████████  ▄██████▄     ▄████████    ▄████████ \n" +
                "  ███    ███ ███    ███   ███    ███   ███    ███ \n" +
                "  ███    █▀  ███    ███   ███    ███   ███    ███ \n" +
                "  ███        ███    ███  ▄███▄▄▄▄██▀   ███    ███ \n" +
                "▀███████████ ███    ███ ▀▀███▀▀▀▀▀   ▀███████████ \n" +
                "         ███ ███    ███ ▀███████████   ███    ███ \n" +
                "   ▄█    ███ ███    ███   ███    ███   ███    ███ \n" +
                " ▄████████▀   ▀██████▀    ███    ███   ███    █▀  \n" +
                "                          ███    ███              \n\n" +
        "\nDesigned by: Noah Elijah Till\nand the weebskingdom team\n-------------------------------------------------------------------------\n\n"));

        YukiProperties.loadApplicationSettings();
        YukiProperties.loadBotProperties();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                onShutdown();
            }
        }));

        printBuildData();

        networkManager = new NetworkManager(this);
        requestHandler = new ApiRequestHandler(this);
        apiManagerOld = new ApiManagerOld(this);

        application = new DiscordApplication(this);
        YukiLogger.log(new YukiLogInfo("Yuki Sora application successfully started at " + new Date()));

        handleArgs(args);
    }

    private void handleArgs(String[] args) {
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "start" -> {
                    YukiLogger.log(new YukiLogInfo("Starting Discord Application because of start parameter -->"));
                    application.boot();
                }
                case "debug" -> {
                    YukiLogger.log(new YukiLogInfo("Fine Debug activated, cmd and network will be printed into the debug log!"));
                    YukiProperties.getApplicationSettings().fineDebug = true;
                }
            }
        }
    }

    private void printBuildData() {
        YukiProperties applicationSettings = YukiProperties.getApplicationSettings();
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = null;

        String pomFileLocation = YukiProperties.getProperties(YukiProperties.dPPomFileLocation);
        if (pomFileLocation != null) {
            try {
                model = reader.read(new FileReader(System.getProperty("user.dir") + pomFileLocation));
            } catch (Exception ignored) {
            }
        }

        if (model == null)
            try {
                model = reader.read(new FileReader(System.getProperty("user.dir") + "/pom.xml"));
            } catch (Exception e) {
                YukiLogger.log(new YukiLogInfo("Can't find POM File!").warning());
            }
        YukiLogger.log(new YukiLogInfo("-------------------------------------------").debug());
        YukiLogger.log(new YukiLogInfo("Fine Debug: " + applicationSettings.fineDebug).debug());
        String oldVersion = applicationSettings.mvnVersion;
        applicationSettings.mvnArtifact = model.getArtifactId();
        applicationSettings.mvnGroup = model.getGroupId();
        applicationSettings.mvnVersion = model.getVersion();
        YukiLogger.log(new YukiLogInfo("<" + applicationSettings.mvnGroup + "> " + applicationSettings.mvnArtifact + ": " + applicationSettings.mvnVersion).debug());
        if (!applicationSettings.mvnVersion.equals(oldVersion)) {
            YukiLogger.log(new YukiLogInfo("Updated from: " + oldVersion + " to " + applicationSettings.mvnVersion).debug());
        }
        YukiLogger.log(new YukiLogInfo("-------------------------------------------\n\n").debug());
    }

    private void onShutdown() {
        YukiProperties.saveApplicationSettings();

        if (application != null)
            application.shutdown();
    }

    public void shutdown(){
        onShutdown();
    }

    public YukiTaskManager getYukiTaskManager() {
        return yukiTaskManager;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public ApiRequestHandler getRequestHandler() {
        return requestHandler;
    }

    public ApiManagerOld getApiManagerOld() {
        return apiManagerOld;
    }

    public DiscordApplication getDiscordApplication() {
        return application;
    }
}
