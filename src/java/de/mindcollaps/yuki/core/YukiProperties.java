package de.mindcollaps.yuki.core;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

@YukiLogModule(name = "Yuki Properties")
public class YukiProperties implements Serializable {

    //Default properties keys
    public static final String dPDefaultCommandPrefix = "disc-application-prefix";
    public static final String dPSpecialCommandPrefix = "disc-application-special-prefix";
    public static final String dPPomFileLocation = "bot-pomfile-location";
    public static final String dPApiPort = "botapi-port";
    public static final String dPApiUrl = "botapi-url";
    public static final String dPDbApiUrl = "botapi-yukidb-url";
    public static final String dPDbApiToken = "botapi-yukidb-token";
    public static final String dPMusicSlaveArr = "botapi-botslave";
    public static final String dPDbApiUrlOld = "old-botapi-yukidb-url";
    public static final String dPTwitchSecret = "twitch-secret";
    public static final String dPTwitchToken = "twitch-token";
    public static final String dPClientId = "twitch-client-id";
    public static final String dPDiscordToken = "disc-application-token";
    private static final String yukiPropertiesPath = FileUtils.home + "/yuki.properties";
    private static final String yukiApplicationSettingsPath = FileUtils.home + "/application.dat";
    private static final String[][] defaultProperties = new String[][]{
            new String[]{dPDefaultCommandPrefix, "-"},

            new String[]{dPDiscordToken, "null"},

            new String[]{dPClientId, "null"},
            new String[]{dPTwitchToken, "null"},
            new String[]{dPTwitchSecret, "null"},

            new String[]{dPApiUrl, "null"},
            new String[]{dPApiPort, "5003"},
            new String[]{dPDbApiUrl, "http://127.0.0.1:5004/yuki-api"},
            new String[]{dPDbApiToken, "null"},
            new String[]{dPMusicSlaveArr + ".1", "null"},
            new String[]{dPMusicSlaveArr + ".2", "null"},

            new String[]{dPDbApiUrlOld, "http://127.0.0.1:5004/api/yuki"},

            new String[]{dPPomFileLocation, "null"},
            new String[]{dPSpecialCommandPrefix, "!"}
    };

    private static Properties properties;

    private static YukiProperties applicationSettings;
    //Automated Properties
    //Maven
    public String mvnVersion = "";
    public String mvnGroup = "";
    public String mvnArtifact = "";
    //Engine stuff
    public boolean fineDebug = false;
    public boolean showTime = true;
    public String api;
    public int apiPort = 5003;

    public static String getProperties(String key) {
        if (properties == null)
            return null;

        if (!properties.containsKey(key)) {
            YukiLogger.log(new YukiLogInfo("Application was asking for Properties key " + key + " but that key isn't present!").debug());
            return null;
        }

        String data = properties.getProperty(key);
        if (data.equalsIgnoreCase("null"))
            return null;

        return data;
    }

    public static String[] getMultipleProperties(String key) {
        ArrayList<String> data = new ArrayList<>();
        for (String stringPropertyName : properties.stringPropertyNames()) {
            if (stringPropertyName.startsWith(key)) {
                data.add(stringPropertyName);
            }
        }

        String[] props = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            props[i] = properties.getProperty(data.get(i));
        }

        return props;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static YukiProperties loadApplicationSettings() {
        applicationSettings = null;
        try {
            applicationSettings = (YukiProperties) FileUtils.loadObject(yukiApplicationSettingsPath);
        } catch (Exception ignored) {
        }

        if (applicationSettings == null) {
            applicationSettings = new YukiProperties();
            saveApplicationSettings();
        }

        return applicationSettings;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Properties loadBotProperties() {
        properties = new Properties();
        FileUtils.createFileRootAndFile(new File(yukiPropertiesPath));
        try {
            InputStream inputStream = new FileInputStream(yukiPropertiesPath);
            properties.load(inputStream);
        } catch (Exception ignored) {
        }

        if (properties == null) {
            properties = getDefaultProperties();
            YukiLogger.log(new YukiLogInfo("Couldn't find a properties file - using default parameters").warning());
            YukiLogger.log(new YukiLogInfo("Created default properties file: " + properties.toString()).debug());
            saveBotProperties();
        } else {
            Properties defaultProperties = getDefaultProperties();
            boolean hasErrors = false;
            for (String stringPropertyName : defaultProperties.stringPropertyNames()) {
                if (!properties.containsKey(stringPropertyName)) {
                    for (String propertyName : defaultProperties.stringPropertyNames()) {
                        if (!properties.containsKey(propertyName)) {
                            properties.put(propertyName, defaultProperties.get(stringPropertyName));
                            hasErrors = true;
                        }
                    }
                }
            }
            if (hasErrors) {
                YukiLogger.log(new YukiLogInfo("The properties file had missing fields, so we added them").warning());
                saveBotProperties();
                return properties;
            }
        }

        return properties;
    }

    public static void saveBotProperties() {
        YukiLogger.log(new YukiLogInfo("Properties are: " + properties).debug());
        if (properties == null)
            return;

        try {
            properties.store(new FileOutputStream(yukiPropertiesPath), "");
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("An error occurred while saving the bot properties!").trace(e));
        }
    }

    public static void saveApplicationSettings() {
        if (applicationSettings == null)
            return;

        try {
            FileUtils.saveObject(yukiApplicationSettingsPath, applicationSettings);
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("An error occurred while saving the application settings!").trace(e));
        }
    }

    private static Properties getDefaultProperties() {
        Properties properties = new Properties();

        for (String[] defaultProperty : defaultProperties) {
            properties.put(defaultProperty[0], defaultProperty[1]);
        }

        return properties;
    }

    public static YukiProperties getApplicationSettings() {
        return applicationSettings;
    }
}
