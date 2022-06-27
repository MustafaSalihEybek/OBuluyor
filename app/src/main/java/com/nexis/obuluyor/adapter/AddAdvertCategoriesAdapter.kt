package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AdvertCategoryItemBinding
import com.nexis.obuluyor.model.Category

class AddAdvertCategoriesAdapter(var categoryList: List<Category>) : RecyclerView.Adapter<AddAdvertCategoriesAdapter.SubCategoriesHolder>() {
    private lateinit var v: AdvertCategoryItemBinding
    private lateinit var listener: AdvertCategoryOnItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoriesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.advert_category_item, parent, false)
        return SubCategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: SubCategoriesHolder, position: Int) {
        holder.aI.category = categoryList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(categoryList.get(aPos))
        }
    }

    override fun getItemCount() = categoryList.size

    inner class SubCategoriesHolder(var aI: AdvertCategoryItemBinding) : RecyclerView.ViewHolder(aI.root)

    fun loadData(categories: List<Category>){
        categoryList = categories
        notifyDataSetChanged()
    }

    interface AdvertCategoryOnItemClickListener{
        fun onItemClick(category: Category)
    }

    fun setAdvertCategoryOnItemClickListener(listener: AdvertCategoryOnItemClickListener){
        this.listener = listener
    }
}