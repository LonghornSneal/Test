plugins {
    id("com.android.application")
    kotlin("android")
    id("com.github.triplet.play")
}

android {
    namespace = "com.cosmobond.watchface"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cosmobond.watchface"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = false
    }
}

play {
    serviceAccountCredentials.set(file(System.getenv("PLAY_SERVICE_ACCOUNT_JSON") ?: ""))
    track.set("internal")
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear.watchface:watchface:1.3.1")
    implementation("androidx.wear.watchface:watchface-client:1.3.1")
    implementation("androidx.wear.watchface:watchface-style:1.3.1")
}
