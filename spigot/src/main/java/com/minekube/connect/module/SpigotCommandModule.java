/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Floodgate
 */

package com.minekube.connect.module;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.minekube.connect.SpigotPlugin;
import com.minekube.connect.command.util.Permission;
import com.minekube.connect.platform.command.CommandUtil;
import com.minekube.connect.player.ConnectCommandPreprocessor;
import com.minekube.connect.player.UserAudience;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

@RequiredArgsConstructor
public final class SpigotCommandModule extends CommandModule {
    private final SpigotPlugin plugin;

    @Override
    protected void configure() {
        super.configure();
        registerPermissions();
    }

    @Provides
    @Singleton
    @SneakyThrows
    public CommandManager<UserAudience> commandManager(CommandUtil commandUtil) {
        CommandManager<UserAudience> commandManager = new BukkitCommandManager<>(
                plugin,
                CommandExecutionCoordinator.simpleCoordinator(),
                commandUtil::getUserAudience,
                audience -> (CommandSender) audience.source()
        );
        commandManager.registerCommandPreProcessor(new ConnectCommandPreprocessor<>(commandUtil));
        return commandManager;
    }

    private void registerPermissions() {
        PluginManager manager = Bukkit.getPluginManager();
        for (Permission permission : Permission.values()) {
            if (manager.getPermission(permission.get()) != null) {
                continue;
            }

            PermissionDefault defaultValue =
                    PermissionDefault.getByName(permission.defaultValue().name());

            manager.addPermission(new org.bukkit.permissions.Permission(
                    permission.get(), defaultValue
            ));
        }
    }
}
