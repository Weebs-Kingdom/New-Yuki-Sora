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
                        re = res;

                        responses.remove(res);
                        res.onGuildMessage(update);
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

    public static boolean lookForPrivateResponse(MessageReceivedEvent update) {
        Response re = null;
        try {
            for (Response res : responses) {
                if (update.getAuthor().getId().equals(res.discUserId)) {
                    if (update.getChannel().getId().equals(res.discChannelId)) {
                        re = res;
                        responses.remove(res);
                        res.onPrivateMessage(update);
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
                if (update.getMember().getId().equals(res.discUserId)) {
                    if (update.getChannel().getId().equals(res.discChannelId)) {
                        re = res;
                        responses.remove(res);
                        res.onGuildEmote(update);
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

    public static boolean lookForPrivateResponse(MessageReactionAddEvent update) {
        Response re = null;
        try {
            for (Response res : responses) {
                if (update.getUser().getId().equals(res.discUserId)) {
                    if (update.getChannel().getId().equals(res.discChannelId)) {
                        re = res;
                        responses.remove(res);
                        res.onPrivateEmote(update);
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

    private int lookForCurrentTime() {
        Date now = new Date();
        String hours = String.valueOf(now.getHours());
        String minutes = String.valueOf(now.getMinutes());
        return Integer.valueOf(hours + minutes);
    }
}
