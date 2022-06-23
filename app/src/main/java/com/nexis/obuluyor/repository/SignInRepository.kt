package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Sign
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class SignInRepository {
    fun signInUser(email: String, password: String) : Single<Sign> {
        return AppUtils.getAppAPI().signInUser(email, password)
    }
}