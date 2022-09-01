package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.repository.AdvertsBySearchResult
import com.nexis.obuluyor.repository.OtherAdvertsRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class OtherAdvertsViewModel(application: Application) : BaseViewModel(application) {
    val errorMessage = MutableLiveData<String>()
    val advertList = MutableLiveData<List<Advert>>()

    fun getOtherAdverts(confirm: Int, activeAdverts: Int, priceDrops: String?, urgent: String?, last48Hours: String?){
        AppUtils.otherAdvertsRepository = OtherAdvertsRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.otherAdvertsRepository.getOtherAdverts(confirm, activeAdverts, "1", null, null)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Advert>>(){
                    override fun onSuccess(t: List<Advert>) {
                        advertList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                        println("Hata: ${e.message}")
                    }
                })
        )

        /*AppUtils.advertsBySearchResult = AdvertsBySearchResult()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.advertsBySearchResult.getAdvertsBySearchResult(confirm, activeAdverts)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Advert>>(){
                    override fun onSuccess(t: List<Advert>) {
                        advertList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                        println("Hata: ${e.localizedMessage}")
                    }
                })
        )*/
    }
}