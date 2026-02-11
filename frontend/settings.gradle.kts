pluginManagement {
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

        // ⭐ Mappls / MapMyIndia Maven repository
        maven {
            url = uri("https://maven.mappls.com/repository/mappls/")
        }
    }
}

rootProject.name = "TrailMate"
include(":app")
