package de.mindcollaps.yuki.application.discord.response;

import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@YukiLogModule(name = "Disc Response Handler")
public class ResponseHandler {
    private static final ArrayList<Response> responses = new ArrayList<>();

    private static TimerTask updater;

    public static void makeResponse(Response response) {
        responses.add(response);
    }

    public static void startUpdateThread() {
        if (updater != null) {
            YukiLogger.log(new YukiLogInfo("Response handler tried to start an update thread, but there is one running already!").warning());
            return;
        }
        YukiLogger.log(new YukiLogInfo("Response handler started update thread!").debug());
        Timer t = new Timer();
        updater = new TimerTask() {
            @Override
            public void run() {
                try {
                    while (responses.iterator().hasNext()) {
                        Response r = responses.iterator().next();
                        Instant now = new Date().toInstant();
                        now.minus(10, ChronoUnit.MINUTES);
                        if (now.isAfter(r.creationTime.toInstant())) {
                            YukiLogger.log(new YukiLogInfo("Response handler removed an response because it was too old!").debug());
                            responses.iterator().remove();
                        }
                    }
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("The response updater caught an error while checking an response!").trace(e));
                }
            }
        };
        t.schedule(updater, 10 * 10 * 60, 10 * 10 * 60 * 5);
    }

    public static boolean lookForResponse(MessageReceivedEvent update) {
        Response re = null;
        try {
            for (Response res : responses) {
                if (update.getAuthor().getId().equals(res.discUserId)) {
                    if (update.getChannel().getId().equals(res.discChannelId)) {
                        try {
                            if (!update.getGuild().getId().equals(res.discGuildId))
                                continue;
                        } catch (Exception ignored) {
                        }
                        re = res;

                        responses.remove(res);
                        res.onMessage(update);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("The response handler caught an error while performing an response action!").trace(e));

            if (re != null) {
                try {
                    re.onError(e);
                } catch (Exception ee) {
                    YukiLogger.log(new YukiLogInfo("The response handler caught an error while performing the onError() action").trace(e));
                }
            }
            return false;
        }
        return false;
    }

    public static boolean lookForResponse(MessageReactionAddEvent update) {
        Response re = null;
        try {
            for (Response res : responses) {
                if (update.getUserId().equals(res.discUserId)) {
                    if (update.getChannel().getId().equals(res.discChannelId)) {
                        if (update.getMessageId().equals(res.discMessageId)) {
                            try {
                                if (!update.getGuild().getId().equals(res.discGuildId))
                                    continue;
                            } catch (Exception ignored) {
                            }
                            re = res;
                            responses.remove(res);
                            res.onEmote(update);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            YukiLogger.log(new YukiLogInfo("The response handler caught an error while performing an response action!").trace(e));

            if (re != null) {
                try {
                    re.onError(e);
                } catch (Exception ee) {
                    YukiLogger.log(new YukiLogInfo("The response handler caught an error while performing the onError() action").trace(e));
                }
            }
            return false;
        }
        return false;
    }

    private int lookForCurrentTime() {
        Date now = new Date();
        String hours = String.valueOf(now.getHours());
        String minutes = String.valueOf(now.getMinutes());
        return Integer.valueOf(hours + minutes);
    }
}
