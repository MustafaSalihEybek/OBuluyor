package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.Result
import com.nexis.obuluyor.model.Sign
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.repository.CitiesRepository
import com.nexis.obuluyor.repository.CountriesRepository
import com.nexis.obuluyor.repository.UpdateUserDataRepository
import com.nexis.obuluyor.repository.UserDataRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import kotlin.math.sign

class ProfileViewModel(application: Application) : BaseViewModel(application) {
    val userData = MutableLiveData<User>()
    val countryList = MutableLiveData<List<Country>>()
    val cityList = MutableLiveData<List<City>>()
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()

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

    fun getCountryList(){
        AppUtils.countriesRepository = CountriesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.countriesRepository.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        countryList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getCityList(countryId: Int){
        AppUtils.citiesRepository = CitiesRepository()
        AppUtils.disposable = CompositeDisposable()

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

    fun updateUserData(
        userId: Int,
        userFullName: String,
        userBirthday: String,
        userGender: String,
        userPassword: String,
        userPhone: String,
        userGsm: String,
        userCountry: Int,
        userCity: Int
    ){
        AppUtils.updateUserDataRepository = UpdateUserDataRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.updateUserDataRepository.updateUserData(
                userId,
                userFullName,
                userBirthday,
                userGender,
                userPassword,
                userPhone,
                userGsm,
                userCountry,
                userCity
            )
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