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

package com.minekube.connect;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.minekube.connect.api.Clients;
import com.minekube.connect.api.ConnectApi;
import com.minekube.connect.api.InstanceHolder;
import com.minekube.connect.api.inject.PlatformInjector;
import com.minekube.connect.api.logger.ConnectLogger;
import com.minekube.connect.api.packet.PacketHandlers;
import com.minekube.connect.config.ConnectConfig;
import com.minekube.connect.inject.CommonPlatformInjector;
import com.minekube.connect.module.ClientsModule;
import com.minekube.connect.module.ConfigLoadedModule;
import com.minekube.connect.module.PostInitializeModule;
import com.minekube.connect.register.WatcherRegister;
import com.minekube.connect.tunnel.Tunneler;
import com.minekube.connect.util.Metrics;
import java.util.UUID;

public class ConnectPlatform {
    private static final String DOMAIN_SUFFIX = ".play.minekube.net";

    private static final UUID KEY = UUID.randomUUID();
    @Inject private PlatformInjector injector;
    @Inject private ConnectConfig config;
    @Inject private Injector guice;
    @Inject private ConnectLogger logger;

    @Inject
    public void init(
            ConnectApi api,
            PacketHandlers packetHandlers) {

        guice = guice.createChildInjector(
                new ConfigLoadedModule(config),
                new ClientsModule()
        );

        Clients clients = guice.getInstance(Clients.class);
        InstanceHolder.set(api, this.injector, packetHandlers, clients, KEY);
    }

    public void enable(Module... postInitializeModules) throws RuntimeException {
        if (injector == null) {
            throw new RuntimeException("Failed to find the platform injector!");
        }

        try {
            injector.inject();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to inject the packet listener!", exception);
        }

        this.guice = guice.createChildInjector(new PostInitializeModule(postInitializeModules));

        guice.getInstance(Metrics.class);

        logger.info("Endpoint name: " + config.getEndpoint());
        if (config.getSuperEndpoints() != null && !config.getSuperEndpoints().isEmpty()) {
            logger.info("Super endpoints: " + String.join(", ", config.getSuperEndpoints()));
        }
        logger.info("Your public address: " + config.getEndpoint() + DOMAIN_SUFFIX);
    }

    public boolean disable() {
        guice.getInstance(WatcherRegister.class).stop();
        guice.getInstance(Tunneler.class).close();
        guice.getInstance(CommonPlatformInjector.class).shutdown();
        return true;
    }

    public boolean isProxy() {
        return config.isProxy();
    }
}
