package com.nexis.obuluyor.view.addadvert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AddAdvertSubCategoriesAdapter
import com.nexis.obuluyor.databinding.FragmentAddAdvertSelectSubCategoriesBinding
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.SubCategory
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertSelectSubCategoriesViewModel
import kotlinx.android.synthetic.main.custom_advert_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_advert_select_sub_categories.*

class AddAdvertSelectSubCategoriesFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var selectSubBinding: FragmentAddAdvertSelectSubCategoriesBinding
    private lateinit var addAdvertSelectSubCategoriesViewModel: AddAdvertSelectSubCategoriesViewModel
    private lateinit var navDirections: NavDirections

    private var userId: Int = -1
    private lateinit var categoryData: Category
    private lateinit var selectedSubCategoryList: Array<SubCategory>
    private lateinit var subCategoryName: String
    private lateinit var subCategoryList: List<SubCategory>
    private lateinit var subCategoriesAdapter: AddAdvertSubCategoriesAdapter
    private lateinit var selectedSubCategory: SubCategory
    private var subListCheck: Boolean = false

    private fun init(){
        arguments?.let {
            userId = AddAdvertSelectSubCategoriesFragmentArgs.fromBundle(it).userId
            categoryData = AddAdvertSelectSubCategoriesFragmentArgs.fromBundle(it).categoryData
            selectedSubCategoryList = AddAdvertSelectSubCategoriesFragmentArgs.fromBundle(it).subCategoryList

            subCategoryName = "KATEGORİLER > ${categoryData.kategori_adi}"

            for (sub in selectedSubCategoryList)
                subCategoryName += " > ${sub.kategori_adi}"

            add_advert_select_sub_categories_fragment_txtTitle1.text = subCategoryName

            add_advert_select_sub_categories_fragment_recyclerView.setHasFixedSize(true)
            add_advert_select_sub_categories_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            subCategoriesAdapter = AddAdvertSubCategoriesAdapter(arrayListOf())
            add_advert_select_sub_categories_fragment_recyclerView.adapter = subCategoriesAdapter

            addAdvertSelectSubCategoriesViewModel = ViewModelProvider(this).get(AddAdvertSelectSubCategoriesViewModel::class.java)
            observeLiveData()

            if (selectedSubCategoryList.size > 0)
                addAdvertSelectSubCategoriesViewModel.getSubCategories(selectedSubCategoryList.get(selectedSubCategoryList.size - 1).Id)
            else
                addAdvertSelectSubCategoriesViewModel.getSubCategories(selectedSubCategoryList.get(0).Id)

            custom_advert_toolbar_imgClose.setOnClickListener(this)
            custom_toolbar_imgLogo.setOnClickListener(this)

            AppUtils.setLogoTint(custom_toolbar_imgLogo)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        selectSubBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_advert_select_sub_categories, container, false)
        return selectSubBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        addAdvertSelectSubCategoriesViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addAdvertSelectSubCategoriesViewModel.subCategoryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!subListCheck) {
                    subCategoryList = it
                    subCategoriesAdapter.loadData(subCategoryList)

                    subCategoriesAdapter.setAdvertSubCategoryOnItemClickListener(object : AddAdvertSubCategoriesAdapter.AdvertSubCategoryOnItemClickListener{
                        override fun onItemClick(subCategory: SubCategory) {
                            subListCheck = true
                            selectedSubCategory = subCategory
                            addAdvertSelectSubCategoriesViewModel.getSubCategories(subCategory.Id)
                        }
                    })
                } else {
                    val subCategoryMutableList = selectedSubCategoryList.toMutableList()
                    subCategoryMutableList.add(selectedSubCategory)

                    if (it.isNotEmpty()) {
                        navDirections = AddAdvertSelectSubCategoriesFragmentDirections.actionAddAdvertSelectSubCategoriesFragmentSelf(
                            userId,
                            subCategoryMutableList.toTypedArray(),
                            categoryData
                        )
                    }
                    else {
                        navDirections = AddAdvertSelectSubCategoriesFragmentDirections.actionAddAdvertSelectSubCategoriesFragmentToAddAdvertDetailsFragment(
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

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_advert_toolbar_imgClose -> backToPage(userId, selectedSubCategoryList, categoryData)
                R.id.custom_toolbar_imgLogo -> backToMainPage(userId)
            }
        }
    }

    private fun backToMainPage(userId: Int){
        navDirections = AddAdvertSelectSubCategoriesFragmentDirections.actionAddAdvertSelectSubCategoriesFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun backToPage(userId: Int, subCategoryList: Array<SubCategory>, category: Category){
        val subCategories: ArrayList<SubCategory> = ArrayList(subCategoryList.toMutableList())

        if (subCategoryList.size == 1)
            navDirections = AddAdvertSelectSubCategoriesFragmentDirections.actionAddAdvertSelectSubCategoriesFragmentToAddAdvertSubCategoriesFragment(
                userId,
                category
            )
        else {
            subCategories.removeAt(subCategoryList.size - 1)
            navDirections = AddAdvertSelectSubCategoriesFragmentDirections.actionAddAdvertSelectSubCategoriesFragmentSelf(
                userId,
                subCategories.toTypedArray(),
                category
            )
        }

        Navigation.findNavController(v).navigate(navDirections)
    }
}