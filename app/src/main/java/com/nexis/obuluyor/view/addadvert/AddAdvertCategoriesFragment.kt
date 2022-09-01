package com.nexis.obuluyor.view.addadvert

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.nexis.obuluyor.adapter.AddAdvertCategoriesAdapter
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertCategoriesViewModel
import kotlinx.android.synthetic.main.custom_advert_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_advert_categories.*

class AddAdvertCategoriesFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var addAdvertCategoriesViewModel: AddAdvertCategoriesViewModel
    private lateinit var navDirections: NavDirections
    private var userId: Int = -1

    private lateinit var categoryList: List<Category>
    private lateinit var addAdvertCategoriesAdapter: AddAdvertCategoriesAdapter
    private lateinit var txtSearch: String
    private lateinit var searchedCategoryList: List<Category>
    private var isDataReceived: Boolean = false

    private fun init(){
        arguments?.let {
            userId = AddAdvertCategoriesFragmentArgs.fromBundle(it).userId

            advert_categories_fragment_recyclerView.setHasFixedSize(true)
            advert_categories_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            addAdvertCategoriesAdapter = AddAdvertCategoriesAdapter(arrayListOf())
            advert_categories_fragment_recyclerView.adapter = addAdvertCategoriesAdapter

            addAdvertCategoriesViewModel = ViewModelProvider(this).get(AddAdvertCategoriesViewModel::class.java)
            observeLiveData()
            addAdvertCategoriesViewModel.getCategories()

            custom_advert_toolbar_imgClose.setOnClickListener(this)
            custom_toolbar_imgLogo.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advert_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        advert_categories_fragment_editSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isDataReceived){
                    if (p0 != null){
                        if (p0.isNotEmpty()){
                            txtSearch = p0.toString()

                            searchedCategoryList = getFilteredData(categoryList, txtSearch)
                            fillDataFromAdapter(searchedCategoryList)
                        } else
                            fillDataFromAdapter(categoryList)
                    } else
                        fillDataFromAdapter(categoryList)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observeLiveData(){
        addAdvertCategoriesViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addAdvertCategoriesViewModel.subCategoryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                categoryList = it
                isDataReceived = true

                fillDataFromAdapter(categoryList)
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_advert_toolbar_imgClose -> Singleton.showExitTheAddAdvertDialog(v, userId)
                R.id.custom_toolbar_imgLogo -> Singleton.showExitTheAddAdvertDialog(v, userId)
            }
        }
    }

    private fun goToAddAdvertSubCategoriesFragmentPage(category: Category, userId: Int){
        navDirections = AddAdvertCategoriesFragmentDirections.actionAdvertCategoriesFragmentToAddAdvertSubCategoriesFragment(userId, category)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun fillDataFromAdapter(categoryList: List<Category>){
        addAdvertCategoriesAdapter.loadData(categoryList)
        addAdvertCategoriesAdapter.setAdvertCategoryOnItemClickListener(object : AddAdvertCategoriesAdapter.AdvertCategoryOnItemClickListener{
            override fun onItemClick(category: Category) {
                goToAddAdvertSubCategoriesFragmentPage(category, userId)
            }
        })
    }

    private fun getFilteredData(categoryList: List<Category>, searchText: String) : ArrayList<Category> {
        var categories: ArrayList<Category> = ArrayList()

        for (category in categoryList){
            category.kategori_adi?.let {
                if (it.lowercase().contains(searchText.lowercase()))
                    categories.add(category)
            }
        }

        return categories
    }
}