plugins {
    id("connect.shadow-conventions")
    id("net.kyori.indra.publishing")
}

indra {
    configurePublications {
        if (shouldAddBranchName()) {
            version = versionWithBranchName()
        }
    }

    publishSnapshotsTo("minekube", "http://localhost/snapshots")
    publishReleasesTo("minekube", "http://localhost/releases")

}
