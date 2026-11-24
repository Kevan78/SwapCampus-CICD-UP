plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android)
}

// ---------------------------------------------------------------------------
// CI/CD: values injected by GitHub Actions / Fastlane through env variables.
// Locally (Android Studio) nothing is set, so the defaults below are used.
// ---------------------------------------------------------------------------
val ciVersionCode: Int = providers.environmentVariable("VERSION_CODE").orNull?.toIntOrNull() ?: 1
val ciVersionName: String = providers.environmentVariable("VERSION_NAME").orNull ?: "1.0"
val releaseKeystorePath: String? = providers.environmentVariable("KEYSTORE_PATH").orNull

android {
    namespace = "com.example.swapcampus_geoffroykevan"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.swapcampus_geoffroykevan"
        minSdk = 36
        targetSdk = 37
        versionCode = ciVersionCode      // = GitHub run number on CI
        versionName = ciVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (releaseKeystorePath != null) {
                storeFile = file(releaseKeystorePath)
                storePassword = providers.environmentVariable("KEYSTORE_PASSWORD").orNull
                keyAlias = providers.environmentVariable("KEY_ALIAS").orNull
                keyPassword = providers.environmentVariable("KEY_PASSWORD").orNull
            }
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
            // Real keystore on CI when the secrets exist, debug key otherwise
            // (Firebase App Distribution accepts debug-signed APKs).
            signingConfig = if (releaseKeystorePath != null) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    testImplementation(project(":shared"))   // AuthRepositoryTest uses the shared domain
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}