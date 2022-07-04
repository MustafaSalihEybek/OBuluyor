package com.nexis.obuluyor.view.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.UpdateUserProfileDialogBinding
import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.update_user_profile_dialog.*
import java.util.*
import kotlin.collections.ArrayList

class UpdateUserProfileDialog(val mContext: Context, val mUser: User, val countryList: List<Country>, val pV: ProfileViewModel, val owner: LifecycleOwner) : Dialog(mContext), View.OnClickListener {
    private lateinit var v: UpdateUserProfileDialogBinding

    private lateinit var genderArrayAdapter: ArrayAdapter<CharSequence>
    private lateinit var txtSelectedGender: String

    private lateinit var countryArrayAdapter: ArrayAdapter<*>
    private lateinit var countryNameList: ArrayList<String>
    private var selectedCountryIn: Int = -1

    private lateinit var cityArrayAdapter: ArrayAdapter<*>
    private lateinit var cityNameList: ArrayList<String>
    private lateinit var cityList: List<City>
    private var selectedCityIn: Int = -1

    private lateinit var txtUserFullName: String
    private lateinit var txtUserPassword: String
    private lateinit var txtUserPhoneNumber: String
    private lateinit var txtUserGsmNumber: String

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var dataSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var dateString: String
    private lateinit var calendar: Calendar
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    private fun init(){
        genderArrayAdapter = ArrayAdapter.createFromResource(mContext, R.array.GenderList, R.layout.spinner_item)
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        update_user_profile_dialog_spinnerUserGender.adapter = genderArrayAdapter
        update_user_profile_dialog_spinnerUserGender.setSelection(getGenderSelectedIndex(mUser.cinsiyet!!), true)

        update_user_profile_dialog_spinnerUserGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    txtSelectedGender = it.getItemAtPosition(p2).toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    txtSelectedGender = it.getItemAtPosition(0).toString()
                }
            }
        }

        countryNameList = ArrayList()

        for (country in countryList)
            countryNameList.add(country.il_adi!!)

        countryArrayAdapter = ArrayAdapter(mContext, R.layout.spinner_item, countryNameList)
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        update_user_profile_dialog_spinnerUserCountry.adapter = countryArrayAdapter
        update_user_profile_dialog_spinnerUserCountry.setSelection(getCountrySelectedIndex(mUser.il!!), true)
        pV.getCityList(countryList.get(getCountrySelectedIndex(mUser.il)).id)

        update_user_profile_dialog_spinnerUserCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedCountryIn = p2
                    pV.getCityList(countryList.get(selectedCountryIn).id)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedCountryIn = 0
                    pV.getCityList(countryList.get(selectedCountryIn).id)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.update_user_profile_dialog, null, false)
        v.user = mUser
        setContentView(v.root)
        init()

        window?.let {
            it.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        }

        observeLiveData()
        initDatePicker()
        update_user_profile_dialog_imgClose.setOnClickListener(this)
        update_user_profile_dialog_editUserBirthday.setOnClickListener(this)
        update_user_profile_dialog_btnUpdate.setOnClickListener(this)
    }

    private fun observeLiveData(){
        pV.errorMessage.observe(owner, Observer {
            it?.let {
                it.show(v.root, it)
            }
        })

        pV.cityList.observe(owner, Observer {
            it?.let {
                cityList = it
                fillCityDataFromListAndSpinner(cityList)
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.update_user_profile_dialog_imgClose -> closeThisDialog()
                R.id.update_user_profile_dialog_editUserBirthday -> datePickerDialog.show()
                R.id.update_user_profile_dialog_btnUpdate -> updateUserData()
            }
        }
    }

    private fun updateUserData(){

    }

    private fun closeThisDialog(){
        if (this.isShowing)
            this.dismiss()
    }

    private fun initDatePicker(){
        dataSetListener = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
            dateString = makeDateString(day, (month + 1), year)
            update_user_profile_dialog_editUserBirthday.setText(dateString)
        }

        calendar = Calendar.getInstance()

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(mContext, dataSetListener, year, month, day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
    }

    private fun makeDateString(day: Int, month: Int, year: Int) : String {
        return "$year-${getDayAndMonth(month)}-${getDayAndMonth(day)}"
    }

    private fun getDayAndMonth(dayOrMonth: Int) : String{
        if (dayOrMonth < 10)
            return "0$dayOrMonth"

        return "$dayOrMonth"
    }

    private fun getGenderSelectedIndex(gender: String) : Int {
        if (gender.equals("Erkek"))
            return 0

        return 1
    }

    private fun getCountrySelectedIndex(countryId: Int) : Int {
        for (cIn in countryList.indices){
            if (countryList.get(cIn).id == countryId)
                return cIn
        }

        return 0
    }

    private fun fillCityDataFromListAndSpinner(cityList: List<City>){
        cityNameList = ArrayList()

        for (city in cityList)
            cityNameList.add(city.county_adi!!)

        cityArrayAdapter = ArrayAdapter(mContext, R.layout.spinner_item, cityNameList)
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        update_user_profile_dialog_spinnerUserCity.adapter = cityArrayAdapter

        update_user_profile_dialog_spinnerUserCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedCityIn = p2
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedCityIn = 0
                }
            }
        }
    }
}