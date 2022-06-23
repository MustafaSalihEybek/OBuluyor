package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Sign
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class SignUpRepository {
    fun signUpUser(
        fullName: String,
        birthday: String,
        gender: String,
        email: String,
        password: String,
        phone: String,
        gsm: String,
        country: Int,
        city: Int
    ) : Single<Sign> {
        return  AppUtils.getAppAPI().signUpUser(fullName, birthday, gender, email, password, phone, gsm, country, city)
    }
}