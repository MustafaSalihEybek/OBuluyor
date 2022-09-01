package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.UpdateStore
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateStoreDataRepository {
    fun updateStoreData(
        userId: RequestBody,
        storeName: RequestBody,
        storeDescription: RequestBody,
        imageLogo: MultipartBody.Part?,
        storeSlider: MultipartBody.Part?
    ) : Single<UpdateStore> {
        return AppUtils.getAppAPI().updateStoreData(
            userId,
            storeName,
            storeDescription,
            imageLogo,
            storeSlider
        )
    }
}