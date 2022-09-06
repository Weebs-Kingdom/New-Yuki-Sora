package de.mindcollaps.yuki.core;

import de.mindcollaps.yuki.NetworkManager;
import de.mindcollaps.yuki.api.ApiManagerOld;
import de.mindcollaps.yuki.api.ApiRequestHandler;
import de.mindcollaps.yuki.api.YukiApi;
import de.mindcollaps.yuki.application.discord.core.DiscordApplication;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;

public class YukiSora {

    private final String consMsgDef = "[Yuki Sora]";

    private final YukiApi yukiApi;
    private NetworkManager networkManager;
    private ApiRequestHandler requestHandler;
    private ApiManagerOld apiManagerOld;
    private DiscordApplication application;

    public YukiSora() {
        yukiApi = new YukiApi();
    }

    public void boot(String[] args) {
        System.out.println("▄██   ▄   ███    █▄     ▄█   ▄█▄  ▄█  \n" +
                "███   ██▄ ███    ███   ███ ▄███▀ ███  \n" +
                "███▄▄▄███ ███    ███   ███▐██▀   ███▌ \n" +
                "▀▀▀▀▀▀███ ███    ███  ▄█████▀    ███▌ \n" +
                "▄██   ███ ███    ███ ▀▀█████▄    ███▌ \n" +
                "███   ███ ███    ███   ███▐██▄   ███  \n" +
                "███   ███ ███    ███   ███ ▀███▄ ███  \n" +
                " ▀█████▀  ████████▀    ███   ▀█▀ █▀   \n" +
                "                       ▀              \n");
        System.out.println("   ▄████████  ▄██████▄     ▄████████    ▄████████ \n" +
                "  ███    ███ ███    ███   ███    ███   ███    ███ \n" +
                "  ███    █▀  ███    ███   ███    ███   ███    ███ \n" +
                "  ███        ███    ███  ▄███▄▄▄▄██▀   ███    ███ \n" +
                "▀███████████ ███    ███ ▀▀███▀▀▀▀▀   ▀███████████ \n" +
                "         ███ ███    ███ ▀███████████   ███    ███ \n" +
                "   ▄█    ███ ███    ███   ███    ███   ███    ███ \n" +
                " ▄████████▀   ▀██████▀    ███    ███   ███    █▀  \n" +
                "                          ███    ███              ");
        System.out.println("\nDesigned by: Noah Elijah Till\nand the weebskingdom team\n-------------------------------------------------------------------------\n\n");

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
                YukiLogger.log(new YukiLogInfo("Can't find POM File!", "YukiSora").warning());
            }
        System.out.println("\n-------------------------------------------");
        System.out.println("Debug: " + applicationSettings.debug);
        System.out.println("Network Debug: " + applicationSettings.fineDebug);
        String oldVersion = applicationSettings.mvnVersion;
        applicationSettings.mvnArtifact = model.getArtifactId();
        applicationSettings.mvnGroup = model.getGroupId();
        applicationSettings.mvnVersion = model.getVersion();
        System.out.println("<" + applicationSettings.mvnGroup + "> " + applicationSettings.mvnArtifact + ": " + applicationSettings.mvnVersion);
        if (!applicationSettings.mvnVersion.equals(oldVersion)) {
            System.out.println("Updated from: " + oldVersion + " to " + applicationSettings.mvnVersion);
        }
        System.out.println("-------------------------------------------\n\n");
    }

    private void onShutdown() {
        YukiProperties.saveApplicationSettings();

        if (application != null)
            application.onShutdown();
    }

    public YukiApi getYukiApi() {
        return yukiApi;
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
