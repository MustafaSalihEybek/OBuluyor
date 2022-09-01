package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.nexis.obuluyor.R

class SplashDialog(mContext: Context) : Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_dialog)
    }
}