package com.cosmobond.watchface

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class SetWatchFaceActivity : Activity() {
    private lateinit var settingsRepository: CompanionSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsRepository = CompanionSettingsRepository(applicationContext)

        // Route users based on whether they've picked a pet yet.
        if (!settingsRepository.hasSelectedPet()) {
            startActivity(Intent(this, PetSelectActivity::class.java))
            finish()
            return
        }

        // If they already have a pet, drop them into layout setup directly.
        startActivity(Intent(this, LayoutSetupActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        settingsRepository.close()
        super.onDestroy()
    }
}

class LegacySetWatchFaceActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_watch_face)

        findViewById<Button>(R.id.open_pet_setup_button).setOnClickListener {
            startActivity(Intent(this, PetSelectActivity::class.java))
        }
        findViewById<Button>(R.id.open_config_button).setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
    }
}
