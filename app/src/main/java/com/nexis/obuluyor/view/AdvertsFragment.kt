package com.nexis.obuluyor.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AdvertsAdapter
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AdvertsViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_advert_details.*
import kotlinx.android.synthetic.main.fragment_adverts.*

class AdvertsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var advertsViewModel: AdvertsViewModel

    private lateinit var advertList: List<Advert>
    private lateinit var advertsAdapter: AdvertsAdapter
    private lateinit var categoryData: Category
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = AdvertsFragmentArgs.fromBundle(it).userId
            categoryData = AdvertsFragmentArgs.fromBundle(it).categoryData

            custom_toolbar_imgPerson.visibility = View.VISIBLE
            custom_toolbar_imgClose.visibility = View.VISIBLE

            adverts_fragment_recyclerView.setHasFixedSize(true)
            adverts_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            advertsAdapter = AdvertsAdapter(arrayListOf())
            adverts_fragment_recyclerView.adapter = advertsAdapter

            advertsViewModel = ViewModelProvider(this).get(AdvertsViewModel::class.java)
            observeLiveData()
            advertsViewModel.getAdverts(1, 1)

            custom_toolbar_imgPerson.setOnClickListener(this)
            custom_toolbar_imgClose.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adverts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgPerson -> goToProfilePage(userId)
                R.id.custom_toolbar_imgClose -> backToMainPage(userId)
            }
        }
    }

    private fun observeLiveData(){
        advertsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        advertsViewModel.advertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                advertList = it
                advertsAdapter.loadData(advertList)

                advertsAdapter.setAdvertOnItemClickListener(object : AdvertsAdapter.AdvertOnItemClickListener{
                    override fun onItemClick(advert: Advert) {
                        goToAdvertDetailsPage(advert, userId, categoryData)
                    }
                })
            }
        })
    }

    private fun goToProfilePage(userId: Int){
        navDirections = AdvertsFragmentDirections.actionAdvertsFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun backToMainPage(userId: Int){
        navDirections = AdvertsFragmentDirections.actionAdvertsFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToAdvertDetailsPage(advert: Advert, userId: Int, category: Category){
        navDirections = AdvertsFragmentDirections.actionAdvertsFragmentToAdvertDetailsFragment(category, userId, advert)
        Navigation.findNavController(v).navigate(navDirections)
    }
}