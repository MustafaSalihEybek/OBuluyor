package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.District
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetDistrictsRepository {
    fun getDistricts(cityIn: Int) : Single<List<District>> {
        return AppUtils.getAppAPI().getDistricts(cityIn)
    }
}