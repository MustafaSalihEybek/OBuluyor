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
import com.nexis.obuluyor.model.SubCategory
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AdvertsViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_advert_details.*
import kotlinx.android.synthetic.main.fragment_adverts.*

class AdvertsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var advertsViewModel: AdvertsViewModel

    private lateinit var subCategoryList: Array<SubCategory>
    private lateinit var advertList: List<Advert>
    private lateinit var advertsAdapter: AdvertsAdapter
    private lateinit var categoryData: Category
    private lateinit var editedCategoryList: ArrayList<Int>
    private lateinit var categoriesData: String
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = AdvertsFragmentArgs.fromBundle(it).userId
            categoryData = AdvertsFragmentArgs.fromBundle(it).categoryData
            subCategoryList = AdvertsFragmentArgs.fromBundle(it).subCategoryList

            custom_toolbar_imgPerson.visibility = View.VISIBLE
            custom_toolbar_imgClose.visibility = View.VISIBLE

            adverts_fragment_recyclerView.setHasFixedSize(true)
            adverts_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            advertsAdapter = AdvertsAdapter(arrayListOf())
            adverts_fragment_recyclerView.adapter = advertsAdapter

            editedCategoryList = ArrayList()

            for (cIn in 9 downTo 0){
                if (cIn > subCategoryList.size - 1){
                    if (cIn == subCategoryList.size)
                        editedCategoryList.add(categoryData.Id)
                    else
                        editedCategoryList.add(0)
                }
                else
                    editedCategoryList.add(subCategoryList.get(subCategoryList.size - (cIn + 1)).Id)
            }

            editedCategoryList.reverse()
            categoriesData = getCategoriesData(editedCategoryList)

            advertsViewModel = ViewModelProvider(this).get(AdvertsViewModel::class.java)
            observeLiveData()
            advertsViewModel.getAdverts(
                1,
                1,
                categoriesData
            )

            custom_toolbar_imgPerson.setOnClickListener(this)
            custom_toolbar_imgClose.setOnClickListener(this)

            AppUtils.setLogoTint(custom_toolbar_imgLogo)
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
                R.id.custom_toolbar_imgClose -> backToMainPage(userId, subCategoryList, categoryData)
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

                if (it.get(0).Id != null){
                    adverts_fragment_recyclerView.visibility = View.VISIBLE
                    adverts_fragment_txtNoData.visibility = View.GONE

                    advertsAdapter.loadData(advertList)
                    advertsAdapter.setAdvertOnItemClickListener(object : AdvertsAdapter.AdvertOnItemClickListener{
                        override fun onItemClick(advert: Advert) {
                            goToAdvertDetailsPage(advert, userId, categoryData, subCategoryList)
                        }
                    })
                } else {
                    adverts_fragment_txtNoData.visibility = View.VISIBLE
                    adverts_fragment_recyclerView.visibility = View.GONE
                }
            }
        })
    }

    private fun goToProfilePage(userId: Int){
        navDirections = AdvertsFragmentDirections.actionAdvertsFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun backToMainPage(userId: Int, subCategoryList: Array<SubCategory>, categoryData: Category){
        val subCategories: ArrayList<SubCategory> = ArrayList(subCategoryList.toMutableList())

        if (subCategories.size > 0)
            subCategories.removeAt(subCategoryList.size - 1)

        navDirections = AdvertsFragmentDirections.actionAdvertsFragmentToAdvertsSubCategoriesFragment(
            userId,
            categoryData,
            subCategories.toTypedArray()
        )
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToAdvertDetailsPage(advert: Advert, userId: Int, category: Category?, subCategoryList: Array<SubCategory>?){
        navDirections = AdvertsFragmentDirections.actionAdvertsFragmentToAdvertDetailsFragment(
            category,
            userId,
            advert,
            subCategoryList,
            false,
            null
        )
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun getCategoriesData(editedCategoryList: ArrayList<Int>) : String {
        var categoriesData: String = ""

        for (cIn in editedCategoryList.indices){
            if (editedCategoryList.get(cIn) == 0)
                break

            if (editedCategoryList.get(cIn + 1) == 0)
                categoriesData += "${editedCategoryList.get(cIn)}"
            else
                categoriesData += "${editedCategoryList.get(cIn)},"
        }

        return categoriesData
    }
}