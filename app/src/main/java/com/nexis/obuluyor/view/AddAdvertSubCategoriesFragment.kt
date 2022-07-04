package com.nexis.obuluyor.view

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
import com.nexis.obuluyor.databinding.FragmentAddAdvertSubCategoriesBinding
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.SubCategory
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertSubCategoriesViewModel
import kotlinx.android.synthetic.main.custom_advert_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_advert_sub_categories.*

class AddAdvertSubCategoriesFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var addAdvertSubCategoryBinding: FragmentAddAdvertSubCategoriesBinding
    private lateinit var addAdvertSubCategoriesViewModel: AddAdvertSubCategoriesViewModel

    private lateinit var categoryData: Category
    private lateinit var subCategoryList: List<SubCategory>
    private lateinit var subCategoriesAdapter: AddAdvertSubCategoriesAdapter
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = AddAdvertSubCategoriesFragmentArgs.fromBundle(it).userId
            categoryData = AddAdvertSubCategoriesFragmentArgs.fromBundle(it).categoryData
            addAdvertSubCategoryBinding.category = categoryData

            add_advert_sub_categories_fragment_recyclerView.setHasFixedSize(true)
            add_advert_sub_categories_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            subCategoriesAdapter = AddAdvertSubCategoriesAdapter(arrayListOf())
            add_advert_sub_categories_fragment_recyclerView.adapter = subCategoriesAdapter

            addAdvertSubCategoriesViewModel = ViewModelProvider(this).get(AddAdvertSubCategoriesViewModel::class.java)
            observeLiveData()
            addAdvertSubCategoriesViewModel.getSubCategories(categoryData.Id)

            custom_advert_toolbar_imgClose.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addAdvertSubCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_advert_sub_categories, container, false)
        return addAdvertSubCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        addAdvertSubCategoriesViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addAdvertSubCategoriesViewModel.subCategoryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                subCategoryList = it

                if (add_advert_sub_categories_fragment_recyclerView.itemDecorationCount > 0)
                    add_advert_sub_categories_fragment_recyclerView.removeItemDecorationAt(0)

                subCategoriesAdapter.loadData(it)
                subCategoriesAdapter.setAdvertSubCategoryOnItemClickListener(object : AddAdvertSubCategoriesAdapter.AdvertSubCategoryOnItemClickListener{
                    override fun onItemClick(subCategory: SubCategory) {
                        goToAdvertDetailsPage(userId, categoryData, subCategory)
                    }
                })
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_advert_toolbar_imgClose -> backToPage(userId)
            }
        }
    }

    private fun backToPage(userId: Int){
        navDirections = AddAdvertSubCategoriesFragmentDirections.actionAddAdvertSubCategoriesFragmentToAdvertCategoriesFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToAdvertDetailsPage(userId: Int, category: Category, subCategory: SubCategory){
        navDirections = AddAdvertSubCategoriesFragmentDirections.actionAddAdvertSubCategoriesFragmentToAddAdvertDetailsFragment(userId, category, subCategory)
        Navigation.findNavController(v).navigate(navDirections)
    }
}