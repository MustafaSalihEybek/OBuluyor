package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.AddAdvertImage
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddImageForAdvertRepository {
    fun addImageForAdvert(advertId: RequestBody, advertImage: MultipartBody.Part?) : Single<AddAdvertImage> {
        return AppUtils.getAppAPI().addImageForAdvert(advertId, advertImage)
    }
}