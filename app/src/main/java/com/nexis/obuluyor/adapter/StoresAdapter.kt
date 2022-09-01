package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.StoreItemBinding
import com.nexis.obuluyor.model.Store

class StoresAdapter(var storeList: List<Store>) : RecyclerView.Adapter<StoresAdapter.StoresHolder>() {
    private lateinit var v: StoreItemBinding
    private lateinit var listener: StoreItemClickListener
    private var aPos: Int = 0
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.store_item, parent, false)
        return StoresHolder(v)
    }

    override fun onBindViewHolder(holder: StoresHolder, position: Int) {
        holder.sV.store = storeList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(storeList.get(aPos))
        }
    }

    override fun getItemCount() = storeList.size

    inner class StoresHolder(var sV: StoreItemBinding) : RecyclerView.ViewHolder(sV.root)

    fun loadData(stores: List<Store>){
        storeList = stores
        notifyDataSetChanged()
    }

    interface StoreItemClickListener{
        fun onItemClick(storeData: Store)
    }

    fun setStoreOnItemClickListener(listener: StoreItemClickListener){
        this.listener = listener
    }
}