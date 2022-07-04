package com.nexis.obuluyor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.MainCategoryItemBinding
import com.nexis.obuluyor.model.Category

class CategoriesAdapter(var categoryList: List<Category>) : RecyclerView.Adapter<CategoriesAdapter.CategoriesHolder>() {
    private lateinit var v: MainCategoryItemBinding
    private lateinit var listener: CategoriesOnItemSelectedListener
    private var aPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.main_category_item, parent, false)
        return CategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: CategoriesHolder, position: Int) {
        holder.cI.category = categoryList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(categoryList.get(aPos))
        }
    }

    override fun getItemCount() = categoryList.size

    inner class CategoriesHolder(var cI: MainCategoryItemBinding) : RecyclerView.ViewHolder(cI.root)

    fun loadData(categories: List<Category>){
        categoryList = categories
        notifyDataSetChanged()
    }

    interface CategoriesOnItemSelectedListener{
        fun onItemClick(category: Category)
    }

    fun setCategoriesOnItemSelectedListener(listener: CategoriesOnItemSelectedListener){
        this.listener = listener
    }
}