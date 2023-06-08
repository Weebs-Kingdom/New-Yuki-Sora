package de.mindcollaps.yuki.application.twitch;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.helix.domain.User;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.manager.LibManager;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import feign.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@YukiLogModule(name = "Twitch Engine")
public class TwitchApplicationEngine {

    private final YukiSora yukiSora;
    private final ArrayList<User> users;
    private final ArrayList<DiscApplicationUser> users2;
    private TwitchClient twitchClient;

    public TwitchApplicationEngine(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
        users = new ArrayList<>();
        users2 = new ArrayList<>();
    }

    public void boot() {
        TwitchClientBuilder b = TwitchClientBuilder.builder()
                .withClientId(YukiProperties.getProperties(YukiProperties.dPClientId))
                .withClientSecret(YukiProperties.getProperties(YukiProperties.dPTwitchSecret))
                .withFeignLogLevel(Logger.Level.BASIC)
                .withBotOwnerId("142364234")
                .withEnableHelix(true);

        twitchClient = b.build();


        TwitchEventHandler myEventHandler = new TwitchEventHandler();
        b.getEventManager().getEventHandler(SimpleEventHandler.class).registerListener(myEventHandler);

        JSONObject res = yukiSora.getApiManagerOld().getTwitchUsers();
        if(res == null){
            YukiLogger.log(
                    new YukiLogInfo("Couldn't load Twitch Users. It seems that they are empty").warning()
            );
            return;
        }
        JSONArray array = (JSONArray) res.get("data");

        for (Object o : array) {
            JSONObject us = (JSONObject) o;
            String userId = (String) us.get("user");
            DiscApplicationUser user = LibManager.findUser(userId, yukiSora);
            String userName = (String) us.get("twitchChannelId");
            if (user == null || userName == null) {
                YukiLogger.log(
                        new YukiLogInfo("!Twitch listener not enabled for " + userName + "! Probably not configured correctly").debug()
                );
                continue;
            }
            YukiLogger.log(
                    new YukiLogInfo("Enabling twitch listener for " + userName + "!").debug()
            );
            User twitchUser = twitchClient.getClientHelper().enableStreamEventListener(userName);
            if (twitchUser == null) {
                YukiLogger.log(
                        new YukiLogInfo("!Twitch listener not enabled for " + userName + "! Couldn't load twitch user from twitch").debug()
                );
                continue;
            }

            users2.add(user);
            users.add(twitchUser);
        }
    }

    public boolean replaceUser(DiscApplicationUser user, String tChannelName) {
        User twUser = null;
        for (int i = 0; i < users2.size(); i++) {
            DiscApplicationUser us = users2.get(i);
            if (us.getUserID().equals(user.getUserID())) {
                twUser = users.get(i);
                users2.remove(i);
                users.remove(i);
                if (twitchClient.getClientHelper().disableStreamEventListenerForId(twUser.getId()))
                    YukiLogger.log(
                            new YukiLogInfo("It seems like the twitch listener couldn't deactivate the twitch listener for " + user.getUsername()).error()
                    );
                break;
            }
        }

        if (twUser == null) {
            twUser = twitchClient.getClientHelper().enableStreamEventListener(tChannelName);
        }
        users.add(twUser);
        users2.add(user);
        return true;
    }

    public void removeUser(String userId) {
        for (int i = 0; i < users2.size(); i++) {
            DiscApplicationUser us = users2.get(i);
            if (us.getUserID().equals(userId)) {
                users2.remove(i);
                users.remove(i);
                break;
            }
        }
    }

    public void notifyTwitchLive(String userId, String channelLink, String liveUserName, String description, String game, String[] guild) {
        JDA botJDA = yukiSora.getDiscordApplication().getBotJDA();

        for (Guild g : botJDA.getGuilds()) {
            if (containsGuild(g.getId(), guild)) {
                DiscApplicationServer server = LibManager.retrieveServer(g, yukiSora);
                if (server == null) continue;

                Member m = g.retrieveMemberById(userId).complete();
                if (m == null) continue;

                String thUrl = retrieveThumbnailUrl(liveUserName);
                FileUpload upload = null;
                if (liveUserName == null) {
                    thUrl = m.getUser().getAvatarUrl();
                } else {
                    try {
                        upload = FileUpload.fromData(retrieveThumbnailFile(thUrl));
                    } catch (Exception ignored){
                    }
                }

                TextChannel tc = g.getTextChannelById(server.getTwitchNotifyChannelId());
                if (tc == null) continue;

                MessageCreateAction data = tc.sendMessage(new MessageCreateBuilder()
                        .setAllowedMentions(Collections.singleton(Message.MentionType.EVERYONE))
                        .setEmbeds(new EmbedBuilder()
                                .setAuthor(m.getEffectiveName() + " is streaming", null, m.getUser().getAvatarUrl())
                                .setColor(Color.decode("#6441a5"))
                                .setTitle(description, channelLink)
                                .setImage(liveUserName == null || upload == null ? thUrl : "attachment://tmp.jpg")
                                .setDescription("**Game**\n" + game).build())

                        .setContent("@everyone " + m.getEffectiveName() + " is now live on twitch, " + getRandomTwitchMessage())
                        .build());
                if(liveUserName == null || upload == null)
                    data.queue();
                else
                    data.addFiles(upload).queue();

            }
        }
    }

    private boolean containsGuild(String g, String[] gs) {
        for (String s : gs) {
            if (s.equals(g))
                return true;
        }
        return false;
    }

    private String getRandomTwitchMessage() {
        return switch (ThreadLocalRandom.current().nextInt(13)) {
            case 1 -> "check it out!";
            case 2 -> "why don't have a look?";
            case 3 -> "lets pay them a visit!";
            case 4 -> "go there and give them some cookies from me :cookie:";
            case 5 -> "maybe consider a follow?";
            case 6 -> "I heard they are only streaming the good stuff :D";
            case 7 -> "say hi from me!";
            case 8 -> "tell them, yuki says hi";
            case 9 -> "seems interesting!";
            case 10 -> "this is a high quality stream, I swear!";
            case 11 -> "maybe worth a lurk?";
            case 12 -> "might be worth a watch!";
            default -> "come and hang out!";
        };

    }

    private String retrieveThumbnailUrl(String liveUserName) {
        return "https://static-cdn.jtvnw.net/previews-ttv/live_user_" + liveUserName + "-" + "1280x720.jpg";
    }

    private File retrieveThumbnailFile(String url){
        File img = new File(de.mindcollaps.yuki.util.FileUtils.getHome() + "/tmp.jpg");
        try {
            FileUtils.copyURLToFile(new URL(url), img);
        } catch (IOException ignored) {
        }
        return img;
    }

    public class TwitchEventHandler {

        @EventSubscriber
        public void onChannelGoOnline(ChannelGoLiveEvent event) {
            int lastI = 0;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(event.getStream().getUserId())) {
                    lastI = i;
                    break;
                }
            }
            User u = users.get(lastI);
            DiscApplicationUser us = users2.get(lastI);
            JSONObject res = yukiSora.getApiManagerOld().getTwitchUserByDiscordId(us.getUserID());

            JSONObject data = (JSONObject) res.get("data");
            JSONArray array = (JSONArray) data.get("servers");
            String[] servrs = new String[array.size()];
            for (int i = 0; i < array.size(); i++) {
                String s = (String) array.get(i);
                servrs[i] = s;
            }
            notifyTwitchLive(us.getUserID(), "https://www.twitch.tv/" + event.getStream().getUserName(), event.getStream().getUserName(), event.getStream().getTitle(), event.getStream().getGameName(), servrs);
        }
    }
}
