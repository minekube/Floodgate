package com.minekube.connect.module;

import build.buf.connect.ProtocolClientConfig;
import build.buf.connect.extensions.GoogleJavaProtobufStrategy;
import build.buf.connect.impl.ProtocolClient;
import build.buf.connect.okhttp.ConnectOkHttpClient;
import build.buf.connect.protocols.NetworkProtocol;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceClient;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceClientInterface;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceGrpc;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceGrpc.ConnectServiceBlockingStub;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceGrpc.ConnectServiceFutureStub;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceGrpc.ConnectServiceStub;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.minekube.connect.api.Clients;
import com.minekube.connect.config.ConfigHolder;
import com.minekube.connect.util.HeaderClientInterceptor;
import com.minekube.connect.watch.WatchClient;
import io.grpc.Channel;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannelBuilder;
import io.grpc.okhttp.OkHttpChannelBuilder;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

@RequiredArgsConstructor
public class ClientsModule extends AbstractModule {

    private static final String CONNECT_API_TARGET = "connect-api.minekube.com";

    @Override
    protected void configure() {
        bind(Clients.class).to(ClientsImpl.class);
        bind(ClientsImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public ProtocolClient protocolClient(
            @Named("connectHttpClient") OkHttpClient connectOkHttpClient
    ) {
        final String connectServiceHost = System.getenv().getOrDefault(
                "CONNECT_SERVICE_HOST", "https://" + CONNECT_API_TARGET);
        return new ProtocolClient(
                new ConnectOkHttpClient(connectOkHttpClient),
                new ProtocolClientConfig(
                        connectServiceHost,
                        new GoogleJavaProtobufStrategy(),
                        NetworkProtocol.CONNECT
                )
        );
    }

    @Provides
    @Singleton
    public ConnectServiceClientInterface connectServiceClient(ProtocolClient protocolClient) {
        return new ConnectServiceClient(protocolClient);
    }

    @Provides
    @Singleton
    public ConnectServiceFutureStub connectServiceFutureStub(
            @Named("connectGrpcChannel") Channel channel
    ) {
        return ConnectServiceGrpc.newFutureStub(channel);
    }

    @Provides
    @Singleton
    public ConnectServiceStub connectServiceStub(
            @Named("connectGrpcChannel") Channel channel
    ) {
        return ConnectServiceGrpc.newStub(channel);
    }

    @Provides
    @Singleton
    public ConnectServiceBlockingStub connectServiceBlockingStub(
            @Named("connectGrpcChannel") Channel channel
    ) {
        return ConnectServiceGrpc.newBlockingStub(channel);
    }

    @Provides
    @Singleton
    @Named("connectGrpcChannel")
    public Channel connectServiceChannel(
            @Named("connectGrpcChannelBuilder") ManagedChannelBuilder<?> managedChannelBuilder
    ) {
        return managedChannelBuilder.build();
    }

    @Provides
    @Singleton
    @Named("connectGrpcChannelBuilder")
    public ManagedChannelBuilder<?> connectServiceChannelBuilder(
            @Named("endpointName") String endpointName,
            @Named("connectToken") String connectToken
    ) {
//        return ManagedChannelBuilder.forTarget(CONNECT_API_TARGET)
//        return Grpc.newChannelBuilder(CONNECT_API_TARGET, InsecureChannelCredentials.create())
        return OkHttpChannelBuilder.forTarget(CONNECT_API_TARGET, InsecureChannelCredentials.create())
                .intercept(new HeaderClientInterceptor(ImmutableMap.of(
                        WatchClient.ENDPOINT_HEADER, endpointName,
                        "Authorization", "Bearer " + connectToken
                )));
    }

    @Provides
    @Singleton
    @Named("endpointName")
    public String endpointName(ConfigHolder configHolder) {
        return configHolder.get().getEndpoint();
    }

    static class ClientsImpl implements Clients {
        @Inject Provider<ConnectServiceBlockingStub> connectServiceBlockingStub;
        @Inject Provider<ConnectServiceFutureStub> connectServiceFutureStub;
        @Inject Provider<ConnectServiceStub> connectServiceStub;

        @Override
        public ConnectServiceBlockingStub getConnectServiceBlockingStub() {
            return connectServiceBlockingStub.get();
        }

        @Override
        public ConnectServiceFutureStub getConnectServiceFutureStub() {
            return connectServiceFutureStub.get();
        }

        @Override
        public ConnectServiceStub getConnectServiceStub() {
            return connectServiceStub.get();
        }
    }
}
