package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.NavCategoryItemBinding
import com.nexis.obuluyor.model.Category

class NavCategoriesAdapter(var categoryList: List<Category>) : RecyclerView.Adapter<NavCategoriesAdapter.NavCategoriesHolder>() {
    private lateinit var v: NavCategoryItemBinding
    private lateinit var listener: NavCategoriesItemSelectedListener
    private var aPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavCategoriesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.nav_category_item, parent, false)
        return NavCategoriesHolder(v)
    }

    override fun onBindViewHolder(holder: NavCategoriesHolder, position: Int) {
        holder.nV.category = categoryList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(categoryList.get(aPos))
        }
    }

    override fun getItemCount() = categoryList.size

    inner class NavCategoriesHolder(var nV: NavCategoryItemBinding) : RecyclerView.ViewHolder(nV.root)

    fun loadData(categories: List<Category>){
        categoryList = categories
        notifyDataSetChanged()
    }

    interface NavCategoriesItemSelectedListener{
        fun onItemClick(category: Category)
    }

    fun setNavCategoriesOnItemSelectedListener(listener: NavCategoriesItemSelectedListener){
        this.listener = listener
    }
}