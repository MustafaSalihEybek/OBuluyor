package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.AddAdvertImage
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.model.Module
import com.nexis.obuluyor.model.ModuleId
import com.nexis.obuluyor.repository.AddImageForAdvertRepository
import com.nexis.obuluyor.repository.AddNewAdvertRepository
import com.nexis.obuluyor.repository.GetModulesForCategoriesRepository
import com.nexis.obuluyor.repository.GetModulesRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field

class AddAdvertDetailsViewModel(application: Application) : BaseViewModel(application) {
    val advertData = MutableLiveData<Advert>()
    val errorMessage = MutableLiveData<String>()
    val addAdvertImage = MutableLiveData<AddAdvertImage>()

    fun addNewAdvert(
        userId: Int,
        userPhone: Int?,
        message: Int?,
        advertTitle: String?,
        advertContent: String?,
        advertType: Int?,
        advertPrice: Int?,
        advertExchange: String?,
        cargoPrice: Int?,
        cargoArrive: Int?,
        advertCargo: String?,
        advertCountry: Int,
        advertCity: Int,
        advertLocality: Int,
        advertLat: String?,
        advertLng: String?,
        advertZoom: String?,
        advertConfirm: Int?,
        advertDates: String?,
        advertCategories: String,
        advertStream: Int?,
        advertFinishDate: String?,
        advertPrice2: String?,
        advertPrice3: String?,
        advertFinishFullDate: String?,
        advertPay: Int?,
        companyName: String?,
        phone: String?,
        fax: String?,
        gsm: String?,
        web: String?,
        taxAdmin: String?,
        taxNo: String?,
        createDate: String?,
        businessType: String?,
        director: String?,
        endorsement: String?,
        employeesAmount: String?,
        about: String?,
        adres: String?,
        viewAmount: Int?,
        priceDown: String?,
        moduleContentIdData: String,
        moduleContentNameData: String,
        moduleIdData: String,
        moduleClassData: String,
        propIdData: String,
        propValData: String
    ){
        AppUtils.addNewAdvertRepository = AddNewAdvertRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.addNewAdvertRepository.addNewAdvert(
                userId,
                userPhone,
                message,
                advertTitle,
                advertContent,
                advertType,
                advertPrice,
                advertExchange,
                cargoPrice,
                cargoArrive,
                advertCargo,
                advertCountry,
                advertCity,
                advertLocality,
                advertLat,
                advertLng,
                advertZoom,
                advertConfirm,
                advertDates,
                advertCategories,
                advertStream,
                advertFinishDate,
                advertPrice2,
                advertPrice3,
                advertFinishFullDate,
                advertPay,
                companyName,
                phone,
                fax,
                gsm,
                web,
                taxAdmin,
                taxNo,
                createDate,
                businessType,
                director,
                endorsement,
                employeesAmount,
                about,
                adres,
                viewAmount,
                priceDown,
                moduleContentIdData,
                moduleContentNameData,
                moduleIdData,
                moduleClassData,
                propIdData,
                propValData
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Advert>(){
                    override fun onSuccess(t: Advert) {
                        advertData.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.message
                    }
                })
        )
    }

    fun addAdvertNewImage(advertId: RequestBody, advertImage: MultipartBody.Part){
        AppUtils.addImageForAdvertRepository = AddImageForAdvertRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.addImageForAdvertRepository.addImageForAdvert(advertId, advertImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AddAdvertImage>(){
                    override fun onSuccess(t: AddAdvertImage) {
                        addAdvertImage.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}