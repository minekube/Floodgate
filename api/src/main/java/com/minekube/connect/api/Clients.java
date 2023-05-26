package com.minekube.connect.api;


import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceGrpc.ConnectServiceBlockingStub;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceGrpc.ConnectServiceFutureStub;
import build.buf.gen.minekube.connect.v1alpha1.ConnectServiceGrpc.ConnectServiceStub;

public interface Clients {
    ConnectServiceBlockingStub getConnectServiceBlockingStub();
    ConnectServiceFutureStub getConnectServiceFutureStub();
    ConnectServiceStub getConnectServiceStub();
}