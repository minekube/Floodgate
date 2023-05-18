plugins {
    idea // used to let Intellij recognize protobuf generated sources
}

dependencies {
    api("com.google.code.gson", "gson", Versions.gsonVersion)

    compileOnly("io.netty", "netty-transport", Versions.nettyVersion)
    api("build.buf", "connect-kotlin-okhttp", Versions.connectKotlinVersion)
    api("build.buf.gen", "minekube_connect_grpc_java", Versions.minekube_connect_grpc_javaVersion)
    api("build.buf.gen", "minekube_connect_bufbuild_connect-kotlin", Versions.minekube_connect_bufbuild_connectKotlinVersion)
    api("build.buf.gen", "minekube_connect_protocolbuffers_java", Versions.minekube_connect_protocolbuffers_javaVersion)
}
