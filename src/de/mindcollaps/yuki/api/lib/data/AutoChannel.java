package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.ForeignData;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteData;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RouteClass("autochannel")
public class AutoChannel extends RouteData {

    @RouteField
    @ForeignData(DiscApplicationServer.class)
    private String server;

    @RouteField
    private String channelId;

    //0 = auto, 1 = gaming
    @RouteField
    private int autoChannelType;

    private boolean initialized = false;

    private VoiceChannel voiceChannel;
    private Member createdBy;
    private boolean wasRenamed;
    private Guild guild;

    public AutoChannel() {
    }

    public AutoChannel(String channelId){
        this.channelId = channelId;
    }

    public AutoChannel(VoiceChannel voiceChannel){
        this.guild = voiceChannel.getGuild();
        this.voiceChannel = voiceChannel;
        this.channelId = voiceChannel.getId();
        this.initialized = true;
    }

    public AutoChannel(String server, String channelId, int autoChannelType) {
        this.server = server;
        this.channelId = channelId;
        this.autoChannelType = autoChannelType;
    }

    public AutoChannel recreate(VoiceChannel vc, int type) {
        this.voiceChannel = vc;
        this.guild = vc.getGuild();
        this.autoChannelType = type;
        if (vc.getMembers().size() != 0)
            this.createdBy = vc.getMembers().get(0);

        return this;
    }

    public void init(Guild g, YukiSora yukiSora) throws Exception {
        this.guild = g;
        voiceChannel = guild.getVoiceChannelById(channelId);
        initialized = true;
    }

    public void initBaseChannel(Guild g, YukiSora yukiSora) throws Exception {
        this.guild = g;
        voiceChannel = guild.getVoiceChannelById(channelId);
        voiceChannel.getManager().setName(":heavy_plus_sign: Create Channel").queue();
        voiceChannel.getManager().setUserLimit(1).queue();
        initialized = true;
    }

    public AudioChannel getVc() {
        return voiceChannel;
    }

    public void rename(String name) {
        voiceChannel.getManager().setName(name + " [AC]").queue();
    }

    @Nullable
    public AutoChannel createChildAutoChannel(Member m) {
        if(!initialized)
            return null;

        String name = "";
        if(autoChannelType == 0){
                name = "Gaming Lounge";
        } else {
            name = DiscordUtil.getGame(m);
            if(name == null)
                name = "Gaming Lounge";
        }

        VoiceChannel channel = createNewAutoChannel(voiceChannel, m.getGuild(), m, name);

        AutoChannel newAutoChannel = new AutoChannel(channel);
        newAutoChannel.setCreatedBy(m);
        return newAutoChannel;
    }

    private VoiceChannel createNewAutoChannel(VoiceChannel baseVoiceChannel, Guild gc, Member m, String name) {
        if (m == null || baseVoiceChannel == null)
            return null;
        else if (m.getVoiceState() == null)
            return null;
        else if (m.getVoiceState().getChannel() == null)
            return null;
        else if (!m.getVoiceState().getChannel().getId().equals(baseVoiceChannel.getId()))
            return null;

        VoiceChannel nvc = gc.createVoiceChannel(name + " [AC]")
                .setBitrate(baseVoiceChannel.getBitrate())
                .complete();

        if (baseVoiceChannel.getParentCategory() != null)
            nvc.getManager().setParent(baseVoiceChannel.getParentCategory()).queue();

        gc.modifyVoiceChannelPositions().selectPosition(nvc).moveTo(baseVoiceChannel.getPosition() + 1).queue();
        for (PermissionOverride or : baseVoiceChannel.getPermissionOverrides()) {
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

    public VoiceChannel getVoiceChannel() {
        return voiceChannel;
    }

    public void setVoiceChannel(VoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
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

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}
