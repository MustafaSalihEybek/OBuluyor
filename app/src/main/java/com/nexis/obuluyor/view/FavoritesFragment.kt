package com.nexis.obuluyor.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AdvertsAdapter
import com.nexis.obuluyor.adapter.FavoritesAdapter
import com.nexis.obuluyor.model.Favorite
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.FavoritesViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var navDirections: NavDirections

    private var userId: Int = -1
    private lateinit var favoriteList: List<Favorite>
    private lateinit var favoritesAdapter: FavoritesAdapter

    private fun init(){
        arguments?.let {
            userId = FavoritesFragmentArgs.fromBundle(it).userId

            favorites_fragment_recyclerView.setHasFixedSize(true)
            favorites_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            favoritesAdapter = FavoritesAdapter(arrayListOf())
            favorites_fragment_recyclerView.adapter = favoritesAdapter

            favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
            observeLiveData()
            favoritesViewModel.getFavorites(userId)

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgClose -> backToPage(userId)
            }
        }
    }

    private fun backToPage(userId: Int){
        navDirections = FavoritesFragmentDirections.actionFavoritesFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun observeLiveData(){
        favoritesViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        favoritesViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
                Singleton.closeRemoveFavoriteDialog()
                favoritesViewModel.getFavorites(userId)
            }
        })

        favoritesViewModel.favoriteList.observe(viewLifecycleOwner, Observer {
            it?.let {
                favoriteList = it

                if (it.get(0).Id != null){
                    favorites_fragment_recyclerView.visibility = View.VISIBLE
                    favorites_fragment_txtNoData.visibility = View.GONE

                    favoritesAdapter.loadData(it)
                    favoritesAdapter.setFavoriteOnItemClickListener(object : FavoritesAdapter.FavoriteItemClickListener{
                        override fun onItemClick(favorite: Favorite) {
                            Singleton.showRemoveFavoriteDialog(v.context, userId, favorite.favoriilanId, favorite.favoriId, favoritesViewModel)
                        }
                    })
                } else {
                    favorites_fragment_txtNoData.visibility = View.VISIBLE
                    favorites_fragment_recyclerView.visibility = View.GONE
                }
            }
        })
    }
}