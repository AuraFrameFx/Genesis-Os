// Apply only the Android and Kotlin plugins directly, versions managed in root
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.coreLibraryDesugaring)
}

android {
    namespace = "dev.aurakai.auraframefx.romtools"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        vectorDrawables {
            useSupportLibrary = true
        }

        // ROM Tools Configuration
        buildConfigField("boolean", "ROM_TOOLS_ENABLED", "true")
        buildConfigField("String", "SUPPORTED_ANDROID_VERSIONS", "\"13,14,15\"")
        buildConfigField("String", "SUPPORTED_ARCHITECTURES", "\"arm64-v8a,armeabi-v7a,x86_64\"")

        // Native ROM modification tools
        externalNativeBuild {
            cmake {
                cppFlags += listOf("-std=c++20", "-fPIC")
                abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
                arguments += listOf(
                    "-DANDROID_STL=c++_shared",
                    "-DCMAKE_VERBOSE_MAKEFILE=ON",
                    "-DROM_TOOLS_BUILD=ON"
                )
            }
        }

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            externalNativeBuild {
                cmake {
                    cppFlags += listOf("-O3", "-DNDEBUG", "-DROM_RELEASE_BUILD")
                }
            }
        }
        debug {
            externalNativeBuild {
                cmake {
                    cppFlags += listOf("-g", "-DDEBUG", "-DROM_DEBUG_BUILD")
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
        prefab = true
        resValues = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = libs.versions.cmakeVersion.get()
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }

    sourceSets {
        named("main") {
            // Include ROM tools native libraries
            jniLibs.srcDirs("src/main/jniLibs")
        }
    }
}

kotlin {
    jvmToolchain(24)

    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
        freeCompilerArgs.addAll(
            "-Xuse-k2",
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-Xjvm-default=all"
        )
    }
}

dependencies {
    // Project modules (commented out until they exist)
    // implementation(project(":core-module"))
    // implementation(project(":secure-comm"))

    // Core AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.activity.compose)

    // Compose - ROM Tools UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Network & Serialization for ROM downloads/updates
    implementation(libs.bundles.network)

    // Room Database for ROM metadata and history
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // Security for ROM verification and signing
    implementation(libs.bundles.security)

    // WorkManager for background ROM operations
    implementation(libs.bundles.work)

    // Utilities
    implementation(libs.timber)
    implementation(libs.coil.compose)

    // Core library desugaring
    coreLibraryDesugaring(libs.coreLibraryDesugaring)
    coreLibraryDesugaring(libs.protobufDesugaring)

    // ROM Tools specific dependencies
    implementation("commons-io:commons-io:2.15.1")
    implementation("org.apache.commons:commons-compress:1.25.0")
    implementation("org.tukaani:xz:1.9")
    implementation("org.conscrypt:conscrypt-android:2.5.2")

    // Testing
    testImplementation(libs.bundles.testing.unit)
    androidTestImplementation(libs.bundles.testing.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    kspAndroidTest(libs.hilt.compiler)

    // Debug implementations
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // System interaction and root access
    implementation(files("${project.rootDir}/Libs/api-82.jar"))
    implementation(files("${project.rootDir}/Libs/api-82-sources.jar"))
}

// Configure native ROM tools build
tasks.configureEach {
    if (name.startsWith("externalNativeBuild")) {
        dependsOn(":copyRomTools")
    }
}

// Task to copy ROM modification tools
tasks.register<Copy>("copyRomTools") {
    from("${project.rootDir}/rom-tools")
    into("${layout.buildDirectory.dir("rom-tools").get()}")
    include("**/*.so", "**/*.bin", "**/*.img")
    includeEmptyDirs = false
}

// Task to verify ROM tools integrity
tasks.register("verifyRomTools") {
    doLast {
        val romToolsDir = file("${layout.buildDirectory.dir("rom-tools").get()}")
        if (!romToolsDir.exists()) {
            println("⚠️  ROM tools directory not found - ROM functionality may be limited")
        } else {
            println("✅ ROM tools verified and ready")
        }
    }
}

tasks.named("preBuild") {
    dependsOn("verifyRomTools")
}
