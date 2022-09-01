package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.StorePaymentInfo
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class StorePaymentInfoRepository {
    fun getStorePaymentInfo() : Single<StorePaymentInfo> {
        return AppUtils.getAppAPI().getStorePaymentInfo()
    }
}