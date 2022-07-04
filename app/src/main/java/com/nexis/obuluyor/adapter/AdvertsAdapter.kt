package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AdvertItemBinding
import com.nexis.obuluyor.model.Advert

class AdvertsAdapter(var advertList: List<Advert>) : RecyclerView.Adapter<AdvertsAdapter.AdvertsHolder>() {
    private lateinit var v: AdvertItemBinding
    private lateinit var listener: AdvertOnItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertsHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.advert_item, parent, false)
        return AdvertsHolder(v)
    }

    override fun onBindViewHolder(holder: AdvertsHolder, position: Int) {
        holder.aV.advert = advertList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(advertList.get(aPos))
        }
    }

    override fun getItemCount() = advertList.size

    inner class AdvertsHolder(var aV: AdvertItemBinding) : RecyclerView.ViewHolder(aV.root)

    fun loadData(adverts: List<Advert>){
        advertList = adverts
        notifyDataSetChanged()
    }

    interface AdvertOnItemClickListener{
        fun onItemClick(advert: Advert)
    }

    fun setAdvertOnItemClickListener(listener: AdvertOnItemClickListener){
        this.listener = listener
    }
}