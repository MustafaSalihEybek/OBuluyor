package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Sign
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single
import okhttp3.ResponseBody

class UserDataRepository {
    fun getUserData(userId: Int) : Single<Sign> {
        return AppUtils.getAppAPI().getUserData(userId)
    }
}