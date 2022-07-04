package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AdvertSubCategoryItemBinding
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.SubCategory

class AddAdvertSubCategoriesAdapter(var subCategoryList: List<SubCategory>) : RecyclerView.Adapter<AddAdvertSubCategoriesAdapter.AddAdvertSubCategoriesHolder>() {
    private lateinit var v: AdvertSubCategoryItemBinding
    private lateinit var listener: AdvertSubCategoryOnItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddAdvertSubCategoriesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.advert_sub_category_item, parent, false)
        return AddAdvertSubCategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: AddAdvertSubCategoriesHolder, position: Int) {
        holder.cV.subcategory = subCategoryList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(subCategoryList.get(aPos))
        }
    }

    override fun getItemCount() = subCategoryList.size

    inner class AddAdvertSubCategoriesHolder(var cV: AdvertSubCategoryItemBinding) : RecyclerView.ViewHolder(cV.root)

    fun loadData(subCategories: List<SubCategory>){
        subCategoryList = subCategories
        notifyDataSetChanged()
    }

    interface AdvertSubCategoryOnItemClickListener{
        fun onItemClick(subCategory: SubCategory)
    }

    fun setAdvertSubCategoryOnItemClickListener(listener: AdvertSubCategoryOnItemClickListener){
        this.listener = listener
    }
}