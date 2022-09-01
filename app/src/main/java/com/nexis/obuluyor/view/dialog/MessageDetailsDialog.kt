package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.MessageDetailsDialogBinding
import com.nexis.obuluyor.model.Message
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.SenderAndReceiverMessagesViewModel
import kotlinx.android.synthetic.main.message_details_dialog.*

class MessageDetailsDialog(val mContext: Context, val messageData: Message, val senderData: User, val userId: Int, val srV: SenderAndReceiverMessagesViewModel, val owner: LifecycleOwner) : Dialog(mContext), View.OnClickListener {
    private lateinit var messageDetailsBinding: MessageDetailsDialogBinding
    private lateinit var txtMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messageDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.message_details_dialog, null, false)
        messageDetailsBinding.message = messageData
        messageDetailsBinding.user = senderData
        setContentView(messageDetailsBinding.root)

        window?.let {
            it.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        }

        observeLiveData()
        setVisibilityProperties(messageData.gonderen == userId)

        if (messageData.gonderen == userId){
            message_details_dialog_txtMessageType.text = "Gönderilen Mesaj"
        } else {
            message_details_dialog_txtMessageType.text = "Alınan Mesaj"
            message_details_dialog_txtSendMessageType.text = "Gönderilecek Mesaj"
        }

        message_details_dialog_btnClose1.setOnClickListener(this)
        message_details_dialog_btnClose2.setOnClickListener(this)
        message_details_dialog_btnSend.setOnClickListener(this)
        message_details_dialog_imgClose.setOnClickListener(this)
    }

    private fun observeLiveData(){
        srV.sendMessage.observe(owner, Observer {
            it?.let {
                it.show(messageDetailsBinding.root, it)

                object : CountDownTimer(1000, 1000){
                    override fun onTick(p0: Long) {}

                    override fun onFinish() {
                        closeThisDialog()
                    }
                }.start()
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id){
                R.id.message_details_dialog_btnClose1 -> closeThisDialog()
                R.id.message_details_dialog_btnClose2 -> closeThisDialog()
                R.id.message_details_dialog_imgClose -> closeThisDialog()
                R.id.message_details_dialog_btnSend -> sendMessage()
            }
        }
    }

    private fun sendMessage(){
        txtMessage = message_details_dialog_editMessage.text.toString().trim()

        if (!txtMessage.isEmpty())
            srV.sendMessage(userId, senderData.Id, txtMessage, messageData.advert!!.Id!!)
        else
            txtMessage.show(messageDetailsBinding.root, "Lütfen mesaj içeriğini giriniz")
    }

    private fun closeThisDialog(){
        if (this.isShowing)
            this.dismiss()
    }

    private fun setVisibilityProperties(isSender: Boolean){
        if (isSender){
            message_details_dialog_btnClose1.visibility = View.VISIBLE

            message_details_dialog_linearBottom.visibility = View.GONE
            message_details_dialog_editMessage.visibility = View.GONE
            message_details_dialog_txtSendMessageType.visibility = View.GONE
        } else {
            message_details_dialog_linearBottom.visibility = View.VISIBLE
            message_details_dialog_editMessage.visibility = View.VISIBLE
            message_details_dialog_txtSendMessageType.visibility = View.VISIBLE

            message_details_dialog_btnClose1.visibility = View.GONE
        }
    }
}