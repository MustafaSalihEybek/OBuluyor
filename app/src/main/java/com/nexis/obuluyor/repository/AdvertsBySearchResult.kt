package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class AdvertsBySearchResult {
    fun getAdvertsBySearchResult(confirm: Int, activeAdverts: Int) : Single<List<Advert>> {
        return AppUtils.getAppAPI().getAdvertsBySearchResult(confirm, activeAdverts)
    }
}