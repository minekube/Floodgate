plugins {
    id("connect.generate-templates")
}

dependencies {
    api(projects.api)
    api("org.geysermc.configutils", "configutils", Versions.configUtilsVersion)

    compileOnly(projects.ap)
    annotationProcessor(projects.ap)

    api("com.google.inject", "guice", Versions.guiceVersion)
    api("cloud.commandframework", "cloud-core", Versions.cloudVersion)
    api("org.yaml", "snakeyaml", Versions.snakeyamlVersion)
    api("org.bstats", "bstats-base", Versions.bstatsVersion)

    implementation("javax.annotation", "javax.annotation-api", "1.3.2")

    implementation("com.squareup.okhttp3", "okhttp", Versions.okHttpVersion)
    implementation("io.grpc", "grpc-okhttp", Versions.gRPCVersion)

    implementation("build.buf", "connect-kotlin-okhttp", Versions.connectKotlinVersion)
    implementation("build.buf", "connect-kotlin-google-java-ext", Versions.connectKotlinVersion)

    implementation("build.buf.gen", "minekube_connect_grpc_java", Versions.minekube_connect_grpc_javaVersion)
    implementation("build.buf.gen", "minekube_connect_bufbuild_connect-kotlin", Versions.minekube_connect_bufbuild_connectKotlinVersion)
    implementation("build.buf.gen", "minekube_connect_protocolbuffers_java", Versions.minekube_connect_protocolbuffers_javaVersion)

}

relocate("org.bstats")
relocate("com.squareup.okhttp3")


// present on all platforms
provided("io.netty", "netty-transport", Versions.nettyVersion)
provided("io.netty", "netty-codec", Versions.nettyVersion)
provided("io.netty", "netty-transport-native-unix-common", Versions.nettyVersion)


tasks {
    templateSources {
        replaceToken("connectVersion", fullVersion())
        replaceToken("branch", branchName())
        replaceToken("buildNumber", buildNumber())
    }
}