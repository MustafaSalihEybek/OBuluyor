package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Prop
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetPropsRepository {
    fun getProps(advertId: Int) : Single<List<Prop>> {
        return AppUtils.getAppAPI().getProps(advertId)
    }
}