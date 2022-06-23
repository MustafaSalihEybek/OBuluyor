package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class CountriesRepository {
    fun getCountries() : Single<List<Country>> {
        return AppUtils.getAppAPI().getCountries()
    }
}