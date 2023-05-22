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

package com.minekube.connect.api;

import com.minekube.connect.api.inject.PlatformInjector;
import com.minekube.connect.api.packet.PacketHandlers;
import java.util.UUID;
import lombok.Getter;

public final class InstanceHolder {
    @Getter private static ConnectApi api;

    @Getter private static PlatformInjector injector;
    @Getter private static PacketHandlers packetHandlers;
    @Getter private static Clients clients;
    private static UUID storedKey;

    public static boolean set(
            ConnectApi connectApi,
            PlatformInjector platformInjector,
            PacketHandlers packetHandlers,
            Clients clients,
            UUID key) {

        if (storedKey != null) {
            if (!storedKey.equals(key)) {
                return false;
            }
        } else {
            storedKey = key;
        }

        InstanceHolder.api = connectApi;
        InstanceHolder.injector = platformInjector;
        InstanceHolder.packetHandlers = packetHandlers;
        InstanceHolder.clients = clients;
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T extends ConnectApi> T castApi(Class<T> cast) {
        return (T) api;
    }


}
