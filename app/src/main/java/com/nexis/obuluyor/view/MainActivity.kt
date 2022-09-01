package com.nexis.obuluyor.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nexis.obuluyor.R
import com.nexis.obuluyor.util.Singleton

class MainActivity : AppCompatActivity() {
    private var themeMode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val nightModeFlags: Int = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                themeMode = "Dark"
                setTheme(R.style.Theme_Dark)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                themeMode = "Light"
                setTheme(R.style.Theme_Light)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                themeMode = "Light"
                setTheme(R.style.Theme_Light)
            }
        }
        Singleton.themeMode = themeMode

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        Singleton.showExitTheAppDialog(this)
    }
}