package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AdvertPropertyWithCheckboxItemBinding
import com.nexis.obuluyor.model.PropData

class PropertiesCheckboxAdapter(var propDataList: ArrayList<PropData>) : RecyclerView.Adapter<PropertiesCheckboxAdapter.PropertiesCheckboxHolder>() {
    private lateinit var v: AdvertPropertyWithCheckboxItemBinding
    private lateinit var onCheckedListener: PropertiesCheckedListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesCheckboxHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.advert_property_with_checkbox_item, parent, false)
        return PropertiesCheckboxHolder(v)
    }

    override fun onBindViewHolder(holder: PropertiesCheckboxHolder, position: Int) {
        holder.pCV.propdata = propDataList.get(position)

        holder.checkBox.setOnCheckedChangeListener { compoundButton, b ->
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                onCheckedListener.onChecked(propDataList.get(aPos), b)
        }
    }

    override fun getItemCount() = propDataList.size

    inner class PropertiesCheckboxHolder(var pCV: AdvertPropertyWithCheckboxItemBinding) : RecyclerView.ViewHolder(pCV.root) {
        val checkBox: CheckBox = pCV.root.findViewById(R.id.advert_property_checkboxItem)
        val linearItem: LinearLayout = pCV.root.findViewById(R.id.advert_property_linearItem)
    }

    fun loadData(propsData: ArrayList<PropData>){
        propDataList = propsData
        notifyDataSetChanged()
    }

    interface PropertiesCheckedListener {
        fun onChecked(propData: PropData, checkedState: Boolean)
    }

    fun setOnPropertiesCheckedListener(onCheckedListener: PropertiesCheckedListener){
        this.onCheckedListener = onCheckedListener
    }
}