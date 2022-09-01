package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.Store
import com.nexis.obuluyor.repository.*
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

class MainViewModel(application: Application) : BaseViewModel(application) {
    val errorMessage = MutableLiveData<String>()
    val categoryList = MutableLiveData<List<Category>>()
    val storeList = MutableLiveData<List<Store>>()
    val advertList = MutableLiveData<List<Advert>>()
    val randomAdvertList = MutableLiveData<List<Advert>>()

    fun getCategories(){
        AppUtils.categoriesRepository = CategoriesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.categoriesRepository.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Category>>(){
                    override fun onSuccess(t: List<Category>) {
                        categoryList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                        println("HataCategory: ${e.localizedMessage}")
                    }
                })
        )
    }

    fun getStoreList() {
        AppUtils.storeListRepository = StoreListRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.storeListRepository.getStoreList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Store>>(){
                    override fun onSuccess(t: List<Store>) {
                        storeList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                        println("HataStore: ${e.localizedMessage}")
                    }
                })
        )
    }

    fun getAdvertsBySearchResult(confirm: Int, activeAdverts: Int){
        AppUtils.advertsBySearchResult = AdvertsBySearchResult()
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
        )
    }

    fun getRandomTenAdvert(confirm: Int, activeAdverts: Int, random: Int, randomLimit: Int){
        AppUtils.advertsRepository = AdvertsRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.advertsRepository.getAdverts(
                confirm,
                activeAdverts,
                null,
                random,
                randomLimit
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Advert>>(){
                    override fun onSuccess(t: List<Advert>) {
                        randomAdvertList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.message
                        println("HataRandomAdvert: ${e.localizedMessage}")
                    }
                })
        )
    }
}