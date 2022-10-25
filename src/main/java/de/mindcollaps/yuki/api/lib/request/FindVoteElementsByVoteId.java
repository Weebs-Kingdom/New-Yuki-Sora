package de.mindcollaps.yuki.api.lib.request;

import de.mindcollaps.yuki.api.lib.data.Vote;
import de.mindcollaps.yuki.api.lib.data.VoteElement;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.api.lib.route.RouteRequest;

@RouteClass("findVoteElementsByVote")
public class FindVoteElementsByVoteId extends RouteRequest<VoteElement> {

    @RouteField
    private String vote;

    public FindVoteElementsByVoteId(String vote) {
        super(VoteElement.class);
        this.vote = vote;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
