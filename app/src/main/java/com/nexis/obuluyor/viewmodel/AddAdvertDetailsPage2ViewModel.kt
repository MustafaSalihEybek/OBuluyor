package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.GroupTitle
import com.nexis.obuluyor.model.ModuleId
import com.nexis.obuluyor.model.PropData
import com.nexis.obuluyor.repository.GetModulesForCategoriesRepository
import com.nexis.obuluyor.repository.GetPropTitlesRepository
import com.nexis.obuluyor.repository.GetPropsAndModulesDataRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AddAdvertDetailsPage2ViewModel(application: Application) : BaseViewModel(application) {
    val errorMessage = MutableLiveData<String>()
    val moduleIdList = MutableLiveData<List<ModuleId>>()
    val propDataList = MutableLiveData<List<PropData>>()
    val groupTitleList = MutableLiveData<List<GroupTitle>>()

    fun getModulesForCategories(categoriesData: String){
        AppUtils.getModulesForCategoriesRepository = GetModulesForCategoriesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getModulesForCategoriesRepository.getModulesForCategories(categoriesData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<ModuleId>>(){
                    override fun onSuccess(t: List<ModuleId>) {
                        moduleIdList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getPropsDataByModules(modulesIdData: String){
        AppUtils.getPropsAndModulesDataRepository = GetPropsAndModulesDataRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getPropsAndModulesDataRepository.getPropsData(null, modulesIdData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<PropData>>(){
                    override fun onSuccess(t: List<PropData>) {
                        propDataList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getGroupTitles(groupsIdData: String){
        AppUtils.getPropTitlesRepository = GetPropTitlesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getPropTitlesRepository.getPropTitles(groupsIdData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<GroupTitle>>(){
                    override fun onSuccess(t: List<GroupTitle>) {
                        groupTitleList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}