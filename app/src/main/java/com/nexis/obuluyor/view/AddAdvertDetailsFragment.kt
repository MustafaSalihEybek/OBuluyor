package com.nexis.obuluyor.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.SubCategory
import kotlinx.android.synthetic.main.custom_advert_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class AddAdvertDetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections

    private lateinit var categoryData: Category
    private lateinit var subCategoryData: SubCategory
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = AddAdvertDetailsFragmentArgs.fromBundle(it).userId
            categoryData = AddAdvertDetailsFragmentArgs.fromBundle(it).categoryData
            subCategoryData = AddAdvertDetailsFragmentArgs.fromBundle(it).subCategoryData

            custom_advert_toolbar_txtTitle.text = "Temel Bilgileri Giriniz"
            custom_advert_toolbar_imgClose.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_advert_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_advert_toolbar_imgClose -> backToPage(userId, categoryData)
            }
        }
    }

    private fun backToPage(userId: Int, category: Category){
        navDirections = AddAdvertDetailsFragmentDirections.actionAddAdvertDetailsFragmentToAddAdvertSubCategoriesFragment(userId, category)
        Navigation.findNavController(v).navigate(navDirections)
    }
}