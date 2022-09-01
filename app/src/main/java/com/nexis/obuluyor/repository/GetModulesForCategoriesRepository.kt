package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.ModuleId
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetModulesForCategoriesRepository {
    fun getModulesForCategories(categoriesData: String) : Single<List<ModuleId>> {
        return AppUtils.getAppAPI().getModulesForCategories(categoriesData)
    }
}