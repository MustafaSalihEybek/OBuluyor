package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.District
import com.nexis.obuluyor.repository.CitiesRepository
import com.nexis.obuluyor.repository.CountriesRepository
import com.nexis.obuluyor.repository.GetDistrictsRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AddAdvertDetailsPage3ViewModel(application: Application) : BaseViewModel(application) {
    val countryList = MutableLiveData<List<Country>>()
    val cityList = MutableLiveData<List<City>>()
    val errorMessage = MutableLiveData<String>()
    val districtList = MutableLiveData<List<District>>()

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

    fun getDistrictList(cityIn: Int){
        AppUtils.getDistrictsRepository = GetDistrictsRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getDistrictsRepository.getDistricts(cityIn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<District>>(){
                    override fun onSuccess(t: List<District>) {
                        districtList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}