package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.SubCategory
import com.nexis.obuluyor.repository.SubCategoriesRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AddAdvertSelectSubCategoriesViewModel(application: Application) : BaseViewModel(application) {
    val subCategoryList = MutableLiveData<List<SubCategory>>()
    val errorMessage = MutableLiveData<String>()

    fun getSubCategories(categoryId: Int){
        AppUtils.subCategoriesRepository = SubCategoriesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.subCategoriesRepository.getSubCategories(categoryId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<SubCategory>>(){
                    override fun onSuccess(t: List<SubCategory>) {
                        subCategoryList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}