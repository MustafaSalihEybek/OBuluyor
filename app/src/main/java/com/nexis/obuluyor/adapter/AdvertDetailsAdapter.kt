package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AdvertDetailItemBinding
import com.nexis.obuluyor.model.AdvertDetail

class AdvertDetailsAdapter(val advertDetailList: ArrayList<AdvertDetail>) : RecyclerView.Adapter<AdvertDetailsAdapter.AdvertDetailsHolder>() {
    private lateinit var v: AdvertDetailItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertDetailsHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.advert_detail_item, parent, false)
        return AdvertDetailsHolder(v)
    }

    override fun onBindViewHolder(holder: AdvertDetailsHolder, position: Int) {
        holder.aDV.advertdetail = advertDetailList.get(position)
    }

    override fun getItemCount() = advertDetailList.size

    inner class AdvertDetailsHolder(var aDV: AdvertDetailItemBinding) : RecyclerView.ViewHolder(aDV.root)
}