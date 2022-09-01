package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Result
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class UpdateUserDataRepository {
    fun updateUserData(
        userId: Int,
        userFullName: String,
        userBirthday: String,
        userGender: String,
        userPassword: String,
        userPhone: String,
        userGsm: String,
        userCountry: Int,
        userCity: Int
    ) : Single<Result> {
        return AppUtils.getAppAPI().updateUserData(
            userId,
            userFullName,
            userBirthday,
            userGender,
            userPassword,
            userPhone,
            userGsm,
            userCountry,
            userCity
        )
    }
}