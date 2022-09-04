package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.ModuleItemBinding
import com.nexis.obuluyor.model.Module

class ModulesAdapter(var moduleList: List<Module>, var moduleNameList: ArrayList<String>) : RecyclerView.Adapter<ModulesAdapter.ModulesHolder>() {
    private lateinit var v: ModuleItemBinding
    private lateinit var listener: ModulesItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModulesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.module_item, parent, false)
        return ModulesHolder(v)
    }

    override fun onBindViewHolder(holder: ModulesHolder, position: Int) {
        holder.mI.module = moduleList.get(position)

        if (!moduleNameList.get(position).equals(""))
            holder.subTitle.text = moduleNameList.get(position)
        else
            holder.subTitle.text = "${moduleList.get(position).name} bilgisini giriniz"

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(moduleList.get(position), aPos)
        }
    }

    override fun getItemCount() = moduleList.size

    inner class ModulesHolder(var mI: ModuleItemBinding) : RecyclerView.ViewHolder(mI.root) {
        val subTitle: TextView = mI.root.findViewById(R.id.module_item_txtSubTitle)
    }

    fun loadData(modules: List<Module>, moduleNames: ArrayList<String>){
        moduleList = modules
        moduleNameList = moduleNames
        notifyDataSetChanged()
    }

    interface ModulesItemClickListener {
        fun onItemClick(moduleData: Module, mIn: Int)
    }

    fun setModulesOnItemClickListener(listener: ModulesItemClickListener){
        this.listener = listener
    }

    fun loadModuleNames(moduleNames: ArrayList<String>){
        moduleNameList = moduleNames
        notifyDataSetChanged()
    }
}