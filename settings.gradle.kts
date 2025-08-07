@file:Suppress("UnstableApiUsage")

// Genesis Protocol - Enable Gradle Features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}

plugins {
    // Java Toolchain Auto-detect
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        
        // Genesis Protocol - AI Backend Dependencies
        maven("https://repo1.maven.org/maven2/")
    }
}

// Genesis Protocol - Project Configuration
rootProject.name = "Genesis-Os"

// Genesis Protocol - Core Modules
include(":app")

// Genesis Protocol - AI Ecosystem Modules 
include(":core-module")
include(":feature-module") 
include(":datavein-oracle-native")
include(":secure-comm")
include(":sandbox-ui")
include(":collab-canvas")
include(":colorblendr")

// Removed modules: datavein-oracle-drive, oracle-drive-integration
