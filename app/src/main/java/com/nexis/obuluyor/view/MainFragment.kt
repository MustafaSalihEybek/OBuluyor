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
import com.nexis.obuluyor.adapter.CategoriesAdapter
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var mainViewModel: MainViewModel

    private lateinit var categoryList: List<Category>
    private lateinit var categoriesAdapter: CategoriesAdapter

    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = MainFragmentArgs.fromBundle(it).userId

            main_fragment_recyclerView.setHasFixedSize(true)
            main_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            categoriesAdapter = CategoriesAdapter(arrayListOf(), v.context)
            main_fragment_recyclerView.adapter = categoriesAdapter

            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            observeLiveData()
            mainViewModel.getCategories()

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
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.main_fragment_imgAddAdvert -> addNewAdvert(userId)
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
}