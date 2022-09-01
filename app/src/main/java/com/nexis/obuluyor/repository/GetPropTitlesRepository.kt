package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.GroupTitle
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class GetPropTitlesRepository {
    fun getPropTitles(groupsIdData: String) : Single<List<GroupTitle>> {
        return AppUtils.getAppAPI().getPropTitles(groupsIdData)
    }
}