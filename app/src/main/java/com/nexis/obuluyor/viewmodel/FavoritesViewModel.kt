package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Favorite
import com.nexis.obuluyor.model.Result
import com.nexis.obuluyor.repository.FavoritesRepository
import com.nexis.obuluyor.repository.RemoveFavoriteRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FavoritesViewModel(application: Application) : BaseViewModel(application) {
    val favoriteList = MutableLiveData<List<Favorite>>()
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()

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

    fun removeFavorite(userId: Int, advertId: Int, favoriteId: Int){
        AppUtils.removeFavoriteRepository = RemoveFavoriteRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.removeFavoriteRepository.removeFavorite(userId, advertId, favoriteId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Result>(){
                    override fun onSuccess(t: Result) {
                        successMessage.value = t.message
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}