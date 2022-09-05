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
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.UserAdvertsViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_adverts.*
import kotlinx.android.synthetic.main.fragment_user_adverts.*

class UserAdvertsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var userAdvertsViewModel: UserAdvertsViewModel

    private lateinit var advertList: List<Advert>
    private lateinit var advertsAdapter: AdvertsAdapter
    private var userId: Int = -1
    private var confirm: Int = 0

    private fun init(){
        arguments?.let {
            userId = UserAdvertsFragmentArgs.fromBundle(it).userId
            confirm = UserAdvertsFragmentArgs.fromBundle(it).confirm

            user_adverts_fragment_recyclerView.setHasFixedSize(true)
            user_adverts_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            advertsAdapter = AdvertsAdapter(arrayListOf())
            user_adverts_fragment_recyclerView.adapter = advertsAdapter

            userAdvertsViewModel = ViewModelProvider(this).get(UserAdvertsViewModel::class.java)
            observeLiveData()
            userAdvertsViewModel.getUserAdverts(confirm, 1, userId)

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)

            AppUtils.setLogoTint(custom_toolbar_imgLogo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_adverts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgClose -> backToPage()
            }
        }
    }

    private fun backToPage(){
        navDirections = UserAdvertsFragmentDirections.actionUserAdvertsFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun observeLiveData(){
        userAdvertsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        userAdvertsViewModel.advertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                advertList = it

                if (it.get(0).Id != null){
                    user_adverts_fragment_recyclerView.visibility = View.VISIBLE
                    user_adverts_fragment_txtNoData.visibility = View.GONE

                    advertsAdapter.loadData(advertList)
                    advertsAdapter.setAdvertOnItemClickListener(object : AdvertsAdapter.AdvertOnItemClickListener{
                        override fun onItemClick(advert: Advert) {
                            //goToAdvertDetailsPage(advert, userId, categoryData, subCategoryList)
                        }
                    })
                } else {
                    user_adverts_fragment_txtNoData.visibility = View.VISIBLE
                    user_adverts_fragment_recyclerView.visibility = View.GONE

                    if (confirm == 1)
                        user_adverts_fragment_txtNoData.text = "Yayında olan bir ilan bulunamadı"
                    else
                        user_adverts_fragment_txtNoData.text = "Yayında olmayan bir ilan bulunamadı"
                }
            }
        })
    }
}