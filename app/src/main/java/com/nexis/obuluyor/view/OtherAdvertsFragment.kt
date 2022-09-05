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
import com.nexis.obuluyor.viewmodel.OtherAdvertsViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_other_adverts.*

class OtherAdvertsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var otherAdvertsViewModel: OtherAdvertsViewModel

    private lateinit var advertsAdapter: AdvertsAdapter
    private lateinit var advertType: String
    private lateinit var advertList: List<Advert>
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            advertType = OtherAdvertsFragmentArgs.fromBundle(it).advertType
            userId = OtherAdvertsFragmentArgs.fromBundle(it).userId

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)

            AppUtils.setLogoTint(custom_toolbar_imgLogo)

            other_adverts_fragment_recyclerView.setHasFixedSize(true)
            other_adverts_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            advertsAdapter = AdvertsAdapter(arrayListOf())
            other_adverts_fragment_recyclerView.adapter = advertsAdapter

            otherAdvertsViewModel = ViewModelProvider(this).get(OtherAdvertsViewModel::class.java)
            observeLiveData()

            if (advertType.equals("Urgent"))
                otherAdvertsViewModel.getOtherAdverts(1, 1, null, "1", null)
            else if (advertType.equals("DropsPrice"))
                otherAdvertsViewModel.getOtherAdverts(1, 1, "1", null, null)
            else
                otherAdvertsViewModel.getOtherAdverts(1, 1, null, null, "1")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_adverts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        otherAdvertsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        otherAdvertsViewModel.advertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                advertList = it

                if (it.get(0).Id != null){
                    other_adverts_fragment_recyclerView.visibility = View.VISIBLE
                    other_adverts_fragment_txtNoData.visibility = View.GONE

                    advertsAdapter.loadData(it)
                } else {
                    other_adverts_fragment_txtNoData.visibility = View.VISIBLE
                    other_adverts_fragment_recyclerView.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id){
                R.id.custom_toolbar_imgClose -> backToMainPage(userId)
            }
        }
    }

    private fun backToMainPage(userId: Int){
        navDirections = OtherAdvertsFragmentDirections.actionOtherAdvertsFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}