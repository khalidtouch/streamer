pluginManagement {
    includeBuild("build_logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Streamer"
include(":app")
include(":core")
include(":feature")
include(":feature:search")
include(":feature:albums")
include(":feature:artists")
include(":feature:playlists")
include(":feature:songs")
include(":feature:quickpicks")
include(":feature:settings")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:preferences")
include(":core:designsystem")
include(":core:domain")
include(":core:models")
include(":core:network")
include(":core:notifications")
include(":core:testing")
