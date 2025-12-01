import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Base64

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.github.triplet.play")
    id("app.cash.paparazzi")
    jacoco
}

android {
    namespace = "com.cosmobond.watchface"
    compileSdk = 34

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("UPLOAD_KEYSTORE_PATH")
            val keystoreBase64 = System.getenv("UPLOAD_KEYSTORE_BASE64")
            val storePwd = System.getenv("STORE_PASSWORD")
            val keyAliasEnv = System.getenv("UPLOAD_KEY_ALIAS")
            val keyPwd = System.getenv("UPLOAD_KEY_PASSWORD")

            val keystoreFile =
                when {
                    !keystorePath.isNullOrBlank() -> file(keystorePath)
                    !keystoreBase64.isNullOrBlank() -> {
                        val decoded =
                            layout.buildDirectory.file("keystore/upload-keystore.jks").get().asFile
                        decoded.parentFile.mkdirs()
                        decoded.writeBytes(Base64.getDecoder().decode(keystoreBase64))
                        decoded
                    }
                    else -> null
                }

            val hasReleaseCreds =
                keystoreFile?.exists() == true &&
                    !storePwd.isNullOrBlank() &&
                    !keyAliasEnv.isNullOrBlank() &&
                    !keyPwd.isNullOrBlank()

            if (hasReleaseCreds) {
                storeFile = keystoreFile
                storePassword = storePwd
                keyAlias = keyAliasEnv
                keyPassword = keyPwd
            } else {
                val debugConfig = signingConfigs.getByName("debug")
                logger.lifecycle(
                    "Release signing keys not set; falling back to debug signing config for local builds.",
                )
                storeFile = debugConfig.storeFile
                storePassword = debugConfig.storePassword
                keyAlias = debugConfig.keyAlias
                keyPassword = debugConfig.keyPassword
            }
        }
    }

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
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
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

    lint {
        baseline = file("lint-baseline.xml")
        warningsAsErrors = true
        abortOnError = true
        checkReleaseBuilds = true
        sarifReport = true
        htmlReport = true
        xmlReport = true
        disable += setOf("GradleDependency")
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
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")

    testImplementation("app.cash.paparazzi:paparazzi:1.3.4")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("androidx.test:core-ktx:1.5.0")
}

tasks.withType<Test>().configureEach {
    useJUnit()
    extensions.configure(JacocoTaskExtension::class.java) {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register<JacocoReport>("jacocoTestDebugUnitTestReport") {
    dependsOn("testDebugUnitTest")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    val debugTree =
        fileTree("$buildDir/tmp/kotlin-classes/debug") {
            exclude("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/androidx/**")
        }
    classDirectories.setFrom(debugTree)
    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(
        fileTree(buildDir) {
            include(
                "jacoco/testDebugUnitTest.exec",
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            )
        },
    )
}
