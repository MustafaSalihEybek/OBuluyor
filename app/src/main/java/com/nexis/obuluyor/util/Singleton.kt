package com.nexis.obuluyor.util

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.view.dialog.ExitTheAddAdvertDialog
import com.nexis.obuluyor.view.dialog.ExitTheAppDialog
import com.nexis.obuluyor.view.dialog.SignOutDialog
import com.nexis.obuluyor.view.dialog.UpdateUserProfileDialog
import com.nexis.obuluyor.viewmodel.ProfileViewModel

class Singleton {
    companion object{
        val BASE_URL: String = "https://obuluyor.com.tr/api/"
        val clientUsername: String = "mobil"
        val clientPassword: String = "mU!/-*+eSf30-54!_!Y"
        var mViewPager: ViewPager? = null

        lateinit var exitTheAppDialog: ExitTheAppDialog
        lateinit var exitTheAddAdvertDialog: ExitTheAddAdvertDialog
        lateinit var updateUserProfileDialog: UpdateUserProfileDialog
        lateinit var signOutDialog: SignOutDialog

        fun setPage(pIn: Int){
            mViewPager?.let {
                it.currentItem = pIn
            }
        }

        fun showExitTheAppDialog(mContext: Context){
            exitTheAppDialog = ExitTheAppDialog(mContext)
            exitTheAppDialog.setCancelable(false)
            exitTheAppDialog.show()
        }

        fun showExitTheAddAdvertDialog(v: View, userId: Int){
            exitTheAddAdvertDialog = ExitTheAddAdvertDialog(v, userId)
            exitTheAddAdvertDialog.setCancelable(false)
            exitTheAddAdvertDialog.show()
        }

        fun showUpdateUserProfileDialog(mContext: Context, userData: User, countryList: List<Country>, pV: ProfileViewModel, owner: LifecycleOwner){
            updateUserProfileDialog = UpdateUserProfileDialog(mContext, userData, countryList, pV, owner)
            updateUserProfileDialog.setCancelable(false)
            updateUserProfileDialog.show()
        }

        fun showSignOutDialog(v: View, userId: Int){
            signOutDialog = SignOutDialog(v, userId)
            signOutDialog.setCancelable(false)
            signOutDialog.show()
        }
    }
}