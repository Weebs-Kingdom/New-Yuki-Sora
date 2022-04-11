package yuki.application.discord.command.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.json.simple.JSONObject;
import yuki.api.lib.data.DiscApplicationUser;
import yuki.application.discord.command.CommandAction;
import yuki.application.discord.command.CommandOption;
import yuki.application.discord.command.DiscCommand;
import yuki.application.discord.command.handler.DiscCommandArgs;
import yuki.application.discord.util.TextUtil;
import yuki.core.YukiSora;

public class DiscCmdAlexa extends DiscCommand {

    public DiscCmdAlexa() {
        super("alexa", "Alexa integration command", false);

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
                    TextUtil.sendSuccess("The token was correct. Please use the verify command with alexa again to complete the verification process", event.getPrivateChannel());
                else
                    TextUtil.sendError("The token was invalid!", event.getPrivateChannel());
            }
        });
    }

    @Override
    public String getHelp() {
        return null;
    }
}
