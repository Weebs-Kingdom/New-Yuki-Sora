package de.mindcollaps.yuki.application.discord.command.commands;

import de.mindcollaps.yuki.api.lib.data.DiscApplicationServer;
import de.mindcollaps.yuki.api.lib.data.DiscApplicationUser;
import de.mindcollaps.yuki.application.discord.command.ActionNotImplementedException;
import de.mindcollaps.yuki.application.discord.command.CommandAction;
import de.mindcollaps.yuki.application.discord.command.DiscCommand;
import de.mindcollaps.yuki.application.discord.command.handler.DiscCommandArgs;
import de.mindcollaps.yuki.application.discord.util.TextUtil;
import de.mindcollaps.yuki.core.YukiSora;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.net.URL;

public class DiscCmdInfo extends DiscCommand {

    public DiscCmdInfo() {
        super("info", "Displays your user info");

        addAction(
                new CommandAction() {
                    @Override
                    public void actionServer(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                        dispatchCommand(yukiSora, event.getAuthor(), new TextUtil.ResponseInstance(event));
                    }

                    @Override
                    public void actionPrivate(DiscCommandArgs args, MessageReceivedEvent event, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                        dispatchCommand(yukiSora, event.getAuthor(), new TextUtil.ResponseInstance(event));
                    }

                    @Override
                    public void actionSlash(DiscCommandArgs args, SlashCommandInteractionEvent event, DiscApplicationServer server, DiscApplicationUser user, YukiSora yukiSora) throws ActionNotImplementedException {
                        dispatchCommand(yukiSora, event.getUser(), new TextUtil.ResponseInstance(event));
                    }
                }
        );
    }

    private void dispatchCommand(YukiSora yukiSora, User user, TextUtil.ResponseInstance response) {
        try {
            JSONObject res = yukiSora.getApiManagerOld().getUserLevelInfo(user.getId(), user.getAvatarUrl());
            URL website = new URL((String) res.get("data"));
            File file = new File(de.mindcollaps.yuki.util.FileUtils.home + "/temp.png");
            FileUtils.copyURLToFile(website, file);
            FileUpload fileUpload = FileUpload.fromData(file).setName(user.getName() + " info.png");
            TextUtil.sendFiles(response, fileUpload);
            file.deleteOnExit();
        } catch (Exception e) {
            TextUtil.sendError("There was an error generating your user info. It seems like we have issues with our database or website connection :smiling_face_with_tear:", response);
        }
    }

    @Override
    public String getHelp() {
        return null;
    }
}
