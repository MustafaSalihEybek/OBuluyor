package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.*
import com.nexis.obuluyor.repository.*
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AdvertDetailsViewModel(application: Application) : BaseViewModel(application) {
    val favoriteList = MutableLiveData<List<Favorite>>()
    val errorMessage = MutableLiveData<String>()
    val favoriteMessage = MutableLiveData<String>()
    val sendMessage = MutableLiveData<String>()
    val userData = MutableLiveData<User>()
    val propList = MutableLiveData<List<Prop>>()
    val propDataList = MutableLiveData<List<PropData>>()
    val groupTitleList = MutableLiveData<List<GroupTitle>>()

    fun getFavorites(userId: Int){
        AppUtils.favoritesRepository = FavoritesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.favoritesRepository.getFavorites(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Favorite>>(){
                    override fun onSuccess(t: List<Favorite>) {
                        favoriteList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun addFavorite(userId: Int, advertId: Int){
        AppUtils.addFavoriteRepository = AddFavoriteRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.addFavoriteRepository.addFavorite(userId, advertId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Result>(){
                    override fun onSuccess(t: Result) {
                        favoriteMessage.value = t.message
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun removeFavorite(userId: Int, advertId: Int, favoriteId: Int){
        AppUtils.removeFavoriteRepository = RemoveFavoriteRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.removeFavoriteRepository.removeFavorite(userId, advertId, favoriteId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Result>(){
                    override fun onSuccess(t: Result) {
                        favoriteMessage.value = t.message
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun sendMessage(userId: Int, targetId: Int, messageContent: String, advertId: Int){
        AppUtils.sendMessageRepository = SendMessageRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.sendMessageRepository.sendMessage(userId, targetId, messageContent, advertId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Result>(){
                    override fun onSuccess(t: Result) {
                        sendMessage.value = t.message
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getUserData(userId: Int){
        AppUtils.userDataRepository = UserDataRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.userDataRepository.getUserData(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Sign>(){
                    override fun onSuccess(t: Sign) {
                        if (t.result.message != null)
                            errorMessage.value = t.result.message
                        else
                            userData.value = t.user
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getProps(advertId: Int){
        AppUtils.getPropsRepository = GetPropsRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getPropsRepository.getProps(advertId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Prop>>(){
                    override fun onSuccess(t: List<Prop>) {
                        propList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getPropsData(propsIdData: String){
        AppUtils.getPropsAndModulesDataRepository = GetPropsAndModulesDataRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getPropsAndModulesDataRepository.getPropsData(propsIdData, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<PropData>>(){
                    override fun onSuccess(t: List<PropData>) {
                        propDataList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getGroupTitles(groupsIdData: String){
        AppUtils.getPropTitlesRepository = GetPropTitlesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getPropTitlesRepository.getPropTitles(groupsIdData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<GroupTitle>>(){
                    override fun onSuccess(t: List<GroupTitle>) {
                        groupTitleList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}