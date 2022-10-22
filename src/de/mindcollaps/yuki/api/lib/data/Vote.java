package de.mindcollaps.yuki.api.lib.data;

import de.mindcollaps.yuki.api.lib.request.FindVoteElementsByVoteId;
import de.mindcollaps.yuki.api.lib.route.ForeignData;
import de.mindcollaps.yuki.api.lib.route.RouteClass;
import de.mindcollaps.yuki.api.lib.route.RouteData;
import de.mindcollaps.yuki.api.lib.route.RouteField;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.YukiUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.awt.*;
import java.util.Arrays;

@RouteClass("vote")
public class Vote extends RouteData {

    @RouteField
    @ForeignData(DiscApplicationServer.class)
    private String server = "";

    @RouteField
    private String channelId = "";

    //0 = Voting, 1 = Role assignment, -1 undefined
    @RouteField
    private int voteType = -1;

    @RouteField
    private String voteTitle = "";

    @RouteField
    private int index = -1;

    @RouteField
    private String messageId = "";

    @RouteField
    private String voteColor = "";

    private VoteElement[] voteElements = new VoteElement[]{};

    public void deleteVote(GuildChannel channel){
        TextChannel tc = null;
        if(channel.getType() == ChannelType.TEXT)
            tc = (TextChannel) channel;
        else
            return;

        tc.deleteMessageById(messageId).queue();
    }

    public Message createNewVote(GuildChannel channel) {
        Message message = null;
        TextChannel tc = null;
        if (channel.getType() == ChannelType.TEXT)
            tc = (TextChannel) channel;
        else
            return null;

        message = tc.sendMessageEmbeds(
                        generateContent(
                                getVoteHead())
                                .build())
                .complete();

        return message;
    }

    public Message updateVote(GuildChannel channel) {
        Message message = null;
        TextChannel tc = null;
        if (channel.getType() == ChannelType.TEXT)
            tc = (TextChannel) channel;
        else
            return null;

        if (messageId == null) {
            return null;
        } else {
            message = tc.editMessageEmbedsById(messageId, generateContent(
                            getVoteHead())
                            .build())
                    .complete();
        }
        return message;
    }

    private EmbedBuilder generateContent(EmbedBuilder builder) {
        String content = "";
        if (voteElements != null)
            if (voteElements.length > 0)
                if (voteType == 0) {
                    for (VoteElement voteElement : voteElements) {
                        Emoji emoji = Emoji.fromFormatted(voteElement.getEmote());
                        content += emoji.getFormatted() + " " + voteElement.getDescription() + "\n`Votes: " + voteElement.getVotes() + "`" + "\n\n";
                    }
                    content = content.substring(0, content.length() - 1);
                } else {
                    for (VoteElement voteElement : voteElements) {
                        Emoji emoji = Emoji.fromFormatted(voteElement.getEmote());
                        content += emoji.getFormatted() + " " + voteElement.getDescription() + "\n";
                    }
                    content = content.substring(0, content.length() - 2);
                }

        if (content.length() == 0)
            content = "\n\n`!There are no Vote Elements yet!`\n\nYou can add them by using the **/vote add** command";

        builder.setDescription(content);

        return builder;
    }

    private EmbedBuilder getVoteHead() {
        Color c = null;
        if (Color.getColor(voteColor) == Color.ORANGE)
            if (voteType == 0) {
                c = Color.getHSBColor(300, 100, 50);
            } else {
                c = Color.decode("#ff8c00");
            }
        return new EmbedBuilder()
                .setTitle(voteTitle)
                .setColor(c);
    }

    public VoteElement[] loadVoteElements(YukiSora yukiSora) {
        FindVoteElementsByVoteId req = new FindVoteElementsByVoteId(this.getDatabaseId());
        voteElements = req.makeRequest(yukiSora);

        Arrays.sort(voteElements, (o1, o2) -> Integer.compare(o1.getIndex(), o2.getIndex()));
        return voteElements;
    }

    public void addVoteElement(VoteElement element) {
        voteElements = YukiUtil.addToArray(voteElements, element, VoteElement.class);
    }

    public void updateVoteElement(VoteElement element){
        for (int i = 0; i < voteElements.length; i++) {
            if(voteElements[i].getDatabaseId().equals(element.getDatabaseId())){
                voteElements[i] = element;
                break;
            }
        }
    }

    public VoteElement[] getVoteElements() {
        return voteElements;
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

    public int getVoteType() {
        return voteType;
    }

    public void setVoteType(int voteType) {
        this.voteType = voteType;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getVoteColor() {
        return voteColor;
    }

    public void setVoteColor(String voteColor) {
        this.voteColor = voteColor;
    }
}
