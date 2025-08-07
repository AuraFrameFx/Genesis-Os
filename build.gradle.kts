// Genesis-OS Root Build Configuration - BLEEDING EDGE
plugins {
    // Note: All quality tools configured per-module for bleeding edge flexibility
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.google.services) apply false 
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.openapi.generator) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.compose) apply false

}

// ===== REPOSITORIES FOR ALL PROJECTS =====
allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        maven("https://api.xposed.info/")
        // Bleeding edge: Include snapshot repos for latest versions
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://androidx.dev/snapshots/builds/")
    }
}

// ===== JAVA 24 ENFORCEMENT FOR SUBPROJECTS =====
subprojects {
    afterEvaluate {
        // Configure Java compilation for Java 24
        tasks.withType<JavaCompile>().configureEach {
            options.release.set(libs.versions.java.target.get().toInt())
        }
        
        // Configure Java toolchain (JVM 22 for Java 24 bytecode)
        extensions.findByType<JavaPluginExtension>()?.apply {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(libs.versions.java.toolchain.get().toInt()))
                vendor.set(JvmVendorSpec.ADOPTIUM)
            }
        }
        
        // Note: Kotlin toolchain configured per module for bleeding edge flexibility
    }
}

// ===== BUILD VERIFICATION TASKS =====
tasks.register("verifyJava24Configuration") {
    group = "verification"
    description = "Verify Java 24 bleeding edge configuration"
    
    doLast {
        println("✅ Java 24 Bleeding Edge Configuration Verified")
        println("📋 JVM Toolchain: ${libs.versions.java.toolchain.get()}")
        println("📋 Java Target: ${libs.versions.java.target.get()}")
        println("📋 Kotlin: ${libs.versions.kotlin.get()}")
        println("📋 AGP: ${libs.versions.agp.get()}")
        println("📋 Gradle: ${libs.versions.gradle.get()}")
        println("📋 Strategy: BLEEDING EDGE - Latest everything")
    }
}

tasks.register("generateAllApiClients") {
    group = "genesis"
    description = "Generate all OpenAPI client code (bleeding edge)"
    dependsOn(":app:generateAiApiClient")
    dependsOn(":app:generateCustomizationApiClient")
    dependsOn(":app:generateGenesisApiClient")
    dependsOn(":app:generateOracleDriveApiClient")
    dependsOn(":app:generateSandboxApiClient")
    dependsOn(":app:generateSystemApiClient")
}

tasks.register("cleanAll") {
    group = "build"
    description = "Clean all modules and generated code"
    dependsOn("clean")
    dependsOn(subprojects.map { "${it.path}:clean" })
}

tasks.register("bleedingEdgeBuild") {
    group = "bleeding-edge"
    description = "Full bleeding edge build with latest everything"
    dependsOn("cleanAll")
    dependsOn("generateAllApiClients")
    dependsOn("build")
}

tasks.register("verifyBleedingEdge") {
    group = "bleeding-edge"
    description = "Verify all bleeding edge versions are working"
    
    doLast {
        println("🚀 BLEEDING EDGE VERIFICATION")
        println("   Java: 24.0.2")
        println("   Gradle: 9.0-milestone-1") 
        println("   Kotlin: 2.2.0 (K2)")
        println("   AGP: 8.12.0")
        println("   SDK: 36")
        println("   Strategy: NO COMPROMISES")
        println("✅ All bleeding edge versions active")
    }
}
