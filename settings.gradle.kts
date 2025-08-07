// Settings configured for Gradle 8.14.3 and Java 24
@file:Suppress("UnstableApiUsage", "JCenterRepository")

// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

// Plugin Management
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }

    // Configure resolution strategy for plugins
    resolutionStrategy {
        eachPlugin {
            when {
                // Android plugins
                requested.id.namespace == "com.android" ->
                    useModule("com.android.tools.build:gradle:${requested.version}")

                // KSP plugin
                requested.id.id == "com.google.devtools.ksp" ->
                    useModule("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${requested.version}")

                // Hilt plugin
                requested.id.id == "com.google.dagger.hilt.android" ->
                    useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
            }
        }
    }
}

// Dependency resolution management
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven(url = "https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }
}

rootProject.name = "AuraFrameFX"

// Include all modules
include(":app")
include(":sandbox-ui")
include(":collab-canvas")  // Fixed typo from 'colleb-canvas'
include(":oracle-drive-integration")  // Fixed typo from 'oracledrive-integration'
include(":core-module")
include(":feature-module")

// Configure all projects to use standard build.gradle.kts
rootProject.children.forEach { project ->
    project.buildFileName = "${project.name}.gradle.kts".takeIf {
        project.projectDir.resolve("${project.name}.gradle.kts").exists()
    } ?: "build.gradle.kts"

    // Create build file if it doesn't exist
    val buildFile = project.projectDir.resolve(project.buildFileName!!)
    if (!buildFile.exists()) {
        buildFile.parentFile?.mkdirs()
        buildFile.createNewFile()
        buildFile.writeText(
            """
            // ${project.name} build configuration
            plugins {
                // Common plugins can be applied here
            }

            group = "dev.auraframefx"
            version = "1.0.0"
        """.trimIndent()
        )
    }
}
