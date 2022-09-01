package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.FavoriteItemBinding
import com.nexis.obuluyor.model.Favorite

class FavoritesAdapter(var favoriteList: List<Favorite>) : RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder>() {
    private lateinit var v: FavoriteItemBinding
    private lateinit var listener: FavoriteItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.favorite_item, parent, false)
        return FavoritesHolder(v)
    }

    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
        holder.fV.favorite = favoriteList.get(position)

        holder.imgFilledStar.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                listener.onItemClick(favoriteList.get(aPos))
        }
    }

    override fun getItemCount() = favoriteList.size

    inner class FavoritesHolder(var fV: FavoriteItemBinding) : RecyclerView.ViewHolder(fV.root) {
        var imgFilledStar: ImageView = fV.root.findViewById(R.id.favorite_item_imgFilledStar)
    }

    fun loadData(favorites: List<Favorite>){
        favoriteList = favorites
        notifyDataSetChanged()
    }

    interface FavoriteItemClickListener {
        fun onItemClick(favorite: Favorite)
    }

    fun setFavoriteOnItemClickListener(listener: FavoriteItemClickListener){
        this.listener = listener
    }
}