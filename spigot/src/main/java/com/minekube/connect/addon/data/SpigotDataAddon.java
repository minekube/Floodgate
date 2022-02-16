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

package com.minekube.connect.addon.data;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.minekube.connect.api.SimpleFloodgateApi;
import com.minekube.connect.api.inject.InjectorAddon;
import com.minekube.connect.api.logger.FloodgateLogger;
import com.minekube.connect.api.player.FloodgatePlayer;
import com.minekube.connect.config.FloodgateConfig;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public final class SpigotDataAddon implements InjectorAddon {
    @Inject private FloodgateHandshakeHandler handshakeHandler;
    @Inject private FloodgateConfig config;
    @Inject private SimpleFloodgateApi api;
    @Inject private FloodgateLogger logger;

    @Inject
    @Named("packetHandler")
    private String packetHandlerName;

    @Inject
    @Named("kickMessageAttribute")
    private AttributeKey<String> kickMessageAttribute;

    @Inject
    @Named("playerAttribute")
    private AttributeKey<FloodgatePlayer> playerAttribute;

    @Override
    public void onInject(Channel channel, boolean toServer) {
        // we have to add the packet blocker in the data handler, otherwise ProtocolSupport breaks
        channel.pipeline().addBefore(
                packetHandlerName, "floodgate_data_handler",
                new SpigotDataHandler(handshakeHandler, config, kickMessageAttribute)
        );
    }

    @Override
    public void onChannelClosed(Channel channel) {
        FloodgatePlayer player = channel.attr(playerAttribute).get();
        if (player != null && api.setPendingRemove(player)) {
            logger.translatedInfo("floodgate.ingame.disconnect_name", player.getUsername());
        }
    }

    @Override
    public void onRemoveInject(Channel channel) {
    }

    @Override
    public boolean shouldInject() {
        return true;
    }
}