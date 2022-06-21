package com.nexis.obuluyor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.MainCategoryItemBinding
import com.nexis.obuluyor.model.Category

class CategoriesAdapter(var categoryList: List<Category>, val mContext: Context) : RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder>() {
    private lateinit var v: MainCategoryItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.main_category_item, parent, false)
        return CategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        holder.cI.category = categoryList.get(position)
    }

    override fun getItemCount() = categoryList.size

    inner class CategoriesHolder(var cI: MainCategoryItemBinding) : RecyclerView.ViewHolder(cI.root)

    fun loadData(categories: List<Category>){
        categoryList = categories
        notifyDataSetChanged()
    }
}