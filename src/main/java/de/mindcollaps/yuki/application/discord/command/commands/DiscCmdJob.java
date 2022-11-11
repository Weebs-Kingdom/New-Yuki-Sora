package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.application.discord.command.ActionNotImplementedException;
import de.mindcollaps.yuki.application.discord.command.CommandAction;
import de.mindcollaps.yuki.application.discord.command.DiscCommand;
import de.mindcollaps.yuki.application.discord.command.SubCommand;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.response.Response;
import de.mindcollaps.yuki.application.discord.response.ResponseHandler;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class DiscCmdJob extends DiscCommand {

    public DiscCmdJob() {
        super("job", "With this command you can manage your job");

        addSubcommands(
                new SubCommand("work", "Take a job to work in")
                        .addAction(
                                new CommandAction() {
                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        work(user, yukiSora, new TextUtil.ResponseInstance(event));
                                    }

                                    @Override
                                    public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        work(user, yukiSora, new TextUtil.ResponseInstance(event));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        work(user, yukiSora, new TextUtil.ResponseInstance(event));
                                    }
                                }
                        )
        );

        addSubcommands(
                new SubCommand("take", "Take a job to work at")
                        .addAction(new CommandAction() {
                            @Override
                            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                takeJob((TextChannel) event.getChannel(), event.getAuthor(), yukiSora, new TextUtil.ResponseInstance(event));
                            }

                            @Override
                            public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                takeJob((TextChannel) event.getChannel(), event.getAuthor(), yukiSora, new TextUtil.ResponseInstance(event));
                            }

                            @Override
                            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                takeJob((TextChannel) event.getChannel(), event.getUser(), yukiSora, new TextUtil.ResponseInstance(event));
                            }
                        })
        );
    }

    private void work(DiscApplicationUser user, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        JSONObject workRes = yukiSora.getApiManagerOld().work(user.getUserID());
        if (workRes == null) {
            TextUtil.sendError("Seems like you can't work right now. Try to select a job if you don't have yet!", res);
            return;
        }
        //TODO: BITTE mach das schön....
        if (((Long) workRes.get("status")) == 200) {
            TextUtil.sendMessageEmbed(
                    new EmbedBuilder().setColor(Color.GREEN).setDescription("You've got " + (workRes.get("data") + " weboos")).build(),
                    res
            );
        } else {
            TextUtil.sendMessageEmbed(
                    new EmbedBuilder().setColor(Color.RED).setDescription((String) workRes.get("message")).build(),
                    res
            );
        }
    }

    private void takeJob(TextChannel tc, User user, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        JSONObject jbs = yukiSora.getApiManagerOld().getJobs();
        JSONArray jbss = (JSONArray) jbs.get("data");
        String jbS = "Select one of the following jobs\n\n";
        for (int i = 0; i < jbss.size(); i++) {
            JSONObject ob = (JSONObject) jbss.get(i);
            jbS += "[" + i + "] Work as " + ob.get("doing") + " at " + ob.get("jobName") + "[" + ob.get("shortName") + "]\n";
        }

        Response r = new Response() {
            @Override
            public void onGuildMessage(MessageReceivedEvent respondingEvent) {
                stepTwo(yukiSora, respondingEvent.getAuthor(), respondingEvent.getMessage().getContentRaw(), jbss, respondingEvent.getChannel());
            }

            @Override
            public void onPrivateMessage(MessageReceivedEvent respondingEvent) {
                stepTwo(yukiSora, respondingEvent.getAuthor(), respondingEvent.getMessage().getContentRaw(), jbss, respondingEvent.getChannel());
            }
        };
        r.discChannelId = tc.getId();
        r.discUserId = user.getId();
        ResponseHandler.makeResponse(r);
        TextUtil.sendMessageEmbed(
                new EmbedBuilder().setColor(Color.BLUE).setDescription(jbS).setAuthor("Jobs").build(),
                res
        );
    }

    private void stepTwo(YukiSora yukiSora, User user, String message, JSONArray jbss, MessageChannelUnion channel) {
        int id = Integer.parseInt(message);
        JSONObject o = (JSONObject) jbss.get(id);
        String idd = (String) o.get("_id");

        yukiSora.getApiManagerOld().giveUserAJob(user.getId(), idd, "trainee");
        channel.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.green)
                        .setDescription("You work as " + o.get("doing") + " at " + o.get("jobName")).build()
        ).queue();
    }

    @Override
    public String getHelp() {
        return null;
    }
}
