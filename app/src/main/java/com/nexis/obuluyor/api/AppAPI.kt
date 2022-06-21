package com.nexis.obuluyor.api

import com.nexis.obuluyor.model.Category
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AppAPI {
    @POST("ana_kategori.php")
    fun getCategories() : Single<List<Category>>
}