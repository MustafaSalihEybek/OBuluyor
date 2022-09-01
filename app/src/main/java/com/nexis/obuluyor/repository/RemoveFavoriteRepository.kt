package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Result
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class RemoveFavoriteRepository {
    fun removeFavorite(userId: Int, advertId: Int, favoriteId: Int) : Single<Result> {
        return AppUtils.getAppAPI().removeFavorite(userId, advertId, favoriteId)
    }
}