package com.cosmobond.watchface.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineProfileRule.collect(
        packageName = PACKAGE_NAME,
    ) {
        // Resource-only Watch Face Format app has no launchable activity.
        device.waitForIdle()
    }

    companion object {
        private const val PACKAGE_NAME = "com.cosmobond.watchface"
    }
}
