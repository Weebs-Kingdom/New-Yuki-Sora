package de.mindcollaps.yuki.api;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.FileUtils;

public class ApiManagerOld {

    private final String apiToken;
    private final YukiSora yukiSora;
    private final String api;

    public ApiManagerOld(YukiSora engine) {
        api = YukiProperties.getProperties(YukiProperties.dPDbApiUrlOld);
        JSONObject se = null;
        apiToken = YukiProperties.getProperties(YukiProperties.dPDbApiToken);
        this.yukiSora = engine;
    }

    public JSONObject getServerById(String id) {
        JSONObject req = new JSONObject();
        req.put("sid", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getServer", req.toJSONString(), apiToken));
    }

    public JSONObject getUserBy(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getUser", req.toJSONString(), apiToken));
    }

    public JSONObject getUserInventoryById(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getUserInventory", req.toJSONString(), apiToken));
    }

    public JSONObject getUserMonstersById(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getUserMonsters", req.toJSONString(), apiToken));
    }

    public JSONObject getUserMonsterByIds(String uid, String mid) {
        JSONObject req = new JSONObject();
        req.put("id", uid);
        req.put("mid", mid);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getUserMonster", req.toJSONString(), apiToken));
    }

    public JSONObject dmgOnMonster(String uid, String mid, long dmg) {
        JSONObject req = new JSONObject();
        req.put("id", uid);
        req.put("mid", mid);
        req.put("dmg", dmg);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/dmgOnMonster", req.toJSONString(), apiToken));
    }

    public JSONObject xpOnMonster(String mid, long xp) {
        JSONObject req = new JSONObject();
        req.put("mid", mid);
        req.put("xp", xp);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/xpOnMonster", req.toJSONString(), apiToken));
    }

    public JSONObject addItemToUser(String uidItem, String id, int amount) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        req.put("item", uidItem);
        req.put("amount", amount);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/userItem", req.toJSONString(), apiToken));
    }

    public JSONObject removeItemFromUser(String uidItem, String id, int amount) {
        JSONObject req = new JSONObject();
        amount = amount * -1;
        req.put("id", id);
        req.put("item", uidItem);
        req.put("amount", amount);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/userItem", req.toJSONString(), apiToken));
    }

    public JSONObject work(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/work", req.toJSONString(), apiToken));
    }

    public JSONObject giveUserAJob(String id, String uidJob, String jobPos) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        req.put("job", uidJob);
        req.put("jPos", jobPos);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/userJob", req.toJSONString(), apiToken));

    }

    public JSONObject removeUserAJob(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().delete(api + "/userJob", req.toJSONString(), apiToken));
    }

    public JSONObject giveCoinsToUser(String id, int coins) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        req.put("coins", coins);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/coins", req.toJSONString(), apiToken));
    }

    public JSONObject removeCoinsFromUser(String id, int coins) {
        JSONObject req = new JSONObject();
        int c = coins * -1;
        req.put("id", id);
        req.put("coins", c);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/coins", req.toJSONString(), apiToken));
    }

    public JSONObject createUser(User user, String certLvl) {
        JSONObject req = new JSONObject();
        req.put("username", user.getName());
        req.put("userID", user.getId());
        req.put("certLevel", certLvl);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/user", req.toJSONString(), apiToken));

    }

    public JSONObject createServer(JSONObject object) {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/server", object.toJSONString(), apiToken));
    }

    public JSONObject createUser(JSONObject user) {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/user", user.toJSONString(), apiToken));
    }

    public JSONObject createServer(Guild guild) {
        JSONObject req = new JSONObject();
        req.put("serverName", guild.getName());
        req.put("serverId", guild.getId());
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/server", req.toJSONString(), apiToken));
    }

    public JSONObject getJobs() {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().get(api + "/job", apiToken));
    }

    public JSONObject getUserJobAndJobFromUser(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getUserJob", req.toJSONString(), apiToken));
    }

    public JSONObject getMonsters() {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().get(api + "/monster", apiToken));
    }

    public JSONObject userRandomMonster(String id, String minRarity) {
        JSONObject req = new JSONObject();
        if (minRarity != null)
            req.put("rarity", minRarity);
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/userRandomMonster", req.toJSONString(), apiToken));
    }

    public JSONObject patchUser(JSONObject json, String id) {
        JSONObject req = new JSONObject();
        req.put("data", json);
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().patch(api + "/user", req.toJSONString(), apiToken));
    }

    public JSONObject patchServer(JSONObject json, String id) {
        JSONObject req = new JSONObject();
        req.put("data", json);
        req.put("sid", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().patch(api + "/server", req.toJSONString(), apiToken));
    }

    public JSONObject getUsers() {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().get(api + "/user", apiToken));
    }

    public JSONObject verifyAlexa(String token, String userid) {
        JSONObject req = new JSONObject();
        req.put("user", userid);
        req.put("token", token);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/userAlexaVerify", req.toJSONString(), apiToken));
    }

    public JSONObject addAutochannelToServer(String id, String chanId) {
        JSONObject dat = new JSONObject();
        JSONObject server = getServerById(id);
        if ((Integer) server.get("status") == 200) {
            JSONObject data = (JSONObject) server.get("data");
            JSONArray chans = (JSONArray) data.get("autoChannelIds");

            chans.add(chanId);
            dat.put("autoChannelIds", chans);

            return patchServer(dat, id);
        }
        return getErrorJson();
    }

    public JSONObject removeAutochannel(String id, String chanId) {
        JSONObject dat = new JSONObject();
        JSONObject server = getServerById(id);
        if ((Integer) server.get("status") == 200) {
            JSONObject data = (JSONObject) server.get("data");
            JSONArray chans = (JSONArray) data.get("autoChannelIds");

            chans.removeIf(e -> e.equals(chanId));
            dat.put("autoChannelIds", chans);

            return patchServer(dat, id);
        }
        return getErrorJson();
    }

    public JSONObject createAiFight(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/createFight", req.toJSONString(), apiToken));
    }

    public JSONObject fight(String id, boolean ai1, boolean ai2, String m1, String m2, String attack, String slot) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        if (m1 != null)
            req.put("monster1", m1);
        if (m2 != null)
            req.put("monster2", m2);
        if (slot != null)
            req.put("slot", slot);

        req.put("ai1", ai1);
        req.put("ai2", ai2);

        if (attack != null)
            req.put("attack", attack);

        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/fight", req.toJSONString(), apiToken));
    }

    public JSONObject getAttacks(String mid) {
        JSONObject req = new JSONObject();
        req.put("monster", mid);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getAttacks", req.toJSONString(), apiToken));
    }

    public JSONObject getAttacks() {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().get(api + "/attack", apiToken));
    }

    public JSONObject giveRandomItem(String id, int amount, String rar) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        req.put("amount", amount);
        if (rar != null)
            req.put("rarity", rar);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/giveRandomItem", req.toJSONString(), apiToken));
    }

    public JSONObject getAttacksByUserMonster(String mid) {
        JSONObject req = new JSONObject();
        req.put("monster", mid);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getAttacksByUserMonster", req.toJSONString(), apiToken));
    }

    public JSONObject getUpdatedUsers() {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().get(api + "/getUser", apiToken));
    }

    public JSONObject giveMonsterAttack(String mid, String slot, String attackId) {
        JSONObject req = new JSONObject();
        req.put("monster", mid);
        req.put("slot", slot);
        req.put("attack", attackId);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/selectAttack", req.toJSONString(), apiToken));
    }

    public JSONObject removeUserMonster(String mid) {
        JSONObject req = new JSONObject();
        req.put("mid", mid);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().delete(api + "/userMonster", req.toJSONString(), apiToken));
    }

    public JSONObject feedMonster(String mid) {
        JSONObject req = new JSONObject();
        req.put("mid", mid);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/feedMonster", req.toJSONString(), apiToken));
    }

    public JSONObject healMonster(String mid) {
        JSONObject req = new JSONObject();
        req.put("mid", mid);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/healMonster", req.toJSONString(), apiToken));
    }

    public JSONObject getRandomTopic(String tp, boolean nsfw) {
        JSONObject req = new JSONObject();
        req.put("topic", tp);
        req.put("nsfw", nsfw);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getRandomTopic", req.toJSONString(), apiToken));
    }

    public JSONObject getTopicCategories() {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().get(api + "/topiccategory", apiToken));
    }

    public JSONObject getUserLevelInfo(String id, String img) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        req.put("img", img);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getLevelInfo", req.toJSONString(), apiToken));
    }

    public JSONObject getTwitchUserByDiscordId(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/getTwitchUserConByUser", req.toJSONString(), apiToken));
    }

    public JSONObject addTwitchUser(String id, String channel, String[] servers) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        req.put("twitch", channel);
        JSONArray srs = new JSONArray();
        for (String server : servers) {
            srs.add(server);
        }
        req.put("servers", srs);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/addTwitchUser", req.toJSONString(), apiToken));
    }

    public JSONObject removeTwitchUser(String id) {
        JSONObject req = new JSONObject();
        req.put("id", id);
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(api + "/removeTwitchUser", req.toJSONString(), apiToken));
    }

    public JSONObject getTwitchUsers() {
        return FileUtils.convertStringToJson(yukiSora.getNetworkManager().get(api + "/getTwitchUserCons", apiToken));
    }

    private JSONObject getErrorJson() {
        JSONObject o = new JSONObject();
        o.put("status", 400);
        o.put("message", "request failed!");
        return o;
    }
}