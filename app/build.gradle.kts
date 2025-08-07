import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.openapi.generator)
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 36
    ndkVersion = libs.versions.ndkVersion.get()
    buildToolsVersion = "36.0.0"

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        // ===== XPOSED MODULE CONFIGURATION =====
        buildConfigField("boolean", "XPOSED_MODULE", "true")
        buildConfigField("String", "XPOSED_MIN_VERSION", "\"${libs.versions.xposed.get()}\"")
        resValue("string", "xposed_description", "\"Genesis-OS AI Framework Hooks\"")

        // ===== NATIVE AI PROCESSING =====
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
                abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
                arguments += listOf(
                    "-DANDROID_STL=c++_shared",
                    "-DCMAKE_VERBOSE_MAKEFILE=ON"
                )
                version = libs.versions.cmakeVersion.get()
            }
        }

        // ===== NDK CONFIGURATION =====
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
    }

// ===== OPENAPI CODE GENERATION =====
    val openapiSpecs = listOf(
        Triple("ai", "ai-api.yml", "dev.aurakai.auraframefx.api.ai"),
        Triple(
            "customization",
            "customization-api.yml",
            "dev.aurakai.auraframefx.api.customization"
        ),
        Triple("genesis", "genesis-api.yml", "dev.aurakai.auraframefx.api.genesis"),
        Triple("oracleDrive", "oracle-drive-api.yml", "dev.aurakai.auraframefx.api.oracledrive"),
        Triple("sandbox", "sandbox-api.yml", "dev.aurakai.auraframefx.api.sandbox"),
        Triple("system", "system-api.yml", "dev.aurakai.auraframefx.api.system")
    )
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
    }

    openapiSpecs.forEach { (name, spec, pkg) ->
        tasks.register(
            "generate${name.replaceFirstChar { it.uppercase() }}ApiClient",
            GenerateTask::class
        ) {
            generatorName.set("kotlin")
            library.set("jvm-retrofit2")
            inputSpec.set("${rootDir}/api-spec/$spec")
            outputDir.set(
                layout.buildDirectory.dir("generated/openapi/$name").get().asFile.absolutePath
            )
            packageName.set(pkg)
            configFile.set("${rootDir}/openapi-generator-config.json")
        }
        // No dependsOn needed - the task is standalone
    }

    // ===== BUILD TYPES =====
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Enable R8 for release builds
            isShrinkResources = true
        }

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    // ===== BUILD FEATURES =====
    buildFeatures {
        compose = true
        buildConfig = true
        prefab = true
    }

    // ===== COMPOSE CONFIGURATION =====
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    // ===== EXTERNAL NATIVE BUILD =====
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = libs.versions.cmakeVersion.get()
        }
    }

    // ===== PACKAGING OPTIONS (CORRECTED) =====
    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/DEPENDENCIES",
                "/META-INF/LICENSE",
                "/META-INF/LICENSE.txt",
                "/META-INF/NOTICE",
                "/META-INF/NOTICE.txt",
                "META-INF/*.kotlin_module"
            )
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }

    // ===== SOURCE SETS - GENERATED API CODE =====
    sourceSets {
        getByName("main") {
            java.srcDirs(
                layout.buildDirectory.dir("generated/openapi/ai/src/main/kotlin"),
                layout.buildDirectory.dir("generated/openapi/customization/src/main/kotlin"),
                layout.buildDirectory.dir("generated/openapi/genesis/src/main/kotlin"),
                layout.buildDirectory.dir("generated/openapi/oracleDrive/src/main/kotlin"),
                layout.buildDirectory.dir("generated/openapi/sandbox/src/main/kotlin"),
                layout.buildDirectory.dir("generated/openapi/system/src/main/kotlin")
            )
        }
    }
}

// ===== KOTLIN TOOLCHAIN - JVM 22 FOR JAVA 24 TARGET =====
kotlin {
    jvmToolchain(libs.versions.java.toolchain.get().toInt())
}

// ===== BUILD TASK DEPENDENCIES =====
afterEvaluate {
    tasks.named("preBuild") {
        dependsOn(
            "generateAiApiClient",
            "generateCustomizationApiClient",
            "generateGenesisApiClient",
            "generateOracleDriveApiClient",
            "generateSandboxApiClient",
            "generateSystemApiClient"
        )
    }
}

dependencies {
            // ===== CORE ANDROIDX =====
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.activity.compose)

            // ===== COMPOSE UI SYSTEM =====
            implementation(platform(libs.androidx.compose.bom))
            implementation(libs.bundles.compose)
            implementation(libs.androidx.navigation.compose)

            // ===== HILT DEPENDENCY INJECTION =====
            implementation(libs.hilt.android)
            ksp(libs.hilt.compiler)
            implementation(libs.hilt.navigation.compose)

            // ===== COROUTINES & ASYNC =====
            implementation(libs.bundles.coroutines)

            // ===== NETWORKING =====
            implementation(libs.bundles.network)

            // ===== ROOM DATABASE =====
            implementation(libs.room.runtime)
            implementation(libs.room.ktx)
            ksp(libs.room.compiler)

            // ===== UTILITIES =====
            implementation(libs.timber)
            implementation(libs.coil.compose)

            // ===== JAVA 24 CORE LIBRARY DESUGARING =====
            coreLibraryDesugaring(libs.coreLibraryDesugaring)

            // ===== FIREBASE PLATFORM =====
            implementation(platform(libs.firebase.bom))
            implementation(libs.bundles.firebase)

            // ===== XPOSED FRAMEWORK =====
            implementation(libs.bundles.xposed)
            ksp(libs.yuki.ksp.xposed)
            implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

            // ===== DEBUG TOOLS =====
            debugImplementation(libs.leakcanary.android)
            debugImplementation(libs.androidx.compose.ui.tooling)
            debugImplementation(libs.androidx.compose.ui.test.manifest)

            // ===== TESTING =====
            testImplementation(libs.bundles.testing)
            testRuntimeOnly(libs.junit.engine)

    // ===== ANDROID TESTING =====
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}
