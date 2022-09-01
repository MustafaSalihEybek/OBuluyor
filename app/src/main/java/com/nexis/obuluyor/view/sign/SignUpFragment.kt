package com.nexis.obuluyor.view.sign

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nexis.obuluyor.R
import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*
import kotlin.collections.ArrayList

class SignUpFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var countryList: List<Country>
    private lateinit var countryNameList: ArrayList<String>
    private lateinit var cityList: List<City>
    private lateinit var cityNameList: ArrayList<String>
    private var selectedCountryIn: Int = -1
    private var selectedCityIn: Int = -1

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var dataSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var dateString: String
    private lateinit var calendar: Calendar
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    private lateinit var txtUserName: String
    private lateinit var txtUserSurname: String
    private lateinit var txtUserFullName: String
    private lateinit var txtUserBirthday: String
    private lateinit var txtSelectedGender: String
    private lateinit var genderArrayAdapter: ArrayAdapter<CharSequence>
    private lateinit var txtUserEmail: String
    private lateinit var txtUserPassword: String
    private lateinit var txtUserPhone: String
    private lateinit var txtUserGsm: String
    private lateinit var txtSelectedCountry: String
    private lateinit var countryArrayAdapter: ArrayAdapter<*>
    private lateinit var txtSelectedCity: String
    private lateinit var cityArrayAdapter: ArrayAdapter<*>

    private fun init(){
        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        observeLiveData()
        signUpViewModel.getCountries()

        genderArrayAdapter = ArrayAdapter.createFromResource(v.context, R.array.GenderList, R.layout.spinner_item)
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sign_up_fragment_spinnerUserGender.adapter = genderArrayAdapter

        sign_up_fragment_spinnerUserGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

        sign_up_fragment_btnSignUp.setOnClickListener(this)
        sign_up_fragment_btnSignIn.setOnClickListener(this)
        sign_up_fragment_editUserBirthday.setOnClickListener(this)

        initDatePicker()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        signUpViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        signUpViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
                goToSignInPage()
            }
        })

        signUpViewModel.countryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                countryList = it

                if (countryList.size > 0){
                    countryNameList = ArrayList()

                    for (country in countryList){
                        country.il_adi?.let {
                            countryNameList.add(it)
                        }
                    }

                    loadCountryListFromSpinner(countryNameList, countryList)
                }
            }
        })

        signUpViewModel.cityList.observe(viewLifecycleOwner, Observer {
            it?.let {
                cityList = it

                if (cityList.size > 0){
                    cityNameList = ArrayList()

                    for (city in cityList){
                        city.county_adi?.let {
                            cityNameList.add(it)
                        }
                    }

                    loadCityListFromSpinner(cityNameList)
                }
            }
        })
    }

    private fun loadCityListFromSpinner(cityNameList: ArrayList<String>){
        cityArrayAdapter = ArrayAdapter(v.context, R.layout.spinner_item, cityNameList)
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sign_up_fragment_spinnerUserCity.adapter = cityArrayAdapter

        sign_up_fragment_spinnerUserCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

    private fun loadCountryListFromSpinner(countryNameList: ArrayList<String>, countryList: List<Country>){
        countryArrayAdapter = ArrayAdapter(v.context, R.layout.spinner_item, countryNameList)
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sign_up_fragment_spinnerUserCountry.adapter = countryArrayAdapter

        sign_up_fragment_spinnerUserCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedCountryIn = getSelectedCountryId(it.getItemAtPosition(p2).toString(), countryNameList)

                    if (selectedCountryIn != -1)
                        signUpViewModel.getCities(countryList.get(selectedCountryIn).id)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedCountryIn = getSelectedCountryId(it.getItemAtPosition(0).toString(), countryNameList)

                    if (selectedCountryIn != -1)
                        signUpViewModel.getCities(selectedCountryIn)
                }
            }
        }
    }

    private fun getSelectedCountryId(countryName: String, countryNameList: ArrayList<String>) : Int {
        for (cIn in countryNameList.indices){
            if (countryNameList.get(cIn).equals(countryName))
                return cIn
        }

        return -1
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.sign_up_fragment_btnSignUp -> signUpUser()
                R.id.sign_up_fragment_btnSignIn -> goToSignInPage()
                R.id.sign_up_fragment_editUserBirthday -> datePickerDialog.show()
            }
        }
    }

    private fun initDatePicker(){
        dataSetListener = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
            dateString = makeDateString(day, (month + 1), year)
            sign_up_fragment_editUserBirthday.setText(dateString)
        }

        calendar = Calendar.getInstance()

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(v.context, dataSetListener, year, month, day)
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

    private fun goToSignInPage(){
        Singleton.setPage(0)
    }

    private fun signUpUser(){
        txtUserName = sign_up_fragment_editUserName.text.toString().trim()
        txtUserSurname = sign_up_fragment_editUserSurname.text.toString().trim()
        txtUserBirthday = sign_up_fragment_editUserBirthday.text.toString().trim()
        txtUserEmail = sign_up_fragment_editUserEmail.text.toString().trim()
        txtUserPassword = sign_up_fragment_editUserPassword.text.toString().trim()
        txtUserPhone = sign_up_fragment_editUserPhone.text.toString().trim()
        txtUserGsm = sign_up_fragment_editUserGsm.text.toString().trim()

        if (!txtUserName.isEmpty()){
            if (!txtUserSurname.isEmpty()){
                if (!txtUserBirthday.isEmpty()){
                    if (!txtSelectedGender.isEmpty()){
                        if (!txtUserEmail.isEmpty()){
                            if (!txtUserPassword.isEmpty()){
                                if (!txtUserPhone.isEmpty()){
                                    if (!txtUserGsm.isEmpty()){
                                        if (selectedCountryIn != -1){
                                            if (selectedCityIn != -1){
                                                txtUserFullName = "$txtUserName $txtUserSurname"
                                                AppUtils.mUser = User(
                                                    0,
                                                    txtUserFullName,
                                                    txtUserBirthday,
                                                    txtSelectedGender,
                                                    txtUserEmail,
                                                    txtUserPassword,
                                                    txtUserPhone,
                                                    txtUserGsm,
                                                    countryList.get(selectedCountryIn).id,
                                                    cityList.get(selectedCityIn).id,
                                                    null,
                                                    0,
                                                    null,
                                                    null
                                                )

                                                signUpViewModel.signUpUser(AppUtils.mUser)
                                            }
                                            else
                                                txtSelectedCity.show(v, "Lütfen listeden ilçenizi seçiniz")
                                        } else
                                            txtSelectedCountry.show(v, "Lütfen listeden şehrinizi seçiniz")
                                    } else
                                        txtUserGsm.show(v, "Lütfen telefon gsm numaranızı giriniz")
                                } else
                                    txtUserPhone.show(v, "Lütfen telefon numaranızı giriniz")
                            } else
                                txtUserPassword.show(v, "Lütfen geçerli bir şifre belirleyiniz")
                        } else
                            txtUserEmail.show(v, "Lütfen geçerli bir email adresi giriniz")
                    } else
                        txtSelectedGender.show(v, "Lütfen listeden cinsiyetinizi seçiniz")
                } else
                    txtUserBirthday.show(v, "Lütfen doğum gününüzü seçiniz")
            } else
                txtUserSurname.show(v, "Lütfen geçerli bir soyisim giriniz")
        } else
            txtUserName.show(v, "Lütfen geçerli bir isim giriniz")
    }
}