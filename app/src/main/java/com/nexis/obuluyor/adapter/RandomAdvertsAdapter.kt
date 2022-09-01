package com.nexis.obuluyor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.RandomAdvertItemBinding
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.view.MainFragmentDirections

class RandomAdvertsAdapter(var advertList: List<Advert>, val vV: View, val userId: Int) : RecyclerView.Adapter<RandomAdvertsAdapter.RandomAdvertsHolder>() {
    private lateinit var v: RandomAdvertItemBinding
    private lateinit var navDirections: NavDirections
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomAdvertsHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.random_advert_item, parent, false)
        return RandomAdvertsHolder(v)
    }

    override fun onBindViewHolder(holder: RandomAdvertsHolder, position: Int) {
        holder.rA.advert = advertList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION)
                goToAdvertDetailsPage(advertList.get(aPos), userId, vV)
        }
    }

    override fun getItemCount() = advertList.size

    inner class RandomAdvertsHolder(var rA: RandomAdvertItemBinding) : RecyclerView.ViewHolder(rA.root)

    fun loadData(adverts: List<Advert>){
        advertList = adverts
        notifyDataSetChanged()
    }

    private fun goToAdvertDetailsPage(advert: Advert, userId: Int, v: View){
        navDirections = MainFragmentDirections.actionMainFragmentToAdvertDetailsFragment(
            null,
            userId,
            advert,
            null,
            false,
            null
        )
        Navigation.findNavController(v).navigate(navDirections)
    }
}