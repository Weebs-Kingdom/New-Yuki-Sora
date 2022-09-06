package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.ForeignData;
import de.mindcollaps.yuki.api.lib.RouteClass;
import de.mindcollaps.yuki.api.lib.RouteData;
import de.mindcollaps.yuki.api.lib.RouteField;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.*;

@RouteClass("autochannel")
public class AutoChannel extends RouteData {

    @RouteField
    @ForeignData(DiscApplicationServer.class)
    private String server;

    @RouteField
    private String channelId;

    @RouteField
    private int autoChannelType;

    private AudioChannel audioChannel;
    private Member createdBy;
    private boolean wasRenamed;


    public AutoChannel recreate(AudioChannel vc, int type) {
        this.audioChannel = vc;
        this.autoChannelType = type;
        if (vc.getMembers().size() != 0)
            this.createdBy = vc.getMembers().get(0);

        return this;
    }

    public void init(YukiSora yukiSora) throws Exception {
        audioChannel = yukiSora.getDiscordApplication().getBotJDA().getGuildById(server).getVoiceChannelById(channelId);
    }

    public AudioChannel getVc() {
        return audioChannel;
    }

    public void rename(String name) {
        audioChannel.getManager().setName(name + " [AC]").queue();
    }

    public AutoChannel createAutoChan(VoiceChannel vc, Guild gc, Member m) {
        this.audioChannel = createNewAutoChannel(vc, gc, m, vc.getName());
        this.createdBy = m;
        this.autoChannelType = 0;
        return this;
    }

    public AutoChannel createGamingChan(VoiceChannel vc, Guild gc, Member m) {
        String name = DiscordUtil.getGame(m);
        if (name == null)
            name = "Gaming Lounge";
        this.audioChannel = createNewAutoChannel(vc, gc, m, name);
        this.createdBy = m;
        this.autoChannelType = 1;
        return this;
    }

    private AudioChannel createNewAutoChannel(VoiceChannel vc, Guild gc, Member m, String name) {
        if (m == null || vc == null)
            return null;
        else if (m.getVoiceState() == null)
            return null;
        else if (m.getVoiceState().getChannel() == null)
            return null;
        else if (!m.getVoiceState().getChannel().getId().equals(vc.getId()))
            return null;

        VoiceChannel nvc = gc.createVoiceChannel(name + " [AC]")
                .setBitrate(vc.getBitrate())
                .complete();

        if (vc.getParentCategory() != null)
            nvc.getManager().setParent(vc.getParentCategory()).queue();

        gc.modifyVoiceChannelPositions().selectPosition(nvc).moveTo(vc.getPosition() + 1).queue();
        for (PermissionOverride or : vc.getPermissionOverrides()) {
            nvc.upsertPermissionOverride(or.getRole()).setAllowed(or.getAllowed()).setDenied(or.getDenied()).complete();
        }
        try {
            gc.moveVoiceMember(m, nvc).complete();
        } catch (Exception e) {
            nvc.delete().queue();
        }
        return nvc;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getAutoChannelType() {
        return autoChannelType;
    }

    public void setAutoChannelType(int autoChannelType) {
        this.autoChannelType = autoChannelType;
    }

    public AudioChannel getAudioChannel() {
        return audioChannel;
    }

    public void setAudioChannel(AudioChannel audioChannel) {
        this.audioChannel = audioChannel;
    }

    public Member getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isWasRenamed() {
        return wasRenamed;
    }

    public void setWasRenamed(boolean wasRenamed) {
        this.wasRenamed = wasRenamed;
    }
}
