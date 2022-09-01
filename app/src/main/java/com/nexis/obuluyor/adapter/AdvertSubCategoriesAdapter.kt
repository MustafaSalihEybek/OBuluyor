package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AdvertSubCategoryItemBinding
import com.nexis.obuluyor.model.SubCategory

class AdvertSubCategoriesAdapter(var subCategoryList: List<SubCategory>) : RecyclerView.Adapter<AdvertSubCategoriesAdapter.AdvertSubCategoriesHolder>() {
    private lateinit var v: AdvertSubCategoryItemBinding
    private lateinit var listener: AdvertsSubCategoriesOnItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertSubCategoriesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.advert_sub_category_item, parent, false)
        return AdvertSubCategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: AdvertSubCategoriesHolder, position: Int) {
        holder.sV.subcategory = subCategoryList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(subCategoryList.get(aPos))
        }
    }

    override fun getItemCount() = subCategoryList.size

    inner class AdvertSubCategoriesHolder(var sV: AdvertSubCategoryItemBinding) : RecyclerView.ViewHolder(sV.root)

    fun loadData(subCategories: List<SubCategory>) {
        subCategoryList = subCategories
        notifyDataSetChanged()
    }

    interface AdvertsSubCategoriesOnItemClickListener{
        fun onItemClick(subCategory: SubCategory)
    }

    fun setAdvertsSubCategoriesOnItemClickListener(listener: AdvertsSubCategoriesOnItemClickListener){
        this.listener = listener
    }
}