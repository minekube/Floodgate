plugins {
    `java-library`
    id("net.kyori.indra")
    id("net.kyori.indra.git")
}

dependencies {
    compileOnly("org.checkerframework", "checker-qual", Versions.checkerQual)
}

indra {
    github("minekube", "connect-java") {
        ci(true)
        issues(true)
        scm(true)
    }
    mitLicense()

    javaVersions {
        // without toolchain & strictVersion sun.misc.Unsafe won't be found
        minimumToolchain(8)
        strictVersions(true)
    }
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml", "velocity-plugin.json")) {
            expand(
                "id" to "connect",
                "name" to "connect",
                "version" to fullVersion(),
                "description" to project.description,
                "url" to "https://minekube.com",
                "author" to "Minekube"
            )
        }
    }
}
