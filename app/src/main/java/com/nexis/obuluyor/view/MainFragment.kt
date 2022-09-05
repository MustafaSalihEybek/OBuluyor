package com.nexis.obuluyor.view

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.CategoriesAdapter
import com.nexis.obuluyor.adapter.NavCategoriesAdapter
import com.nexis.obuluyor.adapter.RandomAdvertsAdapter
import com.nexis.obuluyor.adapter.StoresAdapter
import com.nexis.obuluyor.adapter.decoration.GridManagerDecoration
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.Store
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.SharedPreferences
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.custom_nav_header.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.custom_toolbar_mainToolbar

class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mToggle: ActionBarDrawerToggle

    private lateinit var categoryList: List<Category>
    private lateinit var storeList: List<Store>
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var navCategoriesAdapter: NavCategoriesAdapter
    private lateinit var storesAdapter: StoresAdapter
    private lateinit var randomAdvertList: List<Advert>
    private lateinit var randomAdvertsAdapter: RandomAdvertsAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1

    private var txtSearch: String = ""

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

            AppUtils.setLogoTint(custom_toolbar_imgLogo)

            main_fragment_recyclerView.setHasFixedSize(true)
            main_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            categoriesAdapter = CategoriesAdapter(arrayListOf())
            main_fragment_recyclerView.adapter = categoriesAdapter

            main_fragment_recyclerViewStore.setHasFixedSize(true)
            main_fragment_recyclerViewStore.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.HORIZONTAL, false)
            storesAdapter = StoresAdapter(arrayListOf())
            main_fragment_recyclerViewStore.adapter = storesAdapter

            main_fragment_navigation_recyclerView.setHasFixedSize(true)
            main_fragment_navigation_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            navCategoriesAdapter = NavCategoriesAdapter(arrayListOf())
            main_fragment_navigation_recyclerView.adapter = navCategoriesAdapter

            main_fragment_recyclerViewOtherAdverts.setHasFixedSize(true)
            main_fragment_recyclerViewOtherAdverts.layoutManager = GridLayoutManager(v.context, 2)
            randomAdvertsAdapter = RandomAdvertsAdapter(arrayListOf(), v, userId)
            main_fragment_recyclerViewOtherAdverts.adapter = randomAdvertsAdapter

            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            observeLiveData()

            if (!Singleton.mainIsCreated){
                Singleton.showSplashDialog(v.context)
                mainViewModel.getCategories()
            }

            mToggle = ActionBarDrawerToggle(
                (v.context as Activity),
                main_fragment_drawerLayout,
                (custom_toolbar_mainToolbar as Toolbar),
                R.string.nav_open, R.string.nav_close
            )
            mToggle.drawerArrowDrawable.color = ContextCompat.getColor(v.context, android.R.color.white)
            main_fragment_drawerLayout.addDrawerListener(mToggle)
            mToggle.syncState()

            main_fragment_imgAddAdvert.setOnClickListener(this)
            custom_nav_header_relativeAddAdvert.setOnClickListener(this)
            main_fragment_imgSearch.setOnClickListener(this)
            main_fragment_relativeUrgent.setOnClickListener(this)
            main_fragment_relativeDropsPrice.setOnClickListener(this)
            main_fragment_relative48Hour.setOnClickListener(this)
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

        main_fragment_editSearchValue.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    if (it.isNotEmpty())
                        txtSearch = it.toString()
                    else
                        txtSearch = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        if (Singleton.mainIsCreated){
            categoryList = Singleton.categoryList
            randomAdvertList = Singleton.randomAdvertList
            storeList = Singleton.storeList

            setCategoryList(categoryList)
            setRandomAdvertList(randomAdvertList)
            setStoreList(storeList)
        }
    }

    private fun observeLiveData(){
        mainViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        mainViewModel.randomAdvertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                randomAdvertList = it
                Singleton.randomAdvertList = it

                setRandomAdvertList(it)
            }
        })

        mainViewModel.categoryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                categoryList = it
                Singleton.categoryList = it

                setCategoryList(it)
            }
        })

        mainViewModel.storeList.observe(viewLifecycleOwner, Observer {
            it?.let {
                storeList = it
                Singleton.storeList = it

                setStoreList(it)
            }
        })

        mainViewModel.advertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                goToPageBySearchResult(txtSearch, it)
            }
        })
    }

    private fun setCategoryList(categoryList: List<Category>){
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

        if (!Singleton.mainIsCreated)
            mainViewModel.getRandomTenAdvert(1, 1, 1, 10)
    }

    private fun setRandomAdvertList(randomAdvertList: List<Advert>){
        if (randomAdvertList.isNotEmpty()){
            if (randomAdvertList.get(0).title != null){
                if (main_fragment_recyclerViewOtherAdverts.itemDecorationCount > 0)
                    main_fragment_recyclerViewOtherAdverts.removeItemDecorationAt(0)

                main_fragment_recyclerViewOtherAdverts.addItemDecoration(GridManagerDecoration(Singleton.VSIZE, Singleton.HSIZE, randomAdvertList.size))
                randomAdvertsAdapter.loadData(randomAdvertList)
            }
        }

        if (!Singleton.mainIsCreated)
            mainViewModel.getStoreList()
    }

    private fun setStoreList(storeList: List<Store>){
        storesAdapter.loadData(storeList)
        storesAdapter.setStoreOnItemClickListener(object : StoresAdapter.StoreItemClickListener{
            override fun onItemClick(storeData: Store) {
                navDirections = MainFragmentDirections.actionMainFragmentToUserStoreFragment(userId, storeData)
                Navigation.findNavController(v).navigate(navDirections)
            }
        })

        if (!Singleton.mainIsCreated){
            Singleton.closeSplashDialog()
            Singleton.mainIsCreated = true
        }
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.main_fragment_relativeUrgent -> goToOtherAdvertsPage("Urgent", userId)
                R.id.main_fragment_relativeDropsPrice -> goToOtherAdvertsPage("DropsPrice", userId)
                R.id.main_fragment_relative48Hour -> goToOtherAdvertsPage("Last48Hours", userId)
                R.id.main_fragment_imgAddAdvert -> addNewAdvert(userId)
                R.id.custom_nav_header_relativeAddAdvert -> addNewAdvert(userId)
                R.id.custom_toolbar_imgPerson -> goToProfilePage(userId)
                R.id.main_fragment_imgSearch -> {
                    if (txtSearch.isNotEmpty())
                        mainViewModel.getAdvertsBySearchResult(1, 1)
                }
            }
        }
    }

    private fun goToPageBySearchResult(searchValue: String, advertList: List<Advert>){
        val searchByIdResult = checkAdvertIdForSearchValue(searchValue, advertList)
        val searchByNameResult = checkAdvertNameForSearchValue(searchValue, advertList)

        if (searchByIdResult.first){
            navDirections = MainFragmentDirections.actionMainFragmentToAdvertDetailsFragment(
                null,
                userId,
                searchByIdResult.second!!,
                null,
                false,
                null
            )
        } else if (searchByNameResult.first){
            navDirections = MainFragmentDirections.actionMainFragmentToAdvertsBySearchResultFragment(searchByNameResult.second.toTypedArray(), userId)
        }


        if (searchByIdResult.first || searchByNameResult.first)
            Navigation.findNavController(v).navigate(navDirections)
        else
            "message".show(v, "Bu arama sonucuna ait veri bulunamadı")
    }

    private fun goToOtherAdvertsPage(advertType: String, userId: Int){
        navDirections = MainFragmentDirections.actionMainFragmentToOtherAdvertsFragment(advertType, userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun checkAdvertIdForSearchValue(searchValue: String, advertList: List<Advert>) : Pair<Boolean, Advert?> {
        for (advert in advertList){
            if (advert.Id != null){
                if (advert.Id.toString().equals(searchValue))
                    return Pair(true, advert)
            }
        }

        return Pair(false, null)
    }

    private fun checkAdvertNameForSearchValue(searchValue: String, advertList: List<Advert>) : Pair<Boolean, ArrayList<Advert>> {
        val adverts: ArrayList<Advert> = ArrayList()

        for (advert in advertList){
            if (advert.title != null){
                if (advert.title.uppercase().contains(searchValue.uppercase()))
                    adverts.add(advert)
            }
        }

        return Pair(adverts.size > 0, adverts)
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
        navDirections = MainFragmentDirections.actionMainFragmentToAdvertsSubCategoriesFragment(userId, category, arrayOf())
        Navigation.findNavController(v).navigate(navDirections)
    }
}