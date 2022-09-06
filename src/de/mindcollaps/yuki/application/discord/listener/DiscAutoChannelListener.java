package de.mindcollaps.yuki.application.discord.listener;

import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.request.FindServerByGuildId;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public class DiscAutoChannelListener extends ListenerAdapter {

    public static ArrayList<AutoChannel> activeAutoChannels = new ArrayList<>();
    private final YukiSora yukiSora;

    public DiscAutoChannelListener(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (checkVc(event.getGuild(), event.getGuild().getVoiceChannelById(event.getChannelJoined().getId()), event.getEntity()))
            return;
        checkActivities(event.getMember());
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        if (event.getChannelType() == ChannelType.VOICE) {
            DiscApplicationServer server = new FindServerByGuildId(event.getGuild().getId()).makeRequestSingle(yukiSora);
            if (server == null) {
                YukiLogger.log(new YukiLogInfo("Can't find the server!!!", "[Guild Voice Join]").error());
                return;
            }

            for (String vc : server.getAutoChannels()) {
                if (vc.equals(event.getChannel().getId())) {
                    server.removeAutoChannel(vc);
                    server.removeGamingChannel(vc);
                }
            }

            Iterator<AutoChannel> iterator = activeAutoChannels.iterator();
            while (iterator.hasNext()) {
                AutoChannel ac = iterator.next();
                AudioChannel vc = ac.getVc();
                if (vc.getId().equals(event.getChannel().getId())) {
                    iterator.remove();
                    vc.delete().queue();
                }
            }
        }
    }

    @Override
    public void onUserActivityStart(@NotNull UserActivityStartEvent event) {
        checkActivities(event.getMember());
    }

    private boolean checkVc(Guild guild, VoiceChannel channelJoined, Member entity) {
        DiscApplicationServer server = new FindServerByGuildId(guild.getId()).makeRequestSingle(yukiSora);
        if (server == null) {
            YukiLogger.log(new YukiLogInfo("Can't find the server!!!", "[Guild Voice Join]").error());
            return false;
        }

        for (String vcI : server.getAutoChannels()) {
            if (vcI.equals(channelJoined.getId())) {
                VoiceChannel vc = channelJoined;
                AutoChannel ac = new AutoChannel().createAutoChan(vc, guild, entity);
                activeAutoChannels.add(ac);
                return true;
            }
        }

        for (String vcI : server.getGamingChannels()) {
            if (vcI.equals(channelJoined.getId())) {
                VoiceChannel vc = channelJoined;
                AutoChannel ac = new AutoChannel().createGamingChan(vc, guild, entity);
                activeAutoChannels.add(ac);
                return true;
            }
        }
        return false;
    }

    private void checkActivities(Member member) {
        AutoChannel ac = getAutoChan(member);
        if (ac != null) {
            if (ac.getAutoChannelType() == 1) {
                if (!ac.isWasRenamed()) {
                    ArrayList<String> games = new ArrayList<>();
                    String most = "";
                    int mostI = 0;

                    for (Member m : ac.getAudioChannel().getMembers()) {
                        String g = DiscordUtil.getGame(m);
                        if (g != null)
                            games.add(g);
                    }

                    for (String game : games) {
                        if (!game.equals(most)) {
                            int count = count(game, games);
                            if (count > mostI) {
                                most = game;
                                mostI = count;
                            }
                        }
                    }
                }
            }
        }
    }

    private int count(String c, ArrayList<String> list) {
        int i = 0;
        for (String s : list) {
            if (s.equals(c))
                i++;
        }
        return i;
    }

    private AutoChannel getAutoChan(Member m) {
        Iterator<AutoChannel> iterator = activeAutoChannels.iterator();
        while (iterator.hasNext()) {
            AutoChannel ac = iterator.next();
            AudioChannel vc = ac.getAudioChannel();
            AudioChannel svc = m.getVoiceState().getChannel();
            if (svc == null)
                return null;
            if (svc.getId().equals(vc.getId())) {
                return ac;
            }
        }
        return null;
    }

    public boolean renameAutoChannelByUser(User user, String newName) {
        for (Guild g : yukiSora.getDiscordApplication().getBotJDA().getGuilds()) {
            Member m = g.getMember(user);
            if (m == null)
                continue;
            AutoChannel ac = getAutoChan(m);
            if (ac == null)
                continue;
            ac.rename(newName);
            ac.setWasRenamed(true);
            return true;
        }
        return false;
    }


    private void testActiveVC(AudioChannel vc) {
        Iterator<AutoChannel> iterator = activeAutoChannels.iterator();
        while (iterator.hasNext()) {
            AutoChannel ac = iterator.next();
            AudioChannel vcc = null;
            try {
                vcc = ac.getAudioChannel().getGuild().getVoiceChannelById(ac.getAudioChannel().getId());
            } catch (Exception ignored) {
            }
            if (vcc == null) {
                iterator.remove();
                return;
            } else if (vc.getId().equals(vcc.getId()) && vc.getMembers().size() == 0) {
                iterator.remove();
                vc.delete().queue();
            }
        }
    }

    public ArrayList<String> getAutoChanList() {
        ArrayList<String> ids = new ArrayList<>();
        for (AutoChannel ac : activeAutoChannels) {
            AudioChannel vc = ac.getAudioChannel();
            ids.add(vc.getId());
        }
        return ids;
    }

    public void loadAutoChans(ArrayList<String> ids, DiscApplicationServer server) {
        for (String s : ids) {
            AudioChannel vc = null;
            try {
                vc = yukiSora.getDiscordApplication().getBotJDA().getVoiceChannelById(s);
            } catch (Exception e) {
                continue;
            }
            if (vc != null) {
                if (vc.getMembers().size() == 0) {
                    vc.delete().queue();
                } else {
                    AutoChannel ac = new AutoChannel().recreate(vc, 0);
                    activeAutoChannels.add(ac);
                }
            }
        }
    }
}
