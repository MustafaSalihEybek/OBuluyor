package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Store
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class StoreListRepository {
    fun getStoreList() : Single<List<Store>> {
        return AppUtils.getAppAPI().getStoreList()
    }
}