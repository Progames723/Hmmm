pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7-alpha.14"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    shared {
        fun mc(loader: String, vararg versions: String) {
            for (version in versions) vers("$version-$loader", version)
        }
        mc("fabric","1.20.1","1.20.4", "1.21.1", "1.21.4")
        mc("forge","1.20.1")
        mc("neoforge", "1.20.4", "1.21.1", "1.21.4")
        vcsVersion = "1.20.1-fabric"
    }
    create(rootProject)
}

rootProject.name = "hmmm_library"