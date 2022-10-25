package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.application.discord.command.CommandAction;
import de.mindcollaps.yuki.application.discord.command.CommandOption;
import de.mindcollaps.yuki.application.discord.command.DiscCommand;
import de.mindcollaps.yuki.application.discord.command.SubCommand;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgument;
import de.mindcollaps.yuki.core.YukiProperties;
import de.mindcollaps.yuki.core.YukiSora;
import de.mindcollaps.yuki.util.FileUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.HashMap;

public class DiscCmdMusic extends DiscCommand {

    private final HashMap<String, String> channelMusicBots = new HashMap<>();

    public DiscCmdMusic() {
        super("m", "A command to play music from one of the music bots");
        CommandAction action = new CommandAction() {
            @Override
            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                return true;
            }

            @Override
            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                event.getChannel().sendMessageEmbeds(disputeCommand(event.getGuild(), event.getMember(), yukiSora, args)).queue();
            }

            @Override
            public boolean calledPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {
                return true;
            }

            @Override
            public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {
                Guild g = null;
                Member m = null;
                for (Guild gs : yukiSora.getDiscordApplication().getBotJDA().getGuilds()) {
                    m = gs.getMember(event.getAuthor());
                    if (m != null)
                        if (m.getVoiceState() != null) {
                            g = gs;
                            break;
                        }
                }
                if (g != null)
                    event.getChannel().sendMessageEmbeds(disputeCommand(g, m, yukiSora, args)).queue();
            }

            @Override
            public boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                return true;
            }

            @Override
            public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                if (event.getMember() == null)
                    return;
                event.getHook().sendMessageEmbeds((disputeCommand(event.getGuild(), event.getMember(), yukiSora, args))).queue();
            }
        };

        addSubcommands(
                new SubCommand("play", "Play your favorite songs")
                        .addOptions(
                                new CommandOption(OptionType.STRING, "YT search/YT link/Spotify link", "The link to your music")
                        )
                        .addAction(action)
        );

        addSubcommands(
                new SubCommand("skip", "Skip a song")
                        .addAction(action)
        );

        addSubcommands(
                new SubCommand("stop", "Stop your song")
                        .addAction(action)
        );

        addSubcommands(
                new SubCommand("queue", "Show the queue")
                        .addOptions(
                                new CommandOption(OptionType.NUMBER, "Site", "Show a different page of the queue if the queue has multiple pages").optional()
                        )
                        .addAction(action)
        );

        addSubcommands(
                new SubCommand("info", "Show the info of the current track")
                        .addAction(action)
        );

        addSubcommands(
                new SubCommand("shuffle", "Shuffle the queue")
                        .addAction(action)
        );

        addSubcommands(
                new SubCommand("add", "Add a song to the queue")
                        .addOptions(
                                new CommandOption(OptionType.STRING, "YT search/YT link/Spotify link", "The link to your music")
                        )
                        .addAction(action)
        );
    }

    private MessageEmbed disputeCommand(Guild g, Member member, YukiSora yukiSora, DiscCommandArgs args) {
        if (member.getVoiceState() != null) {
            if (member.getVoiceState().getChannel() == null)
                return new EmbedBuilder().setDescription("You are not in a valid Voice Channel!").setColor(Color.RED).build();
        } else
            return new EmbedBuilder().setDescription("You are not in a valid Voice Channel!").setColor(Color.RED).build();

        String[] slaves = YukiProperties.getMultipleProperties(YukiProperties.dPMusicSlaveArr);
        if (slaves.length >= 1) {
            String vcId = member.getVoiceState().getChannel().getId();
            String botUrl = "";
            if (channelMusicBots.containsKey(vcId)) {
                botUrl = channelMusicBots.get(vcId);
            } else {
                Object[] arr = channelMusicBots.values().toArray();
                for (String ss : slaves) {
                    boolean isFree = true;
                    for (Object o : arr) {
                        String s = (String) o;
                        if (s.equals(ss)) {
                            isFree = false;
                            break;
                        }
                    }
                    if (isFree) {
                        botUrl = ss;
                        break;
                    }
                }
                if (botUrl.equals("")) {
                    JSONObject o = new JSONObject();
                    o.put("guild", g.getId());
                    Object[] array = channelMusicBots.keySet().toArray();
                    for (Object ob : array) {
                        String s = channelMusicBots.get(ob);
                        String res = yukiSora.getNetworkManager().post(s + "/state", o.toJSONString(), null);
                        JSONObject obj = FileUtils.convertStringToJson(res);
                        String ev = (String) obj.get("response");
                        if (ev.equals("true")) {
                        } else if (ev.equals("false")) {
                            channelMusicBots.remove(ob);
                            botUrl = s;
                        }
                    }
                    if (botUrl.equals("")) {
                        return new EmbedBuilder().setDescription("There is no Music bot available").setColor(Color.RED).build();
                    }
                }
            }
            return makeSlaveRequest(g, member, vcId, botUrl, args, yukiSora);
        } else {
            return new EmbedBuilder().setDescription("There is no Music bot available").setColor(Color.RED).build();
        }
    }

    private MessageEmbed makeSlaveRequest(Guild g, Member m, String vcId, String botUrl, DiscCommandArgs args, YukiSora yukiSora) {
        String response = "";
        JSONObject r = new JSONObject();
        r.put("status", "200");
        JSONObject data = new JSONObject();
        if (g != null)
            data.put("guild", g.getId());
        if (m != null)
            data.put("member", m.getId());
        if (args != null)
            response = argsToString(args);
        data.put("inst", response);
        r.put("data", data);
        if (g != null && m != null && vcId != null && botUrl != null)
            channelMusicBots.put(vcId, botUrl);
        JSONObject req = FileUtils.convertStringToJson(yukiSora.getNetworkManager().post(botUrl + "/api", r.toJSONString(), null));
        if (g != null && m != null && vcId != null && botUrl != null)
            if (req == null) {
                return new EmbedBuilder().setDescription("The Music Bot slave throws an error").setColor(Color.RED).build();
            }
        if (g != null && m != null && vcId != null && botUrl != null)
            if (req.get("status").equals("200")) {
                return new EmbedBuilder().setDescription((String) req.get("response")).setColor(Color.GREEN).build();
            } else if (req.get("status").equals("400")) {
                return new EmbedBuilder().setDescription((String) req.get("response")).setColor(Color.RED).build();
            }

        return new EmbedBuilder().setDescription(((String) req.get("response")).replace("\\\"", "\"")).setColor(Color.RED).setAuthor("An error occurred!").build();
    }

    private String argsToString(DiscCommandArgs args) {
        StringBuilder s = new StringBuilder();
        for (DiscCommandArgument arg : args.getArray()) {
            s.append(arg.getString()).append(" ");
        }
        return s.toString();
    }

    @Override
    public String getHelp() {
        return null;
    }
}
