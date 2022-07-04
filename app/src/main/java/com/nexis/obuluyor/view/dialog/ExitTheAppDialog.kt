package com.nexis.obuluyor.view.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.nexis.obuluyor.R
import kotlinx.android.synthetic.main.exit_the_app_dialog.*

class ExitTheAppDialog(val mContext: Context) : Dialog(mContext), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exit_the_app_dialog)

        exit_the_app_dialog_imgClose.setOnClickListener(this)
        exit_the_app_dialog_btnNo.setOnClickListener(this)
        exit_the_app_dialog_btnYes.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.exit_the_app_dialog_imgClose -> closeThisDialog()
                R.id.exit_the_app_dialog_btnNo -> closeThisDialog()
                R.id.exit_the_app_dialog_btnYes -> exitTheApp()
            }
        }
    }

    private fun closeThisDialog(){
        if (this.isShowing)
            this.dismiss()
    }

    private fun exitTheApp(){
        (mContext as Activity).moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }
}