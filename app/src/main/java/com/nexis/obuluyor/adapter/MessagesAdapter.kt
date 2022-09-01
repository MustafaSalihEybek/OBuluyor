package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.MessageItemBinding
import com.nexis.obuluyor.model.Message

class MessagesAdapter(var messageList: List<Message>) : RecyclerView.Adapter<MessagesAdapter.MessagesHolder>() {
    private lateinit var v: MessageItemBinding
    private lateinit var listener: MessagesAdapter.MessageItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.message_item, parent, false)
        return MessagesHolder(v)
    }

    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {
        holder.mV.message = messageList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(messageList.get(aPos))
        }
    }

    override fun getItemCount() = messageList.size

    inner class MessagesHolder(var mV: MessageItemBinding) : RecyclerView.ViewHolder(mV.root)

    fun loadData(messages: List<Message>){
        messageList = messages
        notifyDataSetChanged()
    }

    interface MessageItemClickListener{
        fun onItemClick(message: Message)
    }

    fun setOnMessageItemClickListener(listener: MessageItemClickListener){
        this.listener = listener
    }
}