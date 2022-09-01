package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.nexis.obuluyor.R
import kotlinx.android.synthetic.main.loading_dialog.*

class LoadingDialog(mContext: Context, val message: String) : Dialog(mContext) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)

        window?.let {
            it.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }

        loading_dialog_txtMessage.text = message
    }
}