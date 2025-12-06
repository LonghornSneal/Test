package com.cosmobond.watchface

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup

class LayoutSetupActivity : Activity() {
    private lateinit var layoutRepo: LayoutPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_setup)
        layoutRepo = LayoutPreferencesRepository(applicationContext)

        val timeGroup = findViewById<RadioGroup>(R.id.time_position_group)
        val fontGroup = findViewById<RadioGroup>(R.id.font_group)
        val showDate = findViewById<CheckBox>(R.id.show_date_checkbox)

        val prefs = layoutRepo.current()
        when (prefs.timePosition) {
            TimePosition.TOP -> timeGroup.check(R.id.time_top)
            TimePosition.BOTTOM -> timeGroup.check(R.id.time_bottom)
            else -> timeGroup.check(R.id.time_center)
        }
        when (prefs.fontVariant) {
            FontVariant.MONO -> fontGroup.check(R.id.font_mono)
            else -> fontGroup.check(R.id.font_default)
        }
        showDate.isChecked = prefs.showDate

        findViewById<Button>(R.id.save_layout_button).setOnClickListener {
            val timeSelection =
                when (timeGroup.checkedRadioButtonId) {
                    R.id.time_top -> TimePosition.TOP
                    R.id.time_bottom -> TimePosition.BOTTOM
                    else -> TimePosition.CENTER
                }
            val fontSelection =
                when (fontGroup.checkedRadioButtonId) {
                    R.id.font_mono -> FontVariant.MONO
                    else -> FontVariant.DEFAULT
                }
            layoutRepo.setTimePosition(timeSelection)
            layoutRepo.setFontVariant(fontSelection)
            layoutRepo.setShowDate(showDate.isChecked)
            startActivity(Intent(this, SetWatchFaceActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }
    }

    override fun onDestroy() {
        layoutRepo.close()
        super.onDestroy()
    }
}
