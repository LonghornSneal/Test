plugins {
    id("com.android.application") version "8.5.2" apply false
    kotlin("android") version "1.9.24" apply false
    id("com.github.triplet.play") version "3.12.1" apply false
    id("com.diffplug.spotless") version "6.25.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
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
