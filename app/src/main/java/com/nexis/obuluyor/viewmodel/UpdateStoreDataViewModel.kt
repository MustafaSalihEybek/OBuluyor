package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.UpdateStore
import com.nexis.obuluyor.repository.UpdateStoreDataRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateStoreDataViewModel(application: Application) : BaseViewModel(application) {
    val successMessage = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()

    fun updateUserStoreData(
        userId: RequestBody,
        storeName: RequestBody,
        storeDescription: RequestBody,
        imageLogo: MultipartBody.Part?,
        storeSlider: MultipartBody.Part?
    ){
        AppUtils.updateStoreDataRepository = UpdateStoreDataRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.updateStoreDataRepository.updateStoreData(
                userId,
                storeName,
                storeDescription,
                imageLogo,
                storeSlider
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateStore>(){
                    override fun onSuccess(t: UpdateStore) {
                        successMessage.value = t.result?.message
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}