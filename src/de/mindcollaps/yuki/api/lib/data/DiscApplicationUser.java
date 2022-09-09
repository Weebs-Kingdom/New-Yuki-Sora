package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteData;
import de.mindcollaps.yuki.api.lib.route.RouteField;

import java.util.Arrays;
import java.util.Date;

@RouteClass("disc-user")
public class DiscApplicationUser extends RouteData {

    @RouteField
    private String[] servers = new String[]{};
    @RouteField
    private String username = "";
    @RouteField
    private String userID = "";
    @RouteField
    private boolean isBotAdmin = false;
    @RouteField
    private String lang = "en";

    @RouteField
    private Date lastWorkTime = null;
    @RouteField
    private Date lastDungeonVisit = null;
    @RouteField
    private long coins = 20;
    @RouteField
    private int xp = 0;
    @RouteField
    private int level = 1;

    @RouteField
    private int maxMonsters = 10;
    @RouteField
    private int maxItems = 30;

    @RouteField
    private boolean saidHello = false;

    @RouteField
    private boolean isBooster = false;

    @RouteField
    private String[] boosterChans = new String[]{};

    public DiscApplicationUser() {
    }

    public String[] getServers() {
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isBotAdmin() {
        return isBotAdmin;
    }

    public void setBotAdmin(boolean botAdmin) {
        this.isBotAdmin = botAdmin;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Date getLastWorkTime() {
        return lastWorkTime;
    }

    public void setLastWorkTime(Date lastWorkTime) {
        this.lastWorkTime = lastWorkTime;
    }

    public Date getLastDungeonVisit() {
        return lastDungeonVisit;
    }

    public void setLastDungeonVisit(Date lastDungeonVisit) {
        this.lastDungeonVisit = lastDungeonVisit;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
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

    public int getMaxMonsters() {
        return maxMonsters;
    }

    public void setMaxMonsters(int maxMonsters) {
        this.maxMonsters = maxMonsters;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
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

    @Override
    public String toString() {
        return "DiscApplicationUser{" +
                "servers=" + Arrays.toString(servers) +
                ", userName='" + username + '\'' +
                ", userId='" + userID + '\'' +
                ", admin=" + isBotAdmin +
                ", lang='" + lang + '\'' +
                ", lastWorkTime=" + lastWorkTime +
                ", lastDungeonVisit=" + lastDungeonVisit +
                ", coins=" + coins +
                ", xp=" + xp +
                ", level=" + level +
                ", maxMonsters=" + maxMonsters +
                ", maxItems=" + maxItems +
                ", saidHello=" + saidHello +
                ", isBooster=" + isBooster +
                ", boosterChans=" + Arrays.toString(boosterChans) +
                '}';
    }
}
