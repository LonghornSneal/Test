import org.gradle.api.GradleException

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

tasks.register("generateBaselineProfile") {
    group = "baselineProfile"
    description =
        "Runs connectedNonMinifiedReleaseAndroidTest and copies the generated baseline profile into app/src/main/baseline-prof.txt."
    dependsOn("connectedNonMinifiedReleaseAndroidTest")
    doLast {
        val searchRoot = project.layout.projectDirectory.dir("../app/build").asFile
        val generatedProfiles =
            project.fileTree(searchRoot) { include("**/baseline-prof.txt") }.files.sortedByDescending { it.lastModified() }
        val source = generatedProfiles.firstOrNull()
            ?: throw GradleException(
                "Baseline profile not found under ${searchRoot.relativeTo(project.rootDir)}; ensure an emulator is available.",
            )
        val destination = project.layout.projectDirectory.dir("../app/src/main").file("baseline-prof.txt").asFile
        destination.parentFile.mkdirs()
        source.copyTo(destination, overwrite = true)
        logger.lifecycle("Copied baseline profile from $source to $destination")
    }
}
