package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.repository.CategoriesRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

class MainViewModel(application: Application) : BaseViewModel(application) {
    private val categoriesRepository = CategoriesRepository()

    private lateinit var jsonObject: JSONObject
    private lateinit var jsonObjectString: String
    private lateinit var requestBody: RequestBody

    val errorMessage = MutableLiveData<String>()
    val categoryList = MutableLiveData<List<Category>>()

    fun getCategories(){
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            categoriesRepository.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Category>>(){
                    override fun onSuccess(t: List<Category>) {
                        categoryList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}