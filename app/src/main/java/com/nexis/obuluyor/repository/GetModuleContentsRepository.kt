package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.ModuleContent
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetModuleContentsRepository {
    fun getModuleContents(itemId: Int) : Single<List<ModuleContent>> {
        return AppUtils.getAppAPI().getModuleContents(itemId)
    }
}