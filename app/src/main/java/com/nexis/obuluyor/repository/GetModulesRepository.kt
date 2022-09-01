package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Module
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetModulesRepository {
    fun getModules(moduleIdData: String) : Single<List<Module>> {
        return AppUtils.getAppAPI().getModules(moduleIdData)
    }
}