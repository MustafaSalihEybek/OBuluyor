package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetAdvertsByUserRepository {
    fun getAdvertsByUser(confirm: Int, activeAdverts: Int, userId: Int) : Single<List<Advert>> {
        return AppUtils.getAppAPI().getAdvertsByUser(confirm, activeAdverts, userId)
    }
}