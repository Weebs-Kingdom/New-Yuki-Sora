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
            "Hey %user%! Welcome to the community!",
            "Warmest greetings to %user%! We're thrilled to have you here!",
            "It's a pleasure to welcome %user% to our server!",
            "%user% has joined the party! Let's give them a warm welcome!",
            "Attention everyone! %user% has arrived. Say hello and make them feel at home!",
            "Welcome aboard, %user%! We hope you enjoy your time here.",
            "Hey %user%! We've been waiting for you. Get ready for an amazing adventure!",
            "Join us in giving a warm welcome to %user%. We're excited to have you join our community!",
            "Welcome to our amazing community, %user%!",
            "We're delighted to have you here, %user%. Enjoy your stay!",
            "Say hi to %user% as they join our fantastic server!",
            "A warm welcome to %user%! Get ready to have a great time!",
            "Welcome, %user%! We're thrilled to have you as part of our family.",
            "Hello, %user%! We hope you feel right at home in our server.",
            "New member alert! Let's give a warm welcome to %user%.",
            "Welcome to the team, %user%! We're excited to have you on board.",
            "Hey there, %user%! We're glad you found your way to our community.",
            "Join us in extending a warm welcome to %user%. We're happy to have you here!",
            "Greetings, %user%! Get ready to connect, engage, and have a blast!",
            "Welcome to our server, %user%! Feel free to explore and make new friends.",
            "Say hello to %user%, the newest addition to our awesome community!",
            "Welcome aboard, %user%! Prepare for an unforgettable experience with us.",
            "A big welcome to %user%! We can't wait to get to know you better.",
            "Hello, %user%! Get ready for an incredible journey in our server.",
            "Welcome to our vibrant community, %user%! Enjoy your time here.",
            "Say hi to %user% as they join our amazing server!",
            "New member alert! Let's give a warm welcome to %user%.",
            "Welcome, %user%! We're excited to have you join our server.",
            "Hello and welcome, %user%! Make yourself at home in our community.",
            "Say a big hello to %user%, the newest member of our server!",
            "Welcome aboard, %user%! Get ready for an incredible journey with us.",
            "Hey there, %user%! We're thrilled to have you as part of our server.",
            "Welcome to the family, %user%! We're glad you're here.",
            "Say hello to %user% and make them feel right at home in our server.",
            "Welcome, %user%! Prepare for a fun-filled adventure with us.",
            "Greetings, %user%! We're excited to have you join our growing community.",
            "Welcome to our server, %user%! Feel free to explore and connect with others.",
            "Say a warm hello to %user% as they embark on their Discord journey.",
            "Hello, %user%! We hope you enjoy your time in our server.",
            "Welcome to the team, %user%! We're glad to have you on board."
    };

    public static String getWelcomeMessage(String name) {
        return welcomeMessages[
                ThreadLocalRandom.current()
                        .nextInt(0, welcomeMessages.length - 1)]
                .replace("%user%", name);
    }
}
