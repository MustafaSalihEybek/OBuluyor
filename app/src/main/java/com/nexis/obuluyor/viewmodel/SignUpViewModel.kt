package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.Sign
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.repository.CitiesRepository
import com.nexis.obuluyor.repository.CountriesRepository
import com.nexis.obuluyor.repository.SignUpRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SignUpViewModel(application: Application) : BaseViewModel(application) {
    val countryList = MutableLiveData<List<Country>>()
    val cityList = MutableLiveData<List<City>>()
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()

    fun getCountries(){
        AppUtils.disposable = CompositeDisposable()
        AppUtils.countriesRepository = CountriesRepository()

        AppUtils.disposable.add(
            AppUtils.countriesRepository.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        countryList.value = t
                        println("Size = ${t.size}")
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getCities(countryId: Int){
        AppUtils.disposable = CompositeDisposable()
        AppUtils.citiesRepository = CitiesRepository()

        AppUtils.disposable.add(
            AppUtils.citiesRepository.getCities(countryId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<City>>(){
                    override fun onSuccess(t: List<City>) {
                        cityList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun signUpUser(userData: User){
        AppUtils.disposable = CompositeDisposable()
        AppUtils.signUpRepository = SignUpRepository()

        AppUtils.disposable.add(
            AppUtils.signUpRepository.signUpUser(
                userData.ad_soyad!!,
                userData.dogum_tarihi!!,
                userData.cinsiyet!!,
                userData.eposta!!,
                userData.parola!!,
                userData.telefon!!,
                userData.gsm!!,
                userData.il,
                userData.ilce
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Sign>(){
                    override fun onSuccess(t: Sign) {
                        if (t.result.message != null)
                            errorMessage.value = t.result.message

                        if (t.user.ad_soyad != null)
                            successMessage.value = "Başarıyla kayıt olundu"
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}