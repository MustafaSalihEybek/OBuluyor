package com.nexis.obuluyor.view.addadvert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.ModulesAdapter
import com.nexis.obuluyor.databinding.FragmentAddAdvertDetailsPage1Binding
import com.nexis.obuluyor.model.*
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertDetailsPage1ViewModel
import com.nexis.obuluyor.viewmodel.AddAdvertDetailsViewModel
import kotlinx.android.synthetic.main.fragment_add_advert_details_page1.*

class AddAdvertDetailsPage1Fragment(val userId: Int, val categoryData: Category, val subCategoryList: Array<SubCategory>) : Fragment() {
    private lateinit var v: View
    private lateinit var addAdvertDetailsPage1ViewModel: AddAdvertDetailsPage1ViewModel
    private lateinit var page1Binding: FragmentAddAdvertDetailsPage1Binding

    private lateinit var editedCategoryList: ArrayList<Int>
    private lateinit var categoriesData: String
    private lateinit var moduleIdData: String
    private lateinit var moduleIdList: List<ModuleId>
    private lateinit var moduleList: List<Module>
    private lateinit var modulesAdapter: ModulesAdapter
    private lateinit var moduleNameList: ArrayList<String>

    private lateinit var selectedModule: Module
    private lateinit var selectedModuleContent: ModuleContent
    private var selectedModuleIn: Int = 0

    private lateinit var selectedModuleContentList: ArrayList<ModuleContent>
    private lateinit var selectedModuleList: ArrayList<Module>

    private var moduleExistsIn: Int = -1
    private var isCreated: Boolean = false

    private fun init(){
        editedCategoryList = ArrayList()
        selectedModuleContentList = ArrayList()
        selectedModuleList = ArrayList()

        for (cIn in 9 downTo 0){
            if (cIn > subCategoryList.size - 1){
                if (cIn == subCategoryList.size)
                    editedCategoryList.add(categoryData.Id)
                else
                    editedCategoryList.add(0)
            }
            else
                editedCategoryList.add(subCategoryList.get(subCategoryList.size - (cIn + 1)).Id)
        }

        editedCategoryList.reverse()
        categoriesData = getCategoriesData(editedCategoryList)

        addAdvertDetailsPage1ViewModel = ViewModelProvider(this).get(AddAdvertDetailsPage1ViewModel::class.java)
        observeLiveData()

        if (!isCreated)
            addAdvertDetailsPage1ViewModel.getModulesForCategories(categoriesData)

        add_advert_details_page1_fragment_recyclerView.setHasFixedSize(true)
        add_advert_details_page1_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        modulesAdapter = ModulesAdapter(arrayListOf(), arrayListOf())
        add_advert_details_page1_fragment_recyclerView.adapter = modulesAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        page1Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_advert_details_page1, container, false)
        return page1Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        if (isCreated){
            moduleIdList = Singleton.moduleIdList
            moduleIdData = Singleton.moduleIdData
            moduleList = Singleton.moduleList
            selectedModuleList = page1ModuleList
            selectedModuleContentList = page1ModuleContentList
            moduleNameList = Singleton.moduleNameList

            setModule(moduleList)
        }
    }

    private fun observeLiveData(){
        addAdvertDetailsPage1ViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addAdvertDetailsPage1ViewModel.moduleIdList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!isCreated){
                    moduleIdList = it
                    moduleIdData = getModuleData(it)

                    Singleton.moduleIdList = it
                    Singleton.moduleIdData = moduleIdData

                    addAdvertDetailsPage1ViewModel.getModules(moduleIdData)
                }
            }
        })

        addAdvertDetailsPage1ViewModel.moduleList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!isCreated){
                    moduleList = it
                    Singleton.moduleList = it

                    setModule(moduleList)
                }
            }
        })

        addAdvertDetailsPage1ViewModel.moduleContent.observe(viewLifecycleOwner, Observer {
            it?.let {
                setModuleContent(it)
            }
        })

        addAdvertDetailsPage1ViewModel.selectedModuleContent.observe(viewLifecycleOwner, Observer {
            it?.let {
               setModuleContentByClass(it)
            }
        })
    }

    private fun setModule(moduleList: List<Module>){
        if (!isCreated){
            moduleNameList = ArrayList()

            for (m in moduleList.indices)
                moduleNameList.add("")

            isCreated = true
        }

        modulesAdapter.loadData(moduleList, moduleNameList)

        modulesAdapter.setModulesOnItemClickListener(object : ModulesAdapter.ModulesItemClickListener{
            override fun onItemClick(moduleData: Module, mIn: Int) {
                selectedModule = moduleData
                selectedModuleIn = mIn

                Singleton.showAddModuleContentDialog(v.context, moduleData, addAdvertDetailsPage1ViewModel, viewLifecycleOwner)
            }
        })
    }

    private fun setModuleContent(moduleName: String){
        moduleNameList[selectedModuleIn] = moduleName
        modulesAdapter.loadModuleNames(moduleNameList)

        if (!moduleIsExists(selectedModuleList, selectedModule))
            selectedModuleList.add(selectedModule)

        page1ModuleList = selectedModuleList
        Singleton.moduleNameList = moduleNameList
        //writeSelectedModuleList(selectedModuleList)
    }

    private fun setModuleContentByClass(moduleContent: ModuleContent){
        selectedModuleContent = moduleContent
        moduleExistsIn = moduleIsExistsIn(selectedModuleList, selectedModule)

        if (moduleExistsIn != -1) {
            if (selectedModuleContentList.size > 0)
                selectedModuleContentList[moduleExistsIn] = selectedModuleContent
        }
        else
            selectedModuleContentList.add(selectedModuleContent)

        page1ModuleContentList = selectedModuleContentList
        //writeSelectedModuleContentList(selectedModuleContentList)
    }

    private fun getCategoriesData(editedCategoryList: ArrayList<Int>) : String {
        var categoriesData: String = ""

        for (cIn in editedCategoryList.indices){
            if (editedCategoryList.get(cIn) == 0)
                break

            if (editedCategoryList.get(cIn + 1) == 0)
                categoriesData += "${editedCategoryList.get(cIn)}"
            else
                categoriesData += "${editedCategoryList.get(cIn)},"
        }

        return categoriesData
    }

    private fun getModuleData(moduleList: List<ModuleId>) : String {
        var moduleIdData: String = ""

        for (mIn in moduleList.indices){
            if (mIn == (moduleList.size - 1))
                moduleIdData += "${moduleList.get(mIn).moduleId}"
            else
                moduleIdData += "${moduleList.get(mIn).moduleId},"
        }

        return moduleIdData
    }

    private fun writeSelectedModuleList(selectedModuleList: ArrayList<Module>) {
        println("------- ModuleList -------")

        for (module in selectedModuleList)
            println("Module = ${module}")
    }

    private fun moduleIsExists(selectedModuleList: ArrayList<Module>, module: Module) : Boolean {
        for (m in selectedModuleList){
            if (m.Id!! == module.Id!!)
                return true
        }

        return false
    }

    private fun moduleIsExistsIn(selectedModuleList: ArrayList<Module>, module: Module) : Int {
        for (m in selectedModuleList.indices){
            if (selectedModuleList.get(m).Id!! == module.Id!!)
                return m
        }

        return -1
    }

    private fun writeSelectedModuleContentList(selectedModuleContentList: ArrayList<ModuleContent>) {
        println("------- ModuleContentList -------")

        for (moduleContent in selectedModuleContentList)
            println("ModuleContent = ${moduleContent}")
    }

    companion object{
        var page1ModuleList: ArrayList<Module> = arrayListOf()
        var page1ModuleContentList: ArrayList<ModuleContent> = arrayListOf()
    }
}