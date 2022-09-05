package com.nexis.obuluyor.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AdvertsAdapter
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.util.AppUtils
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_adverts_by_search_result.*

class AdvertsBySearchResultFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections

    private lateinit var advertsAdapter: AdvertsAdapter
    private lateinit var advertList: Array<Advert>
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            advertList = AdvertsBySearchResultFragmentArgs.fromBundle(it).advertList
            userId = AdvertsBySearchResultFragmentArgs.fromBundle(it).userId

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)

            AppUtils.setLogoTint(custom_toolbar_imgLogo)

            adverts_by_search_result_fragment_recyclerView.setHasFixedSize(true)
            adverts_by_search_result_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            advertsAdapter = AdvertsAdapter(advertList.toMutableList())
            adverts_by_search_result_fragment_recyclerView.adapter = advertsAdapter

            advertsAdapter.setAdvertOnItemClickListener(object : AdvertsAdapter.AdvertOnItemClickListener{
                override fun onItemClick(advert: Advert) {
                    goToAdvertDetails(userId, advert, advertList)
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adverts_by_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id){
                R.id.custom_toolbar_imgClose -> backToMainPage(userId)
            }
        }
    }

    private fun backToMainPage(userId: Int){
        navDirections = AdvertsBySearchResultFragmentDirections.actionAdvertsBySearchResultFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToAdvertDetails(userId: Int, advert: Advert, advertList: Array<Advert>){
        navDirections = AdvertsBySearchResultFragmentDirections.actionAdvertsBySearchResultFragmentToAdvertDetailsFragment(
            null,
            userId,
            advert,
            null,
            true,
            advertList
        )
        Navigation.findNavController(v).navigate(navDirections)
    }
}