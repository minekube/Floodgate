package com.minekube.connect.util;

import com.google.common.collect.Maps;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.MethodDescriptor;
import java.util.Map;
import lombok.RequiredArgsConstructor;

public class HeaderClientInterceptor implements ClientInterceptor {
    public HeaderClientInterceptor(Map<String, String> headers) {
        headers.forEach((k, v) -> putHeaders.put(Key.of(k, Metadata.ASCII_STRING_MARSHALLER), v));
    }
    public final Map<Key<String>, String> putHeaders = Maps.newHashMap();

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions, Channel next) {
        return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                putHeaders.forEach(headers::put);
                super.start(new SimpleForwardingClientCallListener<RespT>(responseListener) {}, headers);
            }
        };
    }
}
