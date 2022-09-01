package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.SendMessageDialogBinding
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AdvertDetailsViewModel
import kotlinx.android.synthetic.main.send_message_dialog.*

class SendMessageDialog(val v: View, val aDV: AdvertDetailsViewModel, val userData: User, val targetData: User, val advertId: Int) : Dialog(v.context), View.OnClickListener {
    private lateinit var txtMessageContent: String
    private lateinit var sV: SendMessageDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sV = DataBindingUtil.inflate(LayoutInflater.from(v.context), R.layout.send_message_dialog, null, false)
        sV.user = userData
        setContentView(sV.root)

        window?.let {
            it.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        }

        send_message_dialog_imgClose.setOnClickListener(this)
        send_message_dialog_btnClose.setOnClickListener(this)
        send_message_dialog_btnSend.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.send_message_dialog_imgClose -> closeThisDialog()
                R.id.send_message_dialog_btnClose -> closeThisDialog()
                R.id.send_message_dialog_btnSend -> sendMessage()
            }
        }
    }

    private fun closeThisDialog(){
        if (this.isShowing)
            this.dismiss()
    }

    private fun sendMessage(){
        txtMessageContent = send_message_dialog_editMessage.text.toString().trim()

        if (!txtMessageContent.isEmpty()){
            aDV.sendMessage(userData.Id, targetData.Id, txtMessageContent, advertId)
            closeThisDialog()
        }
        else
            txtMessageContent.show(v, "Lütfen bir mesaj içeriği giriniz")
    }
}