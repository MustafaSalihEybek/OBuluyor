package com.nexis.obuluyor.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nexis.obuluyor.R
import com.nexis.obuluyor.util.Singleton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        Singleton.showExitTheAppDialog(this)
    }
}