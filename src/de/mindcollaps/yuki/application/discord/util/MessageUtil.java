package de.mindcollaps.yuki.application.discord.util;

import java.util.concurrent.ThreadLocalRandom;

public class MessageUtil {

    //TODO: outsource to database system
    private static final String[] welcomeMessages = {
            "Welcome our new superstar %user%",
            "Say hello to %user%",
            "We welcome you here on our guild %user%",
            "We are happy you are here %user%",
            "We are glad you joined us %user%",
            "I wish you a pleasant stay here with us %user%",
            "%user% just joined our guild, say hello :wave:",
            "Welcome to our guild %user%",
            ""
    };

    public static String getWelcomeMessage(String name) {
        return welcomeMessages[
                ThreadLocalRandom.current()
                        .nextInt(0, welcomeMessages.length - 1)]
                .replace("%user%", name);
    }
}
