package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Result
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class AddFavoriteRepository {
    fun addFavorite(userId: Int, advertId: Int) : Single<Result> {
        return AppUtils.getAppAPI().addFavorite(userId, advertId)
    }
}