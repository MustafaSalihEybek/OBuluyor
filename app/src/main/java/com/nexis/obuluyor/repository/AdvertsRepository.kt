package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single
import retrofit2.http.Field

class AdvertsRepository {
    fun getAdverts(
        confirm: Int,
        activeAdverts: Int,
        categoriesData: String?,
        random: Int,
        randomLimit: Int
    ) : Single<List<Advert>> {
        return AppUtils.getAppAPI().getAdverts(
            confirm,
            activeAdverts,
            categoriesData,
            random,
            randomLimit
        )
    }
}