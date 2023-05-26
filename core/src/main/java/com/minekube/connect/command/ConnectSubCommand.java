package com.minekube.connect.command;

import cloud.commandframework.context.CommandContext;
import com.minekube.connect.command.util.Permission;
import com.minekube.connect.player.UserAudience;

public abstract class ConnectSubCommand {
    public abstract String name();
    public abstract String description();
    public abstract Permission permission();
    public abstract void execute(CommandContext<UserAudience> context);
}
