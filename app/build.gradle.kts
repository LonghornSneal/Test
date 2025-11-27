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

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

play {
    val credsPath = System.getenv("PLAY_SERVICE_ACCOUNT_JSON")
    if (!credsPath.isNullOrBlank()) {
        val credsFile = file(credsPath)
        if (credsFile.exists()) {
            serviceAccountCredentials.set(credsFile)
        } else {
            logger.warn("PLAY_SERVICE_ACCOUNT_JSON points to missing file: $credsPath")
        }
    } else {
        logger.lifecycle("PLAY_SERVICE_ACCOUNT_JSON not set; skipping Play Publisher credentials for local builds.")
    }
    track.set("internal")
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear.watchface:watchface:1.2.1")
    implementation("androidx.wear.watchface:watchface-client:1.2.1")
    implementation("androidx.wear.watchface:watchface-style:1.2.1")
    implementation("androidx.wear.watchface:watchface-complications:1.2.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("androidx.test:core-ktx:1.5.0")
}
