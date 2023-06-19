package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.*;
import de.mindcollaps.yuki.application.discord.util.DiscordUtil;
import de.mindcollaps.yuki.console.log.YukiLogInfo;
import de.mindcollaps.yuki.console.log.YukiLogger;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
@RouteClass("autochannel")
public class AutoChannel extends YukiRoute {

    @RouteField
    @ForeignData(DiscApplicationServer.class)
    private DatabaseId server = new DatabaseId();

    @RouteField
    private String channelId = "";

    //0 = auto, 1 = gaming
    @RouteField
    private int autoChannelType = 0;

    private boolean initialized = false;

    private VoiceChannel voiceChannel;
    private Member createdBy;
    private boolean wasRenamed;
    private Guild guild;

    public AutoChannel() {
    }

    public AutoChannel(String channelId) {
        this.channelId = channelId;
    }

    public AutoChannel(VoiceChannel voiceChannel) {
        this.guild = voiceChannel.getGuild();
        this.voiceChannel = voiceChannel;
        this.channelId = voiceChannel.getId();
        this.initialized = true;
    }

    public AutoChannel(DatabaseId server, String channelId, int autoChannelType) {
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

    public void init(Guild g, YukiSora yukiSora) {
        this.guild = g;
        voiceChannel = guild.getVoiceChannelById(channelId);
        if (voiceChannel == null)
            return;
        initialized = true;
    }

    public void initBaseChannel(Guild g, YukiSora yukiSora) {
        this.guild = g;
        voiceChannel = guild.getVoiceChannelById(channelId);
        if (voiceChannel == null)
            return;
        voiceChannel.getManager().setName("➕ Create Channel").queue();
        voiceChannel.getManager().setUserLimit(1).queue();
        initialized = true;
    }

    public AudioChannel getVc() {
        return voiceChannel;
    }

    public void rename(String name) {
        voiceChannel.getManager().setName(name + " [AC]").queue();
        wasRenamed = true;
    }

    @Nullable
    public AutoChannel createChildAutoChannel(Member m) {
        if (!initialized)
            return null;

        String name = "";
        if (autoChannelType == 0) {
            name = "Gaming Lounge";
        } else {
            name = DiscordUtil.getGame(m);
            if (name == null)
                name = "Gaming Lounge";
        }

        VoiceChannel channel = createNewAutoChannel(voiceChannel, m.getGuild(), m, name);

        if (channel == null)
            return null;

        AutoChannel newAutoChannel = new AutoChannel(channel);
        newAutoChannel.setCreatedBy(m);
        newAutoChannel.setServer(server);
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

        //Not necessary anymore
        //gc.modifyVoiceChannelPositions().selectPosition(nvc).moveTo(baseVoiceChannel.getPosition() + 1).queue();
        for (PermissionOverride or : baseVoiceChannel.getPermissionOverrides()) {
            if (or.getRole() != null)
                nvc.upsertPermissionOverride(or.getRole()).setAllowed(or.getAllowed()).setDenied(or.getDenied()).complete();
            else if (or.getMember() != null)
                nvc.upsertPermissionOverride(or.getMember()).setAllowed(or.getAllowed()).setDenied(or.getDenied()).complete();
            else
                nvc.upsertPermissionOverride(gc.getPublicRole()).setAllowed(or.getAllowed()).setDenied(or.getDenied()).complete();
        }

        try {
            gc.moveVoiceMember(m, nvc).complete();
        } catch (Exception e) {
            nvc.delete().queue();
            YukiLogger.log(new YukiLogInfo("Failed to move member to new auto channel").trace(e));
            return null;
        }
        return nvc;
    }

    public DatabaseId getServer() {
        return server;
    }

    public void setServer(DatabaseId server) {
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

    public boolean wasRenamed() {
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

    public boolean isInitialized() {
        return initialized;
    }
}
