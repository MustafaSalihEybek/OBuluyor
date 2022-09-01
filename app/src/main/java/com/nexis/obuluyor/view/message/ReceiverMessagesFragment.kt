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
import kotlinx.android.synthetic.main.fragment_receiver_messages.*

class ReceiverMessagesFragment(val userId: Int, val messageType: String) : Fragment() {
    private lateinit var v: View
    private lateinit var senderAndReceiverMessagesViewModel: SenderAndReceiverMessagesViewModel

    private lateinit var messageList: List<Message>
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var selectedMessage: Message

    private fun init(){
        receiver_messages_fragment_recyclerView.setHasFixedSize(true)
        receiver_messages_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        messagesAdapter = MessagesAdapter(arrayListOf())
        receiver_messages_fragment_recyclerView.adapter = messagesAdapter

        senderAndReceiverMessagesViewModel = ViewModelProvider(this).get(SenderAndReceiverMessagesViewModel::class.java)
        observeLiveData()
        senderAndReceiverMessagesViewModel.getMessages(userId, messageType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receiver_messages, container, false)
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
                it.show(v, it)
            }
        })

        senderAndReceiverMessagesViewModel.messageList.observe(viewLifecycleOwner, Observer {
            it?.let {
                messageList = it

                if (it.isNotEmpty()){
                    if (it.get(0).mesaj != null){
                        receiver_messages_fragment_recyclerView.visibility = View.VISIBLE
                        receiver_messages_fragment_txtWarning.visibility = View.GONE
                        messagesAdapter.loadData(it)

                        messagesAdapter.setOnMessageItemClickListener(object : MessagesAdapter.MessageItemClickListener{
                            override fun onItemClick(message: Message) {
                                selectedMessage = message
                                senderAndReceiverMessagesViewModel.getSenderUserData(message.gonderen!!)
                            }
                        })
                    }else{
                        receiver_messages_fragment_txtWarning.visibility = View.VISIBLE
                        receiver_messages_fragment_recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }
}