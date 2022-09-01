package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.repository.AdvertsRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AdvertsViewModel(application: Application) : BaseViewModel(application) {
    val advertList = MutableLiveData<List<Advert>>()
    val errorMessage = MutableLiveData<String>()

    fun getAdverts(
        confirm: Int,
        activeAdverts: Int,
        categoriesData: String
    ){
        AppUtils.advertsRepository = AdvertsRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.advertsRepository.getAdverts(
                confirm,
                activeAdverts,
                categoriesData,
                0,
                0
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Advert>>(){
                    override fun onSuccess(t: List<Advert>) {
                        advertList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}