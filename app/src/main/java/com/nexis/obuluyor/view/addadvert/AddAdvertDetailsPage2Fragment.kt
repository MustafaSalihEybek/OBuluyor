package com.nexis.obuluyor.view.addadvert

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.PropertiesAdapter
import com.nexis.obuluyor.adapter.PropertiesCheckboxAdapter
import com.nexis.obuluyor.model.*
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertDetailsPage2ViewModel
import kotlinx.android.synthetic.main.fragment_add_advert_details_page2.*
import kotlinx.android.synthetic.main.fragment_advert_details.*

class AddAdvertDetailsPage2Fragment(val userId: Int, val categoryData: Category, val subCategoryList: Array<SubCategory>) : Fragment() {
    private lateinit var v: View
    private lateinit var addAdvertDetailsPage2ViewModel: AddAdvertDetailsPage2ViewModel

    private lateinit var editedCategoryList: ArrayList<Int>
    private lateinit var moduleIdList: List<ModuleId>
    private lateinit var categoriesData: String
    private lateinit var modulesIdData: String
    private lateinit var groupsIdData: String
    private lateinit var propDataList: List<PropData>
    private lateinit var groupTitleList: List<GroupTitle>
    private lateinit var filteredGroupList: ArrayList<GroupTitle>

    private lateinit var propertiesAdapter: PropertiesCheckboxAdapter
    private lateinit var selectedPropertiesList: ArrayList<PropData>
    private var resultPropId: Int = -1

    private lateinit var propList: ArrayList<Prop>
    private lateinit var propIdData: String
    private lateinit var propValData: String

    private fun init(){
        editedCategoryList = ArrayList()

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

        addAdvertDetailsPage2ViewModel = ViewModelProvider(this).get(AddAdvertDetailsPage2ViewModel::class.java)
        observeLiveData()
        addAdvertDetailsPage2ViewModel.getModulesForCategories(categoriesData)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_advert_details_page2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        addAdvertDetailsPage2ViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addAdvertDetailsPage2ViewModel.moduleIdList.observe(viewLifecycleOwner, Observer {
            it?.let {
                moduleIdList = it
                modulesIdData = getModuleData(it)

                addAdvertDetailsPage2ViewModel.getPropsDataByModules(modulesIdData)
            }
        })

        addAdvertDetailsPage2ViewModel.propDataList.observe(viewLifecycleOwner, Observer {
            it?.let {
                propDataList = it
                groupsIdData = getGroupsIdData(propDataList)
                propList = ArrayList()

                for (propData in propDataList){
                    AppUtils.mProp = Prop(null, propData.Id, null, null)
                    propList.add(AppUtils.mProp)
                }

                propIdData = getPropData(propList, true)
                propValData = getPropData(propList, false)

                page2PropIdData = propIdData
                page2PropValData = propValData

                addAdvertDetailsPage2ViewModel.getGroupTitles(groupsIdData)
            }
        })

        addAdvertDetailsPage2ViewModel.groupTitleList.observe(viewLifecycleOwner, Observer {
            it?.let {
                groupTitleList = it
                filteredGroupList = getGroupNameList(groupTitleList)
                selectedPropertiesList = ArrayList()

                var txtGroupTitle: TextView
                var propertyRecyclerView: RecyclerView
                var layoutParams: LinearLayout.LayoutParams

                for (gIn in filteredGroupList.indices){
                    txtGroupTitle = TextView(v.context)
                    txtGroupTitle.text = filteredGroupList.get(gIn).groupName
                    txtGroupTitle.typeface = ResourcesCompat.getFont(v.context, R.font.poppins_bold)
                    txtGroupTitle.textSize = 15f

                    if (Singleton.themeMode.equals("Dark"))
                        txtGroupTitle.setTextColor(Color.parseColor("#FFFFFF"))
                    else
                        txtGroupTitle.setTextColor(Color.parseColor("#F77E21"))

                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    if (gIn == 0)
                        layoutParams.setMargins(15, 10, 15, 10)
                    else
                        layoutParams.setMargins(15, 35, 15, 10)

                    add_advert_details_page2_linearLayout.addView(txtGroupTitle, layoutParams)

                    propertyRecyclerView = RecyclerView(v.context)
                    propertyRecyclerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    propertyRecyclerView.setHasFixedSize(true)
                    propertyRecyclerView.layoutManager = GridLayoutManager(v.context, 3)
                    propertiesAdapter = PropertiesCheckboxAdapter(getFilteredPropsData(propDataList, filteredGroupList.get(gIn).Id!!))
                    propertyRecyclerView.adapter = propertiesAdapter

                    propertiesAdapter.setOnPropertiesCheckedListener(object : PropertiesCheckboxAdapter.PropertiesCheckedListener{
                        override fun onChecked(propData: PropData, checkedState: Boolean) {
                            if (checkedState)
                                selectedPropertiesList.add(propData)
                            else {
                                resultPropId = getPropIdInList(propData, selectedPropertiesList)

                                if (resultPropId != -1)
                                    selectedPropertiesList.removeAt(resultPropId)
                            }

                            propList = setPropList(propList, propData, checkedState)
                            propIdData = getPropData(propList, true)
                            propValData = getPropData(propList, false)

                            page2PropIdData = propIdData
                            page2PropValData = propValData

                            //writeSelectedPropList(selectedPropertiesList)
                        }
                    })

                    add_advert_details_page2_linearLayout.addView(propertyRecyclerView)
                }
            }
        })
    }

    private fun getPropIdInList(propData: PropData, selectedPropertiesList: ArrayList<PropData>) : Int {
        for (p in selectedPropertiesList.indices){
            if (selectedPropertiesList.get(p).Id!! == propData.Id!!)
                return p
        }

        return -1
    }

    private fun writeSelectedPropList(selectedPropertiesList: ArrayList<PropData>) {
        for (prop in selectedPropertiesList)
            println("Prop = ${prop}")
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

    private fun getGroupsIdData(propDataList: List<PropData>) : String {
        var groupsId: String = ""

        for (pIn in propDataList.indices){
            if (pIn == (propDataList.size - 1))
                groupsId += "${propDataList.get(pIn).groupId}"
            else
                groupsId += "${propDataList.get(pIn).groupId},"
        }

        return groupsId
    }

    private fun getGroupNameList(groupTitleList: List<GroupTitle>) : ArrayList<GroupTitle> {
        val groups: ArrayList<GroupTitle> = ArrayList()

        for (gData in groupTitleList){
            gData.groupName?.let {
                if (!isExists(groups, it))
                    groups.add(gData)
            }
        }

        return groups
    }

    private fun isExists(groupList: ArrayList<GroupTitle>, groupName: String) : Boolean {
        for (name in groupList){
            if (name.groupName.equals(groupName))
                return true
        }

        return false
    }

    private fun getFilteredPropsData(propDataList: List<PropData>, groupId: Int) : ArrayList<PropData> {
        val propsData: ArrayList<PropData> = ArrayList()

        for (pIn in propDataList.indices){
            propDataList.get(pIn).groupId?.let {
                if (it == groupId){
                    propsData.add(propDataList.get(pIn))
                }
            }
        }

        return propsData
    }

    private fun setPropList(propList: ArrayList<Prop>, propData: PropData, checkedState: Boolean) : ArrayList<Prop> {
        val pList: ArrayList<Prop> = ArrayList()

        for (prop in propList.indices){
            if (propList.get(prop).propId!! == propData.Id){
                if (checkedState)
                    AppUtils.mProp = Prop(propList.get(prop).Id, propList.get(prop).propId, 1, propList.get(prop).advertId)
                else
                    AppUtils.mProp = Prop(propList.get(prop).Id, propList.get(prop).propId, null, propList.get(prop).advertId)
            } else
                AppUtils.mProp = Prop(propList.get(prop).Id, propList.get(prop).propId, propList.get(prop).isUse, propList.get(prop).advertId)

            pList.add(AppUtils.mProp)
        }

        return pList
    }

    private fun getPropData(propList: ArrayList<Prop>, propId: Boolean) : String {
        var propData: String = ""

        for (p in propList.indices){
            if (propId) {
                if (p == (propList.size - 1))
                    propData += "${propList.get(p).propId}"
                else
                    propData += "${propList.get(p).propId},"
            } else {
                if (propList.get(p).isUse != null){
                    if (p == (propList.size - 1))
                        propData += "${propList.get(p).isUse}"
                    else
                        propData += "${propList.get(p).isUse},"
                } else {
                    if (p == (propList.size - 1))
                        propData += "n"
                    else
                        propData += "n,"
                }
            }
        }

        return propData
    }

    companion object{
        var page2PropIdData: String = ""
        var page2PropValData: String = ""
    }
}