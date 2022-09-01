package com.nexis.obuluyor.util

import android.content.ClipData
import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.nexis.obuluyor.model.*
import com.nexis.obuluyor.view.dialog.*
import com.nexis.obuluyor.viewmodel.*

class Singleton {
    companion object{
        val BASE_URL: String = "https://obuluyor.com.tr/api/"
        val clientUsername: String = "mobil"
        val clientPassword: String = "mU!/-*+eSf30-54!_!Y"
        var mViewPager: ViewPager? = null
        val HSIZE: Int = 25
        val VSIZE: Int = 35
        var themeMode: String = ""

        //Page 2
        lateinit var moduleIdList: List<ModuleId>
        lateinit var moduleIdData: String
        lateinit var moduleList: List<Module>
        lateinit var moduleNameList: ArrayList<String>

        //Page 3
        var exchangeIn: Int = 0
        var advertTimeIn: Int = 0
        var clipData: ClipData? = null
        var advertTitle: String? = null
        var advertPrice: Int = 0
        var advertContent: String? = null
        lateinit var countryList: List<Country>
        lateinit var cityList: List<City>

        var categoryList: List<Category> = arrayListOf()
        var randomAdvertList: List<Advert> = arrayListOf()
        var storeList: List<Store> = arrayListOf()
        var mainIsCreated: Boolean = false

        lateinit var exitTheAppDialog: ExitTheAppDialog
        lateinit var exitTheAddAdvertDialog: ExitTheAddAdvertDialog
        var updateUserProfileDialog: UpdateUserProfileDialog? = null
        var loadingDialog: LoadingDialog? = null
        lateinit var signOutDialog: SignOutDialog
        lateinit var sendMessageDialog: SendMessageDialog
        lateinit var messageDetailsDialog: MessageDetailsDialog
        lateinit var addModuleContentDialog: AddModuleContentDialog
        lateinit var removeFavoriteDialog: RemoveFavoriteDialog
        lateinit var splashDialog: SplashDialog

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
            updateUserProfileDialog!!.setCancelable(false)
            updateUserProfileDialog!!.show()
        }

        fun closeUpdateUserProfileDialog(){
            updateUserProfileDialog?.let {
                if (it.isShowing)
                    it.dismiss()
            }
        }

        fun showSignOutDialog(v: View, userId: Int){
            signOutDialog = SignOutDialog(v, userId)
            signOutDialog.setCancelable(false)
            signOutDialog.show()
        }

        fun showSendMessageDialog(v: View, aDV: AdvertDetailsViewModel, userData: User, targetData: User, advertId: Int){
            sendMessageDialog = SendMessageDialog(v, aDV, userData, targetData, advertId)
            sendMessageDialog.setCancelable(false)
            sendMessageDialog.show()
        }

        fun showMessageDetailsDialog(mContext: Context, messageData: Message, senderData: User, userId: Int, srV: SenderAndReceiverMessagesViewModel, owner: LifecycleOwner){
            messageDetailsDialog = MessageDetailsDialog(mContext, messageData, senderData, userId, srV, owner)
            messageDetailsDialog.setCancelable(false)
            messageDetailsDialog.show()
        }

        fun showAddModuleContentDialog(mContext: Context, moduleData: Module, adV: AddAdvertDetailsPage1ViewModel, owner: LifecycleOwner){
            addModuleContentDialog = AddModuleContentDialog(mContext, moduleData, adV, owner)
            addModuleContentDialog.setCancelable(false)
            addModuleContentDialog.show()
        }

        fun showRemoveFavoriteDialog(mContext: Context, userId: Int, advertId: Int, favoriteId: Int, fV: FavoritesViewModel){
            removeFavoriteDialog = RemoveFavoriteDialog(mContext, userId, advertId, favoriteId, fV)
            removeFavoriteDialog.setCancelable(false)
            removeFavoriteDialog.show()
        }

        fun closeRemoveFavoriteDialog(){
            if (removeFavoriteDialog.isShowing)
                removeFavoriteDialog.dismiss()
        }

        fun showSplashDialog(mContext: Context){
            splashDialog = SplashDialog(mContext)
            splashDialog.setCancelable(false)
            splashDialog.show()
        }

        fun closeSplashDialog(){
            if (splashDialog.isShowing)
                splashDialog.dismiss()
        }

        fun showLoadingDialog(mContext: Context, message: String){
            loadingDialog = LoadingDialog(mContext, message)
            loadingDialog!!.setCancelable(false)
            loadingDialog!!.show()
        }

        fun closeLoadingDialog(){
            loadingDialog?.let {
                if (it.isShowing)
                    it.dismiss()
            }
        }
    }
}