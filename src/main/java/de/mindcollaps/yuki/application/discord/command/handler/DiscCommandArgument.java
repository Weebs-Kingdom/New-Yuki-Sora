package de.mindcollaps.yuki.application.discord.command.handler;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class DiscCommandArgument {

    private final String name;
    private final OptionType optionType;

    private String string;
    private int integer;
    private double aDouble;
    private boolean aBoolean;
    private User user;
    private GuildChannel guildChannel;
    private VoiceChannel voiceChannel;
    private Role role;

    public DiscCommandArgument(String name, OptionType optionType) {
        this.name = name;
        this.optionType = optionType;
    }

    public String getName() {
        return name;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GuildChannel getGuildChannel() {
        return guildChannel;
    }

    public void setGuildChannel(GuildChannel guildChannel) {
        this.guildChannel = guildChannel;
    }

    public VoiceChannel getVoiceChannel() {
        return voiceChannel;
    }

    public void setVoiceChannel(VoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
