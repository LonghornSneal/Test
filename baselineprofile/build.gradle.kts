plugins {
    id("com.android.test")
    kotlin("android")
    id("androidx.baselineprofile")
}

android {
    namespace = "com.cosmobond.watchface.baselineprofile"
    compileSdk = 34

    defaultConfig {
        minSdk = 34
        targetSdk = 34
        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.test:core:1.5.0")
    implementation("androidx.test:runner:1.5.2")
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")
    implementation("androidx.benchmark:benchmark-macro-junit4:1.2.4")
    implementation("androidx.benchmark:benchmark-junit4:1.2.4")
}
