package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.AddStore
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.Part
import java.util.*

class AddStoreRepository {
    fun addStore(
        userId: RequestBody,
        storeName: RequestBody,
        storeAddress: RequestBody,
        storeDescription: RequestBody,
        categoryId: RequestBody,
        time: RequestBody,
        timeEnd: RequestBody,
        paymentType: RequestBody,
        price: RequestBody,
        imageLogo: MultipartBody.Part,
        imageSlider: MultipartBody.Part,
        storeQuota: RequestBody
    ) : Single<AddStore> {
        return AppUtils.getAppAPI().addStore(
            userId,
            storeName,
            storeAddress,
            storeDescription,
            categoryId,
            time,
            timeEnd,
            paymentType,
            price,
            imageLogo,
            imageSlider,
            storeQuota
        )
    }
}