package com.cosmobond.watchface

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class SetWatchFaceActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_watch_face)

        findViewById<Button>(R.id.open_picker_button).setOnClickListener {
            if (!launchPicker()) {
                Toast.makeText(this, "Could not open watch-face picker on this build.", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<Button>(R.id.open_pet_setup_button).setOnClickListener {
            startActivity(Intent(this, PetSelectActivity::class.java))
        }
        findViewById<Button>(R.id.open_config_button).setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }
    }

    private fun launchPicker(): Boolean {
        val candidates =
            listOf(
                Intent("com.google.android.clockwork.home.action.CHANGE_WATCH_FACE"),
                Intent("com.google.android.clockwork.action.SET_WATCH_FACE"),
                Intent("android.service.wallpaper.LIVE_WALLPAPER_CHOOSER").addCategory(Intent.CATEGORY_DEFAULT),
            )
        for (intent in candidates) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(packageManager) != null) {
                return runCatching { startActivity(intent) }.isSuccess
            }
        }
        return false
    }
}
