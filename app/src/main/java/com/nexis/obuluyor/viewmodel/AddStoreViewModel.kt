package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.AddStore
import com.nexis.obuluyor.model.Store
import com.nexis.obuluyor.model.StoreInfo
import com.nexis.obuluyor.model.StorePaymentInfo
import com.nexis.obuluyor.repository.AddStoreRepository
import com.nexis.obuluyor.repository.StorePaymentInfoRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.util.*

class AddStoreViewModel(application: Application) : BaseViewModel(application) {
    val successMessage = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val storeData = MutableLiveData<Store>()
    val storePaymentInfo = MutableLiveData<StoreInfo>()

    fun addStore(
        userId: RequestBody,
        storeName: RequestBody,
        storeAddress: RequestBody,
        storeDescription: RequestBody,
        categoryId: RequestBody,
        time: RequestBody,
        timeEnd: RequestBody,
        paymentType: RequestBody,
        price: RequestBody,
        imageLogo: MultipartBody.Part,
        imageSlider: MultipartBody.Part,
        storeQuota: RequestBody
    ){
        AppUtils.addStoreRepository = AddStoreRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.addStoreRepository.addStore(
                userId,
                storeName,
                storeAddress,
                storeDescription,
                categoryId,
                time,
                timeEnd,
                paymentType,
                price,
                imageLogo,
                imageSlider,
                storeQuota
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AddStore>(){
                    override fun onSuccess(t: AddStore) {
                        if (t.result.message != null)
                            errorMessage.value = t.result.message
                        else
                            successMessage.value = "Mağaza başarıyla eklendi"
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.message
                    }
                })
        )
    }

    fun getStorePaymentInfo(){
        AppUtils.storePaymentInfoRepository = StorePaymentInfoRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.storePaymentInfoRepository.getStorePaymentInfo()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StorePaymentInfo>(){
                    override fun onSuccess(t: StorePaymentInfo) {
                        if (t.result?.message != null)
                            errorMessage.value = t.result.message
                        else
                            storePaymentInfo.value = t.storeInfo
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}