import com.google.protobuf.gradle.*
import net.kyori.blossom.BlossomExtension

plugins {
    idea // used to let Intellij recognize protobuf generated sources
    id("net.kyori.blossom")
    id("com.google.protobuf")
}

dependencies {
    api(projects.api)

    api("com.google.inject", "guice", Versions.guiceVersion)
    api("com.nukkitx.fastutil", "fastutil-short-object-maps", Versions.fastutilVersion)
    api("com.nukkitx.fastutil", "fastutil-int-object-maps", Versions.fastutilVersion)
    api("org.java-websocket", "Java-WebSocket", Versions.javaWebsocketVersion)
    api("net.kyori", "adventure-api", Versions.adventureApiVersion)
    api("cloud.commandframework", "cloud-core", Versions.cloudVersion)
    api("org.yaml", "snakeyaml", Versions.snakeyamlVersion)

    runtimeOnly("io.grpc", "grpc-netty-shaded", Versions.gRPCVersion)
    implementation("io.grpc", "grpc-protobuf", Versions.gRPCVersion)
    implementation("io.grpc", "grpc-stub", Versions.gRPCVersion)
}

// present on all platforms
provided("io.netty", "netty-transport", Versions.nettyVersion)
provided("io.netty", "netty-codec", Versions.nettyVersion)
provided("io.netty", "netty-transport-native-unix-common", Versions.nettyVersion)

configure<BlossomExtension> {
    val constantsFile = "src/main/java/org/geysermc/floodgate/util/Constants.java"
    replaceToken("\${branch}", branchName(), constantsFile)
    replaceToken("\${buildNumber}", buildNumber(), constantsFile)
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:${Versions.protocVersion}"
    }
    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Versions.gRPCVersion}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}