package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single
import retrofit2.http.Field

class OtherAdvertsRepository {
    fun getOtherAdverts(confirm: Int, activeAdverts: Int, priceDrops: String?, urgent: String?, last48Hours: String?) : Single<List<Advert>> {
        return AppUtils.getAppAPI().getOtherAdverts(confirm, activeAdverts, priceDrops, urgent, last48Hours)
    }
}