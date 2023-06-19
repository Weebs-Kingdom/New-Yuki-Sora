package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.*;

import java.time.Instant;
import java.util.Date;

@RouteClass("server-user")
public class ServerUser extends YukiRoute {

    @RouteField
    @ForeignData(DiscApplicationUser.class)
    private DatabaseId user = new DatabaseId();

    @RouteField
    @ForeignData(DiscApplicationServer.class)
    private DatabaseId serverId = new DatabaseId();

    @RouteField
    private boolean saidHello = false;

    @RouteField
    private boolean isBooster = false;

    @RouteField
    private String[] boosterChans = new String[]{};

    @RouteField
    private boolean isMember = false;

    @RouteField
    private Date dateBecomeMember = Date.from(Instant.now());

    @RouteField
    private boolean isTempMember = false;

    @RouteField
    private Date dateBecomeTempMember = Date.from(Instant.now());
    @RouteField
    private int xp = 0;
    @RouteField
    private int level = 1;

    public DatabaseId getUser() {
        return user;
    }

    public void setUser(DatabaseId user) {
        this.user = user;
    }

    public DatabaseId getServerId() {
        return serverId;
    }

    public void setServerId(DatabaseId serverId) {
        this.serverId = serverId;
    }

    public boolean isSaidHello() {
        return saidHello;
    }

    public void setSaidHello(boolean saidHello) {
        this.saidHello = saidHello;
    }

    public boolean isBooster() {
        return isBooster;
    }

    public void setBooster(boolean booster) {
        isBooster = booster;
    }

    public String[] getBoosterChans() {
        return boosterChans;
    }

    public void setBoosterChans(String[] boosterChans) {
        this.boosterChans = boosterChans;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public Date getDateBecomeMember() {
        return dateBecomeMember;
    }

    public void setDateBecomeMember(Date dateBecomeMember) {
        this.dateBecomeMember = dateBecomeMember;
    }

    public boolean isTempMember() {
        return isTempMember;
    }

    public void setTempMember(boolean tempMember) {
        isTempMember = tempMember;
    }

    public Date getDateBecomeTempMember() {
        return dateBecomeTempMember;
    }

    public void setDateBecomeTempMember(Date dateBecomeTempMember) {
        this.dateBecomeTempMember = dateBecomeTempMember;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
