package com.nexis.obuluyor.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var profileViewModel: ProfileViewModel

    private var userData: User? = null
    private var countryList: List<Country>? = null
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = ProfileFragmentArgs.fromBundle(it).userId

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)

            if (userId != -1){
                profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
                observeLiveData()
                profileViewModel.getUserData(userId)
                profileViewModel.getCountryList()

                profile_fragment_relativeUpdateProfile.setOnClickListener(this)
                profile_fragment_relativeSignOut.setOnClickListener(this)
                profile_fragment_relativeFavoriteAdverts.setOnClickListener(this)
                profile_fragment_relativeAdvertMessages.setOnClickListener(this)
                profile_fragment_relativeActiveAdverts.setOnClickListener(this)
                profile_fragment_relativeDeActiveAdverts.setOnClickListener(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgClose -> backToMainPage(userId)
                R.id.profile_fragment_relativeUpdateProfile -> showUserProfileUpdateDialog(v.context, userData, countryList)
                R.id.profile_fragment_relativeSignOut -> Singleton.showSignOutDialog(v, userId)
                R.id.profile_fragment_relativeFavoriteAdverts -> goToFavoritesPage(userId)
                R.id.profile_fragment_relativeAdvertMessages -> goToMessagesPage(userId)
                R.id.profile_fragment_relativeStoreCreateOrEdit -> goToAddStorePage(userId, userData!!)
                R.id.profile_fragment_relativeActiveAdverts -> goToUserAdvertsPage(1, userId)
                R.id.profile_fragment_relativeDeActiveAdverts -> goToUserAdvertsPage(0, userId)
            }
        }
    }

    private fun goToUserAdvertsPage(confirm: Int, userId: Int){
        navDirections = ProfileFragmentDirections.actionProfileFragmentToUserAdvertsFragment(userId, confirm)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToAddStorePage(userId: Int, userData: User){
        if (userData.store?.Id == null)
            navDirections = ProfileFragmentDirections.actionProfileFragmentToAddStoreFragment(userId)
        else
            navDirections = ProfileFragmentDirections.actionProfileFragmentToUpdateStoreDataFragment(userId, userData.store)

        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToMessagesPage(userId: Int){
        navDirections = ProfileFragmentDirections.actionProfileFragmentToMessagesFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToFavoritesPage(userId: Int){
        navDirections = ProfileFragmentDirections.actionProfileFragmentToFavoritesFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun showUserProfileUpdateDialog(context: Context, userData: User?, countryList: List<Country>?){
        if (userData != null && countryList != null)
            Singleton.showUpdateUserProfileDialog(context, userData, countryList, profileViewModel, viewLifecycleOwner)
    }

    private fun observeLiveData(){
        profileViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        profileViewModel.userData.observe(viewLifecycleOwner, Observer {
            it?.let {
                userData = it
                profile_fragment_relativeStoreCreateOrEdit.setOnClickListener(this)
            }
        })

        profileViewModel.countryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                countryList = it
            }
        })

        profileViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
                Singleton.closeUpdateUserProfileDialog()
                userData = null
                profileViewModel.getUserData(userId)
            }
        })
    }

    private fun backToMainPage(userId: Int){
        navDirections = ProfileFragmentDirections.actionProfileFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}