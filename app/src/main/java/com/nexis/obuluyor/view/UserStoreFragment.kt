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
import androidx.recyclerview.widget.GridLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.GridAdvertsAdapter
import com.nexis.obuluyor.adapter.decoration.GridManagerDecoration
import com.nexis.obuluyor.databinding.FragmentUserStoreBinding
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.model.Store
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.UserStoreViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_user_store.*

class UserStoreFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var userStoreBinding: FragmentUserStoreBinding
    private lateinit var userStoreViewModel: UserStoreViewModel

    private lateinit var gridAdvertsAdapter: GridAdvertsAdapter
    private lateinit var advertList: List<Advert>
    private lateinit var storeData: Store
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = UserStoreFragmentArgs.fromBundle(it).userId
            storeData = UserStoreFragmentArgs.fromBundle(it).storeData

            userStoreBinding.store = storeData

            user_store_fragment_recyclerView.setHasFixedSize(true)
            user_store_fragment_recyclerView.layoutManager = GridLayoutManager(v.context, 2)
            gridAdvertsAdapter = GridAdvertsAdapter(arrayListOf(), userId, v)
            user_store_fragment_recyclerView.adapter = gridAdvertsAdapter

            userStoreViewModel = ViewModelProvider(this).get(UserStoreViewModel::class.java)
            observeLiveData()
            userStoreViewModel.getAdvertsByUser(1, 1, userId)

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userStoreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_store, container, false)
        return userStoreBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        userStoreViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        userStoreViewModel.advertList.observe(viewLifecycleOwner, Observer {
            it?.let {
                advertList = it

                if (it.get(0).Id != null){
                    user_store_fragment_recyclerView.visibility = View.VISIBLE
                    user_store_fragment_txtNoData.visibility = View.GONE

                    if (user_store_fragment_recyclerView.itemDecorationCount > 0)
                        user_store_fragment_recyclerView.removeItemDecorationAt(0)

                    user_store_fragment_recyclerView.addItemDecoration(GridManagerDecoration(Singleton.VSIZE, Singleton.HSIZE, it.size))
                    gridAdvertsAdapter.loadData(it)
                } else {
                    user_store_fragment_txtNoData.visibility = View.VISIBLE
                    user_store_fragment_recyclerView.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgClose -> backToMainPage(userId)
            }
        }
    }

    private fun backToMainPage(userId: Int){
        navDirections = UserStoreFragmentDirections.actionUserStoreFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}