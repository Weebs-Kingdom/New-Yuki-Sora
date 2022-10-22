package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.route.ForeignData;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteData;
import de.mindcollaps.yuki.api.lib.route.RouteField;

@RouteClass("vote-element")
public class VoteElement extends RouteData {

    @RouteField
    @ForeignData(Vote.class)
    private String vote;

    @RouteField
    private String roleId;

    @RouteField
    private String description;

    @RouteField
    private String emote;

    @RouteField
    private int index;

    @RouteField
    private int votes;

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmote() {
        return emote;
    }

    public void setEmote(String emote) {
        this.emote = emote;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
