package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.SubCategory
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class SubCategoriesRepository {
    fun getSubCategories(categoryId: Int) : Single<List<SubCategory>> {
        return AppUtils.getAppAPI().getSubCategories(categoryId)
    }
}