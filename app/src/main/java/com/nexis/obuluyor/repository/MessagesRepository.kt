package com.nexis.obuluyor.repository

import com.nexis.obuluyor.model.Message
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.Single

class MessagesRepository {
    fun getMessages(userId: Int, messageType: String) : Single<List<Message>> {
        return AppUtils.getAppAPI().getMessages(userId, messageType)
    }
}