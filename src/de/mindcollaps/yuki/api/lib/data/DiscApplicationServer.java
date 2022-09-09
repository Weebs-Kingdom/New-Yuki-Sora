package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteData;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.YukiUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

@RouteClass("disc-server")
public class DiscApplicationServer extends RouteData {

    @RouteField
    private String serverName = "";
    @RouteField
    private String serverId = "";

    @RouteField
    private String[] defaultRoles = new String[]{};

    @RouteField
    private String workChannelId;
    @RouteField
    private String shopChannelId;

    @RouteField
    private String twitchNotifyChannelId;
    @RouteField
    private String vipRoleId;
    @RouteField
    private String primeRoleId;
    @RouteField
    private String boosterRoleId;
    @RouteField
    private String boosterCategoryId;

    @RouteField
    private String certificationMessageId = "";
    @RouteField
    private String certificationChannelId = "";
    @RouteField
    private String defaultMemberRoleId = "";
    @RouteField
    private String defaultTempGamerRoleId = "";

    @RouteField
    private String welcomeMessageChannelId = "";

    @RouteField
    private String statisticsCategoryId = "";

    @RouteField
    private String[] autoChannels = new String[]{};
    @RouteField
    private String[] gamingChannels = new String[]{};

    public DiscApplicationServer() {
    }

    public void updateServerStats(YukiSora yukiSora) {
        if(YukiUtil.isEmpty(serverId))
            return;
        Guild g = yukiSora.getDiscordApplication().getBotJDA().getGuildById(serverId);
        if (g == null)
            return;
        if (YukiUtil.isEmpty(statisticsCategoryId))
            return;
        Category category = yukiSora.getDiscordApplication().getBotJDA().getCategoryById(statisticsCategoryId);
        if (category == null)
            return;

        int channels = category.getChannels().size();

        Role mrole = g.getPublicRole();
        if (mrole == null)
            return;

        for (int i = channels; i < 2; i++) {
            VoiceChannel nVC = category.createVoiceChannel("\uD83D\uDCCA Statistic ...").complete();
            nVC.upsertPermissionOverride(mrole).setAllowed(Permission.ALL_VOICE_PERMISSIONS).setDenied(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK).queue();
        }

        if (channels > 0)
            for (int i = 0; channels - i > 2; i++) {
                category.getChannels().get(i).delete().queue();
            }

        VoiceChannel vc = category.getVoiceChannels().get(0);
        VoiceChannel vc2 = category.getVoiceChannels().get(1);

        vc.getManager().setName("\uD83D\uDCCAMembers: " + g.getMemberCount()).queue();
        vc2.getManager().setName("\uD83E\uDD16Version: " + YukiProperties.getApplicationSettings().mvnVersion).queue();
    }

    public void addGamingChannel(String id) {
        gamingChannels = YukiUtil.addToArray(gamingChannels, id);
    }

    public void removeGamingChannel(String id) {
        gamingChannels = YukiUtil.removeFromArray(gamingChannels, id);
    }

    public void addAutoChannel(String id) {
        autoChannels = YukiUtil.addToArray(autoChannels, id);
    }

    public void removeAutoChannel(String id) {
        autoChannels = YukiUtil.removeFromArray(autoChannels, id);
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String[] getDefaultRoles() {
        return defaultRoles;
    }

    public void setDefaultRoles(String[] defaultRoles) {
        this.defaultRoles = defaultRoles;
    }

    public String getWorkChannelId() {
        return workChannelId;
    }

    public void setWorkChannelId(String workChannelId) {
        this.workChannelId = workChannelId;
    }

    public String getShopChannelId() {
        return shopChannelId;
    }

    public void setShopChannelId(String shopChannelId) {
        this.shopChannelId = shopChannelId;
    }

    public String getTwitchNotifyChannelId() {
        return twitchNotifyChannelId;
    }

    public void setTwitchNotifyChannelId(String twitchNotifyChannelId) {
        this.twitchNotifyChannelId = twitchNotifyChannelId;
    }

    public String getVipRoleId() {
        return vipRoleId;
    }

    public void setVipRoleId(String vipRoleId) {
        this.vipRoleId = vipRoleId;
    }

    public String getPrimeRoleId() {
        return primeRoleId;
    }

    public void setPrimeRoleId(String primeRoleId) {
        this.primeRoleId = primeRoleId;
    }

    public String getBoosterRoleId() {
        return boosterRoleId;
    }

    public void setBoosterRoleId(String boosterRoleId) {
        this.boosterRoleId = boosterRoleId;
    }

    public String getBoosterCategoryId() {
        return boosterCategoryId;
    }

    public void setBoosterCategoryId(String boosterCategoryId) {
        this.boosterCategoryId = boosterCategoryId;
    }

    public String getCertificationMessageId() {
        return certificationMessageId;
    }

    public void setCertificationMessageId(String certificationMessageId) {
        this.certificationMessageId = certificationMessageId;
    }

    public String getCertificationChannelId() {
        return certificationChannelId;
    }

    public void setCertificationChannelId(String certificationChannelId) {
        this.certificationChannelId = certificationChannelId;
    }

    public String getDefaultMemberRoleId() {
        return defaultMemberRoleId;
    }

    public void setDefaultMemberRoleId(String defaultMemberRoleId) {
        this.defaultMemberRoleId = defaultMemberRoleId;
    }

    public String getDefaultTempGamerRoleId() {
        return defaultTempGamerRoleId;
    }

    public void setDefaultTempGamerRoleId(String defaultTempGamerRoleId) {
        this.defaultTempGamerRoleId = defaultTempGamerRoleId;
    }

    public String getWelcomeMessageChannelId() {
        return welcomeMessageChannelId;
    }

    public void setWelcomeMessageChannelId(String welcomeMessageChannelId) {
        this.welcomeMessageChannelId = welcomeMessageChannelId;
    }

    public String getStatisticsCategoryId() {
        return statisticsCategoryId;
    }

    public void setStatisticsCategoryId(String statisticsCategoryId) {
        this.statisticsCategoryId = statisticsCategoryId;
    }

    public String[] getAutoChannels() {
        return autoChannels;
    }

    public void setAutoChannels(String[] autoChannels) {
        this.autoChannels = autoChannels;
    }

    public String[] getGamingChannels() {
        return gamingChannels;
    }

    public void setGamingChannels(String[] gamingChannels) {
        this.gamingChannels = gamingChannels;
    }
}
