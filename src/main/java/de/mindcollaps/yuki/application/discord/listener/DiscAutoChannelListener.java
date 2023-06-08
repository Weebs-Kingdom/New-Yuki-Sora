package de.mindcollaps.yuki.application.discord.listener;

import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.request.FindAutoChannelsByGuildId;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogModule;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("unused")
@YukiLogModule(name = "Disc Listener - Auto Channel")
public class DiscAutoChannelListener extends ListenerAdapter {

    public static final ArrayList<AutoChannel> activeAutoChannels = new ArrayList<>();
    private final YukiSora yukiSora;

    public DiscAutoChannelListener(YukiSora yukiSora) {
        this.yukiSora = yukiSora;
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        if (event.getChannelJoined() == null) {
            onGuildVoiceLeave(event);
        } else if (event.getChannelLeft() == null) {
            onGuildVoiceJoin(event);
        } else {
            onGuildVoiceMove(event);
        }
    }

    public void onGuildVoiceJoin(GuildVoiceUpdateEvent event) {
        if (checkBaseAcAndAdd(event.getGuild(), event.getGuild().getVoiceChannelById(event.getChannelJoined().getId()), event.getEntity()))
            return;
        checkActivities(event.getMember());
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        if (event.getChannelType() == ChannelType.VOICE) {
            AutoChannel[] autoChannels = new FindAutoChannelsByGuildId(event.getGuild().getId()).makeRequest(yukiSora);

            for (AutoChannel ac : autoChannels) {
                if (ac.getChannelId().equals(event.getChannel().getId())) {
                    ac.deleteData(yukiSora);
                }
            }
        }
    }

    public void onGuildVoiceMove(GuildVoiceUpdateEvent event) {
        if (event.getChannelLeft().getMembers().size() == 0) {
            checkAcAndRemove(event.getChannelLeft());
        }
        if (checkBaseAcAndAdd(event.getGuild(), event.getGuild().getVoiceChannelById(event.getChannelJoined().getId()), event.getEntity()))
            return;
        checkActivities(event.getMember());
    }

    public void onGuildVoiceLeave(GuildVoiceUpdateEvent event) {
        if (event.getChannelLeft().getMembers().size() == 0) {
            checkAcAndRemove(event.getChannelLeft());
        }
    }

    @Override
    public void onUserActivityStart(@NotNull UserActivityStartEvent event) {
        checkActivities(event.getMember());
    }

    private boolean checkBaseAcAndAdd(Guild guild, VoiceChannel channelJoined, Member entity) {
        AutoChannel[] autoChannels = new FindAutoChannelsByGuildId(guild.getId()).makeRequest(yukiSora);

        for (AutoChannel ac : autoChannels) {
            if (ac.getChannelId().equals(channelJoined.getId())) {
                try {
                    ac.init(guild, yukiSora);
                } catch (Exception e) {
                    YukiLogger.log(new YukiLogInfo("Failed to initialize Auto Channel!").trace(e));
                    return false;
                }
                AutoChannel newAutoChannel = ac.createChildAutoChannel(entity);
                if (newAutoChannel == null)
                    return false;

                activeAutoChannels.add(newAutoChannel);
                return true;
            }
        }
        return false;
    }

    private void checkAcAndRemove(AudioChannel voiceChannel) {
        for (int i = 0; i < activeAutoChannels.size(); i++) {
            AutoChannel activeAutoChannel = activeAutoChannels.get(i);
            if (activeAutoChannel.getGuild().getId().equals(activeAutoChannel.getGuild().getId())) {
                if (activeAutoChannel.getVoiceChannel().getId().equals(voiceChannel.getId())) {
                    if (voiceChannel.getMembers().size() == 0) {
                        voiceChannel.delete().queue();
                        activeAutoChannels.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void checkActivities(Member member) {
        AutoChannel ac = getAutoChan(member);
        if (ac != null) {
            if (ac.getAutoChannelType() == 1) {
                if (!ac.wasRenamed()) {
                    ArrayList<String> games = new ArrayList<>();
                    String most = "";
                    int mostI = 0;

                    for (Member m : ac.getVoiceChannel().getMembers()) {
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
            AudioChannel vc = ac.getVoiceChannel();
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
                vcc = ac.getVoiceChannel().getGuild().getVoiceChannelById(ac.getVoiceChannel().getId());
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

    public ArrayList<String> getAutoChanStringList() {
        ArrayList<String> ids = new ArrayList<>();
        for (AutoChannel ac : activeAutoChannels) {
            AudioChannel vc = ac.getVoiceChannel();
            ids.add(vc.getId());
        }
        return ids;
    }

    public ArrayList<AutoChannel> getActiveAutoChannels() {
        return activeAutoChannels;
    }

    public void loadAutoChans(ArrayList<String> ids, Guild g, DiscApplicationServer server, YukiSora yukiSora) {
        for (String s : ids) {
            VoiceChannel vc = null;
            try {
                vc = yukiSora.getDiscordApplication().getBotJDA().getVoiceChannelById(s);
            } catch (Exception e) {
                continue;
            }
            if (vc != null) {
                if (vc.getMembers().size() == 0) {
                    vc.delete().queue();
                } else {
                    AutoChannel ac = new AutoChannel(vc).recreate(vc, 0);
                    activeAutoChannels.add(ac);
                }
            }
        }
        AutoChannel[] autoChannels = new FindAutoChannelsByGuildId(g.getId()).makeRequest(yukiSora);
        for (AutoChannel ac : autoChannels) {
            ac.init(g, yukiSora);
            if (ac.isInitialized())
                if (ac.getVoiceChannel().getMembers().size() != 0) {
                    AutoChannel child = ac.createChildAutoChannel(ac.getVoiceChannel().getMembers().get(0));
                    if (!child.isInitialized())
                        continue;
                    activeAutoChannels.add(child);
                    for (Member member : ac.getVoiceChannel().getMembers()) {
                        g.moveVoiceMember(member, child.getVoiceChannel()).queue();
                    }
                }
        }
    }
}
