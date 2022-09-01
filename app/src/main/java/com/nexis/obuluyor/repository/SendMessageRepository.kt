package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Result
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class SendMessageRepository {
    fun sendMessage(userId: Int, targetId: Int, messageContent: String, advertId: Int) : Single<Result> {
        return AppUtils.getAppAPI().sendMessage(userId, targetId, messageContent, advertId)
    }
}