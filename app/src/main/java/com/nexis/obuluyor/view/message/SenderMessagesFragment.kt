package com.nexis.obuluyor.view.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.MessagesAdapter
import com.nexis.obuluyor.model.Message
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.SenderAndReceiverMessagesViewModel
import kotlinx.android.synthetic.main.fragment_sender_messages.*

class SenderMessagesFragment(val userId: Int, val messageType: String) : Fragment() {
    private lateinit var v: View
    private lateinit var senderAndReceiverMessagesViewModel: SenderAndReceiverMessagesViewModel

    private lateinit var messageList: List<Message>
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var selectedMessage: Message

    private fun init(){
        sender_messages_fragment_recyclerView.setHasFixedSize(true)
        sender_messages_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        messagesAdapter = MessagesAdapter(arrayListOf())
        sender_messages_fragment_recyclerView.adapter = messagesAdapter

        senderAndReceiverMessagesViewModel = ViewModelProvider(this).get(SenderAndReceiverMessagesViewModel::class.java)
        observeLiveData()
        senderAndReceiverMessagesViewModel.getMessages(userId, messageType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sender_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        senderAndReceiverMessagesViewModel.userData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Singleton.showMessageDetailsDialog(v.context, selectedMessage, it, userId, senderAndReceiverMessagesViewModel, viewLifecycleOwner)
            }
        })

        senderAndReceiverMessagesViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                println(it)
            }
        })

        senderAndReceiverMessagesViewModel.messageList.observe(viewLifecycleOwner, Observer {
            it?.let {
                messageList = it

                if (it.isNotEmpty()){
                    if (it.get(0).mesaj != null){
                        sender_messages_fragment_recyclerView.visibility = View.VISIBLE
                        sender_messages_fragment_txtWarning.visibility = View.GONE
                        messagesAdapter.loadData(it)

                        messagesAdapter.setOnMessageItemClickListener(object : MessagesAdapter.MessageItemClickListener{
                            override fun onItemClick(message: Message) {
                                selectedMessage = message
                                senderAndReceiverMessagesViewModel.getSenderUserData(message.gonderen!!)
                            }
                        })
                    }else{
                        sender_messages_fragment_txtWarning.visibility = View.VISIBLE
                        sender_messages_fragment_recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }
}