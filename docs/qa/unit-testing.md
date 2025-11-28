# Unit Testing Summary

- **Command:** `./gradlew testDebugUnitTest jacocoTestDebugUnitTestReport` (JAVA_HOME set to Temurin 17).
- **Coverage:** 81% instructions (Jacoco report at `app/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html`).
- **Focus:** time formatting (`CompanionTimeFormatter`), palette selection (`CompanionPalette`), and complication slot mapping (`CompanionComplicationSlots`), plus renderer smoke tests.
- **Artifacts:** JUnit XML at `app/build/test-results/testDebugUnitTest/`, HTML test report at `app/build/reports/tests/testDebugUnitTest/index.html`, Jacoco HTML under `app/build/reports/jacoco/jacocoTestDebugUnitTestReport/`, raw log `docs/qa/screenshots/task8-test.log`.
