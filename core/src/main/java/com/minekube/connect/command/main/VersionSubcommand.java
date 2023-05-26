package com.minekube.connect.command.main;

import static com.minekube.connect.util.Constants.COLOR_CHAR;

import cloud.commandframework.context.CommandContext;
import com.google.inject.Inject;
import com.minekube.connect.api.logger.ConnectLogger;
import com.minekube.connect.command.ConnectSubCommand;
import com.minekube.connect.command.util.Permission;
import com.minekube.connect.player.UserAudience;
import com.minekube.connect.util.Constants;

public class VersionSubcommand extends ConnectSubCommand {
    @Inject
    private ConnectLogger logger;

    @Override
    public String name() {
        return "version";
    }

    @Override
    public String description() {
        return "Displays version information about Connect";
    }

    @Override
    public Permission permission() {
        return Permission.COMMAND_MAIN_VERSION;
    }

    @Override
    public void execute(CommandContext<UserAudience> context) {
        UserAudience sender = context.getSender();
        sender.sendMessage(String.format(
                COLOR_CHAR + "7You're currently on " + COLOR_CHAR + "b%s" +
                        COLOR_CHAR + "7 (branch: " + COLOR_CHAR + "b%s" + COLOR_CHAR + "7)",
                Constants.VERSION, Constants.GIT_BRANCH
                // TODO check for new version
        ));
    }
}