package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Favorite
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class FavoritesRepository {
    fun getFavorites(userId: Int) : Single<List<Favorite>> {
        return AppUtils.getAppAPI().getFavorites(userId)
    }
}