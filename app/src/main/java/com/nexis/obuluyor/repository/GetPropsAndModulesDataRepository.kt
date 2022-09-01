package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.PropData
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetPropsAndModulesDataRepository {
    fun getPropsData(propsIdData: String?, modulesIdData: String?) : Single<List<PropData>> {
        return AppUtils.getAppAPI().getPropsData(propsIdData, modulesIdData)
    }
}