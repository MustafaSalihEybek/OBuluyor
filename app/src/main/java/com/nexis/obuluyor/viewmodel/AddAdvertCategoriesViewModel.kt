package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.repository.CategoriesRepository
import com.nexis.obuluyor.util.AppUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AddAdvertCategoriesViewModel(application: Application) : AndroidViewModel(application) {
    val subCategoryList = MutableLiveData<List<Category>>()
    val errorMessage = MutableLiveData<String>()

    fun getCategories(){
        AppUtils.categoriesRepository = CategoriesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.categoriesRepository.getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Category>>(){
                    override fun onSuccess(t: List<Category>) {
                        subCategoryList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}