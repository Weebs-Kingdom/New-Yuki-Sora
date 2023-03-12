package de.mindcollaps.yuki.application.discord.command.special;

import de.mindcollaps.yuki.api.lib.data.AutoChannel;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.application.discord.command.ActionNotImplementedException;
import de.mindcollaps.yuki.application.discord.command.CommandAction;
import de.mindcollaps.yuki.application.discord.command.CommandOption;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SCmdRename extends SpecialCommand {


    public SCmdRename() {
        super("rn", "With this command you can rename the autochannel you are currently in");

        addOption(new CommandOption(OptionType.STRING, "newname", "The new name of the channel"));
        addAction(new CommandAction() {
            @Override
            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                action(event.getAuthor(), args.getArg(0).getString(), new TextUtil.ResponseInstance(event), yukiSora);
            }

            @Override
            public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                action(event.getAuthor(), args.getArg(0).getString(), new TextUtil.ResponseInstance(event), yukiSora);
            }
        });
    }

    private void action(User user, String newName, TextUtil.ResponseInstance res, YukiSora yukiSora) {
        boolean renamed = false;
        for (Guild guild : yukiSora.getDiscordApplication().getBotJDA().getGuilds()) {
            Member m;
            try {
                m = guild.retrieveMemberById(user.getId()).complete();
            } catch (Exception ignored) {
                continue;
            }

            if (m != null) {
                if (m.getVoiceState() != null) {
                    if (m.getVoiceState().getChannel() != null) {
                        if (m.getVoiceState().getChannel().getType() == ChannelType.VOICE) {
                            for (AutoChannel autoChannel : yukiSora.getDiscordApplication().getAutoChannelListener().getActiveAutoChannels()) {
                                if (autoChannel.getVoiceChannel().getId().equals(m.getVoiceState().getChannel().getId())) {
                                    autoChannel.rename(newName);
                                    renamed = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            }

        }

        if (renamed) {
            TextUtil.sendSuccess("The channel was renamed successfully to `" + newName + "`!", res);
        } else {
            TextUtil.sendError("You are not in an valid autochannel!", res);
        }
    }

    @Override
    public String getHelp() {
        return null;
    }
}
