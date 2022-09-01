package com.nexis.obuluyor.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.obuluyor.model.Module
import com.nexis.obuluyor.model.ModuleContent
import com.nexis.obuluyor.model.ModuleId
import com.nexis.obuluyor.repository.GetModuleContentsRepository
import com.nexis.obuluyor.repository.GetModulesForCategoriesRepository
import com.nexis.obuluyor.repository.GetModulesRepository
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class AddAdvertDetailsPage1ViewModel(application: Application) : BaseViewModel(application) {
    val errorMessage = MutableLiveData<String>()
    val moduleIdList = MutableLiveData<List<ModuleId>>()
    val moduleList = MutableLiveData<List<Module>>()
    val moduleContent = MutableLiveData<String>()
    val moduleContentList = MutableLiveData<List<ModuleContent>>()
    val selectedModuleContent = MutableLiveData<ModuleContent>()

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

    fun getModules(moduleIdData: String){
        AppUtils.getModulesRepository = GetModulesRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getModulesRepository.getModules(moduleIdData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Module>>(){
                    override fun onSuccess(t: List<Module>) {
                        moduleList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun addModuleContent(moduleContent: String){
        this.moduleContent.value = moduleContent
    }

    fun selectModuleContent(moduleContent: ModuleContent){
        selectedModuleContent.value = moduleContent
    }

    fun getModuleContents(itemId: Int){
        AppUtils.getModuleContentsRepository = GetModuleContentsRepository()
        AppUtils.disposable = CompositeDisposable()

        AppUtils.disposable.add(
            AppUtils.getModuleContentsRepository.getModuleContents(itemId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<ModuleContent>>(){
                    override fun onSuccess(t: List<ModuleContent>) {
                        moduleContentList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}