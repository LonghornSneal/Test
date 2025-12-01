package com.cosmobond.watchface

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class ConfigActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val label =
            TextView(this).apply {
                text = getString(R.string.app_name)
                textSize = 18f
                setPadding(32, 32, 32, 32)
            }
        setContentView(label)
    }
}
