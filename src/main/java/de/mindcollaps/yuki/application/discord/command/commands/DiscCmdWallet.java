package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.api.lib.request.FindUserById;
import de.mindcollaps.yuki.application.discord.command.*;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

public class DiscCmdWallet extends DiscCommand {

    public DiscCmdWallet() {
        super("wallet", "With this command you can manage your finances");

        addSubcommands(
                new SubCommand("info", "Shows how much money you have")
                        .addAction(
                                new CommandAction() {
                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        walletInfo(event.getAuthor(), user, new TextUtil.ResponseInstance(event));
                                    }

                                    @Override
                                    public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        walletInfo(event.getAuthor(), user, new TextUtil.ResponseInstance(event));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        walletInfo(event.getUser(), user, new TextUtil.ResponseInstance(event));
                                    }
                                }
                        )
        );

        addSubcommands(
                new SubCommand("give", "Give someone a certain amount of your coins")
                        .addOptions(
                                new CommandOption(
                                        OptionType.USER,
                                        "user",
                                        "The user you want to give coins to"
                                ),
                                new CommandOption(
                                        OptionType.NUMBER,
                                        "amount",
                                        "The amount you want to give"
                                )
                        )
                        .addAction(
                                new CommandAction() {
                                    @Override
                                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        giveCoinsToAnotherUser(
                                                user,
                                                args.getArg("user").getUser(),
                                                args.getArg("amount").getInteger(),
                                                yukiSora,
                                                new TextUtil.ResponseInstance(event));
                                    }

                                    @Override
                                    public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        giveCoinsToAnotherUser(
                                                user,
                                                args.getArg("user").getUser(),
                                                args.getArg("amount").getInteger(),
                                                yukiSora,
                                                new TextUtil.ResponseInstance(event));
                                    }

                                    @Override
                                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                                        giveCoinsToAnotherUser(
                                                user,
                                                args.getArg("user").getUser(),
                                                args.getArg("amount").getInteger(),
                                                yukiSora,
                                                new TextUtil.ResponseInstance(event));
                                    }
                                }
                        )
        );
    }

    private void walletInfo(User usr, DiscApplicationUser user, TextUtil.ResponseInstance res) {
        TextUtil.sendMessageEmbed(
                new EmbedBuilder()
                        .setAuthor(usr.getName() + "'s wallet", usr.getAvatarUrl())
                        .setColor(Color.ORANGE)
                        .setDescription("You currently have " + user.getCoins() + " :coin: in your wallet")
                        .build()
                , res
        );
    }

    private void giveCoinsToAnotherUser(DiscApplicationUser usr, User giveTo, int amount, YukiSora yukiSora, TextUtil.ResponseInstance res) {
        DiscApplicationUser userGiveTo = new FindUserById(giveTo.getId()).makeRequestSingle(yukiSora);
        if (userGiveTo != null) {
            if (usr.removeCoins(amount)) {
                userGiveTo.updateData(yukiSora);
                usr.updateData(yukiSora);

                TextUtil.sendSuccess(amount + " coins have been withdrawn from your account and where added to " + giveTo.getName() + "'s account :blush:", res);
            } else {
                TextUtil.sendError("You don't have enough coins to give " + amount + " to " + giveTo.getName() + " :hushed:", res);
            }
        } else {
            TextUtil.sendError("The user you specified was not found or is not yet registered in the database", res);
        }
    }

    @Override
    public String getHelp() {
        return null;
    }
}
