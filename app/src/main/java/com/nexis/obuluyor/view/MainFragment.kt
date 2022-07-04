package com.nexis.obuluyor.view

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.CategoriesAdapter
import com.nexis.obuluyor.adapter.NavCategoriesAdapter
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.util.SharedPreferences
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.custom_toolbar_mainToolbar

class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mToggle: ActionBarDrawerToggle

    private lateinit var categoryList: List<Category>
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var navCategoriesAdapter: NavCategoriesAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = MainFragmentArgs.fromBundle(it).userId
            sharedPreferences = SharedPreferences(v.context)

            if (userId < 1)
                userId = sharedPreferences.getSavedUserId()

            if (userId > 0){
                custom_toolbar_imgPerson.visibility = View.VISIBLE
                custom_toolbar_imgPerson.setOnClickListener(this)
            }

            main_fragment_recyclerView.setHasFixedSize(true)
            main_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            categoriesAdapter = CategoriesAdapter(arrayListOf())
            main_fragment_recyclerView.adapter = categoriesAdapter

            main_fragment_navigation_recyclerView.setHasFixedSize(true)
            main_fragment_navigation_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            navCategoriesAdapter = NavCategoriesAdapter(arrayListOf())
            main_fragment_navigation_recyclerView.adapter = navCategoriesAdapter

            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            observeLiveData()
            mainViewModel.getCategories()

            mToggle = ActionBarDrawerToggle(
                (v.context as Activity),
                main_fragment_drawerLayout,
                (custom_toolbar_mainToolbar as Toolbar),
                R.string.nav_open, R.string.nav_close
            )
            main_fragment_drawerLayout.addDrawerListener(mToggle)
            mToggle.syncState()

            main_fragment_imgAddAdvert.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        mainViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                main_fragment_editSearchValue.setText(it)
            }
        })

        mainViewModel.categoryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                categoryList = it

                categoriesAdapter.loadData(categoryList)
                categoriesAdapter.setCategoriesOnItemSelectedListener(object : CategoriesAdapter.CategoriesOnItemSelectedListener{
                    override fun onItemClick(category: Category) {
                        goToAdvertsPage(userId, category)
                    }
                })

                navCategoriesAdapter.loadData(categoryList)
                navCategoriesAdapter.setNavCategoriesOnItemSelectedListener(object : NavCategoriesAdapter.NavCategoriesItemSelectedListener{
                    override fun onItemClick(category: Category) {
                        goToAdvertsPage(userId, category)
                    }
                })
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.main_fragment_imgAddAdvert -> addNewAdvert(userId)
                R.id.custom_toolbar_imgPerson -> goToProfilePage(userId)
            }
        }
    }

    private fun addNewAdvert(userId: Int){
        if (userId == -1)
            navDirections = MainFragmentDirections.actionMainFragmentToSignFragment()
        else
            navDirections = MainFragmentDirections.actionMainFragmentToAdvertCategoriesFragment(userId)

        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToProfilePage(userId: Int){
        navDirections = MainFragmentDirections.actionMainFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToAdvertsPage(userId: Int, category: Category){
        navDirections = MainFragmentDirections.actionMainFragmentToAdvertsFragment(userId, category)
        Navigation.findNavController(v).navigate(navDirections)
    }
}