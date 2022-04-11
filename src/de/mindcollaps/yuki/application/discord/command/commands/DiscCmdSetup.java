package yuki.application.discord.command.commands;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import yuki.api.lib.data.DiscApplicationServer;
import yuki.api.lib.data.DiscApplicationUser;
import yuki.application.discord.command.CommandAction;
import yuki.application.discord.command.CommandOption;
import yuki.application.discord.command.DiscCommand;
import yuki.application.discord.command.SubCommand;
import yuki.application.discord.command.handler.DiscCommandArgs;
import yuki.application.discord.util.DiscordUtil;
import yuki.application.discord.util.TextUtil;
import yuki.core.YukiSora;

public class DiscCmdSetup extends DiscCommand {

    public DiscCmdSetup() {
        super("setup", "This command is used to setup the server and all of its features", false);

        addSubcommands(
                new SubCommand("channels", "With this sub command you can edit channels")
                        .addOption(
                                new CommandOption(OptionType.SUB_COMMAND, "twitch", "Create a twitch channel with this command"), new CommandOption(OptionType.CHANNEL, "channel", "The new twitch channel"))
                        .addAction(new CommandAction() {
            @Override
            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getTextChannel(), yukiSora);
            }

            @Override
            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                MessageChannel tc = args.getArg("channel").getMessageChannel();
                if (tc == null)
                    return;
                server.setTwitchNotifyChannel(tc.getId());

                TextUtil.sendSuccess("A new twitch channel is setup and running!\nI will inform anyone when somebody that is listed in my database starts streaming :laughing:", event.getTextChannel());
            }
        }));

        addSubcommands(
                new SubCommand("channels", "With this sub command you can edit channels")
                        .addOption(OptionType.SUB_COMMAND, "statistics", "Create a statistics category")
                        .addOption(OptionType.STRING, "category", "The category where the statistics will be shown")
                .addAction(new CommandAction() {
            @Override
            public boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                return DiscordUtil.userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getTextChannel(), yukiSora);
            }

            @Override
            public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
                String categoryId = args.getArg("category").getString();
                boolean error = false;
                try {
                    Category category = yukiSora.getDiscordApplication().getBotJDA().getCategoryById(categoryId);
                    if(category == null)
                        error = true;
                } catch (Exception e){
                    error = true;
                }
                if(error){
                    TextUtil.sendError("Seems like this category doesn't exist :hushed:", event.getChannel());
                } else {
                    server.setStatisticsCategoryId(categoryId);
                    TextUtil.sendSuccess("Okay, now we have a new statistics category, let me set that up real quick!", event.getTextChannel());
                    server.updateServerStats(yukiSora);
                }
            }
        }));
    }

    @Override
    public String getHelp() {
        return null;
    }
}
