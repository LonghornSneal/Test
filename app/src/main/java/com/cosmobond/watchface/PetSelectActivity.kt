package com.cosmobond.watchface

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class PetSelectActivity : Activity() {
    private lateinit var settingsRepository: CompanionSettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_select)
        settingsRepository = CompanionSettingsRepository(applicationContext)

        findViewById<Button>(R.id.select_pet_button).setOnClickListener {
            settingsRepository.setSelectedPet(CompanionSettingsRepository.DEFAULT_PET_ID)
            startActivity(Intent(this, LayoutSetupActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        settingsRepository.close()
        super.onDestroy()
    }
}
