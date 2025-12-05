import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("com.android.application") version "8.13.1" apply false
    kotlin("android") version "1.9.24" apply false
    id("com.github.triplet.play") version "3.12.2" apply false
    id("androidx.baselineprofile") version "1.2.4" apply false
    id("app.cash.paparazzi") version "1.3.5" apply false
    id("com.diffplug.spotless") version "8.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

spotless {
    kotlin {
        target("app/src/**/*.kt")
        targetExclude("**/build/**")
        ktlint("1.2.1")
        trimTrailingWhitespace()
        endWithNewline()
        indentWithSpaces()
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**")
        ktlint("1.2.1")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("config/detekt/detekt.yml"))
    source.setFrom(files("app/src/main/java", "app/src/test/java"))
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(false)
        sarif.required.set(true)
    }
}
