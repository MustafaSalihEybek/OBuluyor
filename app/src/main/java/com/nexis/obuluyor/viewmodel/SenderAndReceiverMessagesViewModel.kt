package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Message
import com.nexis.obuluyor.model.Result
import com.nexis.obuluyor.model.Sign
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.repository.MessagesRepository
import com.nexis.obuluyor.repository.SendMessageRepository
import com.nexis.obuluyor.repository.UserDataRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SenderAndReceiverMessagesViewModel(application: Application) : BaseViewModel(application) {
    val errorMessage = MutableLiveData<String>()
    val messageList = MutableLiveData<List<Message>>()
    val userData = MutableLiveData<User>()
    val sendMessage = MutableLiveData<String>()

    fun getMessages(userId: Int, messageType: String){
        AppUtils.messagesRepository = MessagesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.messagesRepository.getMessages(userId, messageType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Message>>(){
                    override fun onSuccess(t: List<Message>) {
                        messageList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getSenderUserData(userId: Int){
        AppUtils.userDataRepository = UserDataRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.userDataRepository.getUserData(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Sign>(){
                    override fun onSuccess(t: Sign) {
                        if (t.result.message != null)
                            errorMessage.value = t.result.message
                        else
                            userData.value = t.user
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun sendMessage(userId: Int, targetId: Int, messageContent: String, advertId: Int){
        AppUtils.sendMessageRepository = SendMessageRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.sendMessageRepository.sendMessage(userId, targetId, messageContent, advertId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Result>(){
                    override fun onSuccess(t: Result) {
                        sendMessage.value = t.message
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}