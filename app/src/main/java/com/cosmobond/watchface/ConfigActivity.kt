package com.cosmobond.watchface

import android.app.Activity
import android.os.Bundle
import android.widget.Switch

class ConfigActivity : Activity() {
    private lateinit var settingsRepository: CompanionSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsRepository = CompanionSettingsRepository(applicationContext)
        setContentView(R.layout.activity_config)

        val settings = settingsRepository.current()
        val reducedMotion = findViewById<Switch>(R.id.reduced_motion_switch)
        val haptics = findViewById<Switch>(R.id.haptics_switch)
        val mic = findViewById<Switch>(R.id.mic_switch)

        reducedMotion.isChecked = settings.reducedMotion
        haptics.isChecked = settings.hapticsEnabled
        mic.isChecked = settings.micToggleEnabled

        reducedMotion.setOnCheckedChangeListener { _, isChecked ->
            settingsRepository.setReducedMotion(isChecked)
        }
        haptics.setOnCheckedChangeListener { _, isChecked ->
            settingsRepository.setHapticsEnabled(isChecked)
        }
        mic.setOnCheckedChangeListener { _, isChecked ->
            settingsRepository.setMicToggle(isChecked)
        }
    }

    override fun onDestroy() {
        settingsRepository.close()
        super.onDestroy()
    }
}
