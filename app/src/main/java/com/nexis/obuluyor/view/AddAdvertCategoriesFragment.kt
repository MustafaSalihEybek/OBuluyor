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
import com.nexis.obuluyor.adapter.AddAdvertCategoriesAdapter
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertCategoriesViewModel
import kotlinx.android.synthetic.main.custom_advert_toolbar.*
import kotlinx.android.synthetic.main.fragment_advert_categories.*

class AddAdvertCategoriesFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var addAdvertCategoriesViewModel: AddAdvertCategoriesViewModel
    private lateinit var navDirections: NavDirections
    private var userId: Int = -1

    private lateinit var categoryList: List<Category>
    private lateinit var addAdvertCategoriesAdapter: AddAdvertCategoriesAdapter

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
                addAdvertCategoriesAdapter.loadData(categoryList)

                addAdvertCategoriesAdapter.setAdvertCategoryOnItemClickListener(object : AddAdvertCategoriesAdapter.AdvertCategoryOnItemClickListener{
                    override fun onItemClick(category: Category) {
                        goToAddAdvertSubCategoriesFragmentPage(category, userId)
                    }
                })
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_advert_toolbar_imgClose -> Singleton.showExitTheAddAdvertDialog(v, userId)
            }
        }
    }

    private fun goToAddAdvertSubCategoriesFragmentPage(category: Category, userId: Int){
        navDirections = AddAdvertCategoriesFragmentDirections.actionAdvertCategoriesFragmentToAddAdvertSubCategoriesFragment(userId, category)
        Navigation.findNavController(v).navigate(navDirections)
    }
}