package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody

class CitiesRepository {
    fun getCities(countryId: Int) : Single<List<City>> {
        return AppUtils.getAppAPI().getCities(countryId)
    }
}