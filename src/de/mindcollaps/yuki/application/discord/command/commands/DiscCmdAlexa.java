package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.application.discord.command.CommandAction;
import de.mindcollaps.yuki.application.discord.command.CommandOption;
import de.mindcollaps.yuki.application.discord.command.DiscCommand;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.json.simple.JSONObject;

public class DiscCmdAlexa extends DiscCommand {

    public DiscCmdAlexa() {
        super("alexa", "Alexa integration command");
        doNotCreateSlashCommand();

        addOption(new CommandOption(OptionType.STRING, "Token", "The token provided from alexa"));

        addAction(new CommandAction() {
            @Override
            public boolean calledPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {
                return true;
            }

            @Override
            public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {
                JSONObject res = yukiSora.getApiManagerOld().verifyAlexa(args.getArg(0).getString(), event.getAuthor().getId());

                long status = (long) res.get("status");

                if (status == 200)
                    TextUtil.sendSuccess("The token was correct. Please use the verify command with alexa again to complete the verification process", event.getChannel());
                else
                    TextUtil.sendError("The token was invalid!", event.getChannel());
            }
        });
    }

    @Override
    public String getHelp() {
        return null;
    }
}
