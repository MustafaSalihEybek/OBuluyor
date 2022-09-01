package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AdvertPropertyItemBinding
import com.nexis.obuluyor.model.GroupTitle
import com.nexis.obuluyor.model.Prop
import com.nexis.obuluyor.model.PropData

class PropertiesAdapter(val propData: Pair<ArrayList<PropData>, ArrayList<Prop>>) : RecyclerView.Adapter<PropertiesAdapter.PropertiesHolder>() {
    private lateinit var v: AdvertPropertyItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.advert_property_item, parent, false)
        return PropertiesHolder(v)
    }

    override fun onBindViewHolder(holder: PropertiesHolder, position: Int) {
        holder.pV.propdata = propData.first.get(position)
        holder.pV.prop = propData.second.get(position)
    }

    override fun getItemCount() = propData.first.size

    inner class PropertiesHolder(var pV: AdvertPropertyItemBinding) : RecyclerView.ViewHolder(pV.root)
}