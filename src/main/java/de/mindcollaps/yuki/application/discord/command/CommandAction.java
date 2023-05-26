package de.mindcollaps.yuki.application.discord.command;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@SuppressWarnings({"SameReturnValue", "unused"})
public interface CommandAction {

    default boolean calledServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
        return true;
    }

    default boolean calledPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) {
        return true;
    }

    default boolean calledSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) {
        return true;
    }

    default void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException, Exception {
        throw new ActionNotImplementedException();
    }

    default void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException, Exception {
        throw new ActionNotImplementedException();
    }

    default void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException, Exception {
        throw new ActionNotImplementedException();
    }
}
