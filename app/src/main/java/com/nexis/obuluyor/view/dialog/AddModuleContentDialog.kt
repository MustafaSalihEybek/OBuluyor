package com.nexis.obuluyor.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.AddModuleContentDialogBinding
import com.nexis.obuluyor.model.Module
import com.nexis.obuluyor.model.ModuleContent
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.viewmodel.AddAdvertDetailsPage1ViewModel
import kotlinx.android.synthetic.main.add_module_content_dialog.*

class AddModuleContentDialog(val mContext: Context, val moduleData: Module, val adV: AddAdvertDetailsPage1ViewModel, val owner: LifecycleOwner) : Dialog(mContext), View.OnClickListener {
    private lateinit var addModuleBinding: AddModuleContentDialogBinding

    private lateinit var txtModuleContent: String
    private lateinit var moduleContentList: List<ModuleContent>
    private lateinit var moduleContentAdapter: ArrayAdapter<*>
    private lateinit var moduleContentNameList: ArrayList<String>
    private var selectedModuleContentIn: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addModuleBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.add_module_content_dialog, null, false)
        addModuleBinding.module = moduleData
        setContentView(addModuleBinding.root)

        window?.let {
            it.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        }

        add_module_content_dialog_editModuleContent.visibility = View.GONE
        add_module_content_dialog_spinnerModuleContent.visibility = View.GONE

        observeLiveData()
        adV.getModuleContents(moduleData.Id!!)

        add_module_content_dialog_btnClose.setOnClickListener(this)
        add_module_content_dialog_imgClose.setOnClickListener(this)
    }

    private fun observeLiveData(){
        adV.moduleContentList.observe(owner, Observer {
            it?.let {
                moduleContentList = it

                if (moduleContentList.get(0).name != null){
                    moduleContentNameList = ArrayList()

                    for (name in moduleContentList)
                        moduleContentNameList.add(name.name!!)

                    moduleContentAdapter = ArrayAdapter(mContext, R.layout.spinner_item, moduleContentNameList)
                    moduleContentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    add_module_content_dialog_spinnerModuleContent.adapter = moduleContentAdapter

                    add_module_content_dialog_spinnerModuleContent.visibility = View.VISIBLE
                    add_module_content_dialog_editModuleContent.visibility = View.GONE

                    add_module_content_dialog_spinnerModuleContent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            p0?.let {
                                txtModuleContent = it.getItemAtPosition(p2).toString()
                                selectedModuleContentIn = p2
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            p0?.let {
                                txtModuleContent = it.getItemAtPosition(0).toString()
                                selectedModuleContentIn = 0
                            }
                        }
                    }
                }
                else{
                    add_module_content_dialog_editModuleContent.visibility = View.VISIBLE
                    add_module_content_dialog_spinnerModuleContent.visibility = View.GONE
                }

                add_module_content_dialog_btnAdd.setOnClickListener(this)
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.add_module_content_dialog_btnClose -> closeThisDialog()
                R.id.add_module_content_dialog_imgClose -> closeThisDialog()
                R.id.add_module_content_dialog_btnAdd -> addTheModule()
            }
        }
    }

    private fun addTheModule(){
        if (moduleContentList.get(0).name == null)
            txtModuleContent = add_module_content_dialog_editModuleContent.text.toString().trim()
        else
            txtModuleContent = moduleContentList.get(selectedModuleContentIn).name!!

        if (moduleContentList.get(0).name != null)
            adV.selectModuleContent(moduleContentList.get(selectedModuleContentIn))
        else {
            if (!txtModuleContent.isEmpty()){
                AppUtils.moduleContentData = ModuleContent(null, null, txtModuleContent)
                adV.selectModuleContent(AppUtils.moduleContentData)
            }
        }

        if (!txtModuleContent.isEmpty())
            adV.addModuleContent(txtModuleContent)

        closeThisDialog()
    }

    private fun closeThisDialog(){
        if (this.isShowing)
            this.dismiss()
    }
}