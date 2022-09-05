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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.adapter.GridAdvertsAdapter
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AdvertSubCategoriesAdapter
import com.nexis.obuluyor.adapter.decoration.GridManagerDecoration
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.SubCategory
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AdvertsSubCategoriesViewModel
import kotlinx.android.synthetic.main.custom_advert_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_adverts_sub_categories.*

class AdvertsSubCategoriesFragment() : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var advertsSubCategoriesViewModel: AdvertsSubCategoriesViewModel
    private lateinit var navDirections: NavDirections

    private lateinit var selectedSubCategoryList: Array<SubCategory>
    private lateinit var categoryData: Category
    private var userId: Int = -1

    private lateinit var subCategoryList: List<SubCategory>
    private lateinit var advertSubCategoriesAdapter: AdvertSubCategoriesAdapter
    private lateinit var selectedSubCategory: SubCategory
    private lateinit var subCategoryName: String
    private var subCategorySelected: Boolean = false

    private var unCategoryName: String = "Kategori Yok"

    private lateinit var editedCategoryList: ArrayList<Int>
    private lateinit var advertsAdapter: GridAdvertsAdapter
    private lateinit var categoriesData: String

    private fun init(){
        arguments?.let {
            userId = AdvertsSubCategoriesFragmentArgs.fromBundle(it).userId
            categoryData = AdvertsSubCategoriesFragmentArgs.fromBundle(it).categoryData
            selectedSubCategoryList = AdvertsSubCategoriesFragmentArgs.fromBundle(it).subCategoryList

            subCategoryName = "KATEGORİLER > ${categoryData.kategori_adi}"

            for (sub in selectedSubCategoryList)
                subCategoryName += " > ${sub.kategori_adi}"

            adverts_sub_categories_fragment_txtTitle1.text = subCategoryName
            custom_advert_toolbar_txtTitle.text = "Kategori Seç"

            adverts_sub_categories_fragment_recyclerView.setHasFixedSize(true)
            adverts_sub_categories_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            advertSubCategoriesAdapter = AdvertSubCategoriesAdapter(arrayListOf())
            adverts_sub_categories_fragment_recyclerView.adapter = advertSubCategoriesAdapter

            advertsSubCategoriesViewModel = ViewModelProvider(this).get(AdvertsSubCategoriesViewModel::class.java)
            observeLiveData()

            if (selectedSubCategoryList.isEmpty())
                advertsSubCategoriesViewModel.getSubCategories(categoryData.Id)
            else
                advertsSubCategoriesViewModel.getSubCategories(selectedSubCategoryList.get(selectedSubCategoryList.size - 1).Id)

            if (selectedSubCategoryList.isNotEmpty()){
                editedCategoryList = ArrayList()

                for (cIn in 9 downTo 0){
                    if (cIn > selectedSubCategoryList.size - 1){
                        if (cIn == selectedSubCategoryList.size)
                            editedCategoryList.add(categoryData.Id)
                        else
                            editedCategoryList.add(0)
                    }
                    else
                        editedCategoryList.add(selectedSubCategoryList.get(selectedSubCategoryList.size - (cIn + 1)).Id)
                }

                editedCategoryList.reverse()
                categoriesData = getCategoriesData(editedCategoryList)

                loadAdvertsByCategories()
            } else {
                categoriesData = categoryData.Id.toString()
                loadAdvertsByCategories()
            }

            custom_advert_toolbar_imgClose.setOnClickListener(this)
            custom_toolbar_imgLogo.setOnClickListener(this)

            AppUtils.setLogoTint(custom_toolbar_imgLogo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adverts_sub_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_advert_toolbar_imgClose -> backToPage(userId, selectedSubCategoryList, categoryData)
                R.id.custom_toolbar_imgLogo -> backToMainPage(userId)
            }
        }
    }

    private fun backToPage(userId: Int, subCategoryList: Array<SubCategory>, category: Category){
        val subCategories: ArrayList<SubCategory> = ArrayList(subCategoryList.toMutableList())

        if (subCategoryList.isEmpty())
            navDirections = AdvertsSubCategoriesFragmentDirections.actionAdvertsSubCategoriesFragmentToMainFragment(
                userId
            )
        else {
            subCategories.removeAt(subCategoryList.size - 1)
            navDirections = AdvertsSubCategoriesFragmentDirections.actionAdvertsSubCategoriesFragmentSelf(
                userId,
                category,
                subCategories.toTypedArray(),
            )
        }

        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun backToMainPage(userId: Int){
        navDirections = AdvertsSubCategoriesFragmentDirections.actionAdvertsSubCategoriesFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun observeLiveData(){
        advertsSubCategoriesViewModel.advertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.get(0).Id != null){
                    adverts_sub_categories_fragment_recyclerViewAdverts.visibility = View.VISIBLE
                    adverts_sub_categories_fragment_txtNoData.visibility = View.GONE

                    if (adverts_sub_categories_fragment_recyclerViewAdverts.itemDecorationCount > 0)
                        adverts_sub_categories_fragment_recyclerViewAdverts.removeItemDecorationAt(0)

                    adverts_sub_categories_fragment_recyclerViewAdverts.addItemDecoration(GridManagerDecoration(Singleton.VSIZE, Singleton.HSIZE, it.size))
                    advertsAdapter.loadData(it)
                } else {
                    adverts_sub_categories_fragment_txtNoData.visibility = View.VISIBLE
                    adverts_sub_categories_fragment_recyclerViewAdverts.visibility = View.GONE
                }
            }
        })

        advertsSubCategoriesViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        advertsSubCategoriesViewModel.subCategoryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!subCategorySelected){
                    subCategoryList = it
                    advertSubCategoriesAdapter.loadData(subCategoryList)

                    advertSubCategoriesAdapter.setAdvertsSubCategoriesOnItemClickListener(object : AdvertSubCategoriesAdapter.AdvertsSubCategoriesOnItemClickListener{
                        override fun onItemClick(subCategory: SubCategory) {
                            subCategorySelected = true
                            selectedSubCategory = subCategory
                            advertsSubCategoriesViewModel.getSubCategories(subCategory.Id)
                        }
                    })
                } else {
                    val subCategoryMutableList = selectedSubCategoryList.toMutableList()

                    if (!selectedSubCategory.kategori_adi.equals(unCategoryName))
                        subCategoryMutableList.add(selectedSubCategory)

                    if (it.isNotEmpty()){
                        navDirections = AdvertsSubCategoriesFragmentDirections.actionAdvertsSubCategoriesFragmentSelf(
                            userId,
                            categoryData,
                            subCategoryMutableList.toTypedArray()
                        )
                    } else {
                        navDirections = AdvertsSubCategoriesFragmentDirections.actionAdvertsSubCategoriesFragmentToAdvertsFragment(
                            userId,
                            categoryData,
                            subCategoryMutableList.toTypedArray()
                        )
                    }

                    Navigation.findNavController(v).navigate(navDirections)
                }
            }
        })
    }

    private fun loadAdvertsByCategories(){
        advertsSubCategoriesViewModel.getAdverts(
            1,
            1,
            categoriesData,
        )

        adverts_sub_categories_fragment_recyclerViewAdverts.setHasFixedSize(true)
        adverts_sub_categories_fragment_recyclerViewAdverts.layoutManager = GridLayoutManager(v.context, 2)
        advertsAdapter = GridAdvertsAdapter(arrayListOf(), userId, v)
        adverts_sub_categories_fragment_recyclerViewAdverts.adapter = advertsAdapter
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