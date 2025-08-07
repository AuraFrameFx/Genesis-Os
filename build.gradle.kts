// Root build file - manages plugin versions and common configuration
plugins {
    // Android plugins
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    // Kotlin plugins
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    // KSP (Kotlin Symbol Processing)
    alias(libs.plugins.ksp) apply false

    // Hilt for dependency injection
    alias(libs.plugins.hilt.android) apply false

    // Other plugins
    id("org.openapi.generator") version "7.2.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.5" apply false
}



// Common configuration for all subprojects
subprojects {
    // Configure Java toolchain for all Java projects
    plugins.withType<JavaBasePlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(24))
        }
    }

    // Configure Kotlin compiler options for all Kotlin projects
    plugins.withType<org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin> {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension> {
            // Configure K2 compiler
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(24))
            }
        }
    }

    // Test configuration
    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            showStandardStreams = true
        }
    }
}

// Clean task for the root project
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
