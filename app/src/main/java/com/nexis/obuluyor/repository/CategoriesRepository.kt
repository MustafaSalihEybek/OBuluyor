package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.Singleton
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody

class CategoriesRepository {
    fun getCategories() : Single<List<Category>> {
        return AppUtils.getAppAPI().getCategories()
    }
}