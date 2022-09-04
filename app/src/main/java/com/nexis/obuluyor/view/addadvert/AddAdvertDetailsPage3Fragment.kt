package com.nexis.obuluyor.view.addadvert

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AdvertImagesByGalleryPagerAdapter
import com.nexis.obuluyor.model.City
import com.nexis.obuluyor.model.Country
import com.nexis.obuluyor.model.District
import com.nexis.obuluyor.util.RealPathUtil
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertDetailsPage3ViewModel
import kotlinx.android.synthetic.main.fragment_add_advert_details_page3.*
import kotlinx.android.synthetic.main.update_user_profile_dialog.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddAdvertDetailsPage3Fragment : Fragment(), View.OnClickListener {
    private var IMG_CODE: Int = 11
    private lateinit var galleryIntent: Intent
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var clipData: ClipData
    private lateinit var imageBitmap: Bitmap
    private lateinit var filePath: String

    private lateinit var v: View
    private lateinit var pagerAdapter: AdvertImagesByGalleryPagerAdapter
    private lateinit var bitmapList: ArrayList<Bitmap>
    private lateinit var filePathList: ArrayList<String>

    private var advertTimeList = arrayOf(15, 30, 60, 90, 365)
    private lateinit var exchangeAdapter: ArrayAdapter<CharSequence>
    private lateinit var selectedExchange: String
    private lateinit var advertTimeAdapter: ArrayAdapter<CharSequence>
    private var selectedAdvertTime: Int = -1
    private lateinit var selectedIncreaseDate: String
    private lateinit var selectedDate: String
    private lateinit var fullDateWithTime: String

    private lateinit var countryList: List<Country>
    private lateinit var countryArrayAdapter: ArrayAdapter<*>
    private lateinit var countryNameList: ArrayList<String>
    private var selectedCountryIn: Int = -1

    private lateinit var cityArrayAdapter: ArrayAdapter<*>
    private lateinit var cityNameList: ArrayList<String>
    private lateinit var cityList: List<City>
    private var selectedCityIn: Int = -1

    private lateinit var districtArrayAdapter: ArrayAdapter<*>
    private lateinit var districtNameList: ArrayList<String>
    private lateinit var districtList: List<District>
    private var selectedDistrictIn: Int = -1

    private lateinit var advertPartList: ArrayList<MultipartBody.Part>
    private lateinit var imageFile: File
    private lateinit var imageBody: RequestBody
    private lateinit var imagePart: MultipartBody.Part

    private lateinit var addAdvertDetailsPage3ViewModel: AddAdvertDetailsPage3ViewModel
    private var isCreated: Boolean = false
    private var userManuelSelected: Boolean = false
    private lateinit var imageUriList: ArrayList<Uri>

    private fun init(){
        addAdvertDetailsPage3ViewModel = ViewModelProvider(this).get(AddAdvertDetailsPage3ViewModel::class.java)
        observeLiveData()

        if (!isCreated){
            addAdvertDetailsPage3ViewModel.getCountryList()

            Singleton.imageUriList = null
            Singleton.advertTitle = null
            Singleton.advertPrice = 0
            Singleton.advertContent = null
            Singleton.countryList = null
            Singleton.cityList = null
            Singleton.districtList = null

            advertTitle = null
            advertContent = null
            advertPrice = -1
            advertIncreaseDate = null
            advertExchange = null
            advertDate = null
            advertFullDateWithTime = null
            advertStream = null
            imagePartList = null
        }

        exchangeAdapter = ArrayAdapter.createFromResource(v.context, R.array.ExchangeList, R.layout.spinner_item)
        exchangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_advert_details_page3_fragment_spinnerExchange.adapter = exchangeAdapter

        if (isCreated){
            Singleton.exchangeIn?.let {
                add_advert_details_page3_fragment_spinnerExchange.setSelection(it, true)
            }

            advertExchange?.let {
                selectedExchange = it
            }
        }

        add_advert_details_page3_fragment_spinnerExchange.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedExchange = it.getItemAtPosition(p2).toString()
                    advertExchange = selectedExchange

                    Singleton.exchangeIn = p2
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedExchange = it.getItemAtPosition(0).toString()
                    advertExchange = selectedExchange

                    Singleton.exchangeIn = 0
                }
            }
        }

        advertTimeAdapter = ArrayAdapter.createFromResource(v.context, R.array.AdvertTimeList, R.layout.spinner_item)
        advertTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_advert_details_page3_fragment_spinnerAdvertTime.adapter = advertTimeAdapter

        if (isCreated){
            Singleton.advertTimeIn?.let {
                add_advert_details_page3_fragment_spinnerAdvertTime.setSelection(it, true)
                selectedAdvertTime = advertTimeList.get(it)
                setAdvertTime()
            }
        }

        add_advert_details_page3_fragment_spinnerAdvertTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedAdvertTime = advertTimeList.get(p2)
                    setAdvertTime()

                    Singleton.advertTimeIn = p2
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedAdvertTime = advertTimeList.get(0)
                    setAdvertTime()

                    Singleton.advertTimeIn = 0
                }
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                result?.data?.let {
                    if (it.clipData != null){
                        imageUriList = ArrayList()

                        for (u in 0 until it.clipData!!.itemCount)
                            imageUriList.add(it.clipData!!.getItemAt(u).uri)

                        setAdvertImages(imageUriList)
                    } else {
                        if (it.data != null){
                            imageUriList = ArrayList()
                            imageUriList.add(it.data!!)

                            setAdvertImages(imageUriList)
                        }
                    }

                    Singleton.imageUriList = imageUriList
                }
            }
        }

        add_advert_details_page3_fragment_relativeImages.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_advert_details_page3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        if (isCreated){
            Singleton.imageUriList?.let {
                setAdvertImages(it)
            }

            Singleton.advertTitle?.let {
                add_advert_details_page3_fragment_editAdvertTitle.setText(it)
            }

            if (Singleton.advertPrice != 0){
                add_advert_details_page3_fragment_editAdvertPrice.setText(Singleton.advertPrice.toString())
            }

            Singleton.advertContent?.let {
                add_advert_details_page3_fragment_editAdvertContent.setText(it)
            }

            Singleton.countryList?.let {
                countryList = it
                setCountry()
            }
        }

        add_advert_details_page3_fragment_editAdvertTitle.addTextChangedListener {
            advertTitle = it.toString()
            Singleton.advertTitle = advertTitle
        }

        add_advert_details_page3_fragment_editAdvertPrice.addTextChangedListener {
            if (it.toString().isNotEmpty()){
                if (it.toString().toLong() < 2147483647)
                    advertPrice = it.toString().toInt()
            }
            else
                advertPrice = -1

            Singleton.advertPrice = advertPrice
        }

        add_advert_details_page3_fragment_editAdvertContent.addTextChangedListener {
            advertContent = it.toString()
            Singleton.advertContent = advertContent
        }
    }

    private fun setAdvertTime(){
        selectedIncreaseDate = getIncreaseTime(selectedAdvertTime, true)
        selectedDate = getIncreaseTime(0, true)
        fullDateWithTime = getIncreaseTime(0, false)

        advertStream = selectedAdvertTime
        advertIncreaseDate = selectedIncreaseDate
        advertDate = selectedDate
        advertFullDateWithTime = fullDateWithTime
    }

    private fun setAdvertImages(imageUriList: ArrayList<Uri>){
        bitmapList = ArrayList()
        filePathList = ArrayList()
        advertPartList = ArrayList()

        add_advert_details_page3_fragment_relativeImages.visibility = View.GONE
        add_advert_details_page3_fragment_relativeViewPager.visibility = View.VISIBLE

        for (iIn in imageUriList.indices){
            filePath = RealPathUtil.getRealPath(v.context, imageUriList.get(iIn))
            filePathList.add(filePath)

            imageBitmap = BitmapFactory.decodeFile(filePath)
            bitmapList.add(imageBitmap)

            imageFile = File(filePath)
            imageBody = imageFile.asRequestBody("multipart/form-data".toMediaType())
            imagePart = MultipartBody.Part.createFormData("resim", imageFile.name, imageBody)
            advertPartList.add(imagePart)
        }

        imagePartList = advertPartList

        pagerAdapter = AdvertImagesByGalleryPagerAdapter(bitmapList, v.context)
        add_advert_details_page3_fragment_viewPager.adapter = pagerAdapter
        add_advert_details_page3_fragment_pageIndicator.setViewPager(add_advert_details_page3_fragment_viewPager)

        add_advert_details_page3_fragment_btnEditImages.visibility = View.VISIBLE
        add_advert_details_page3_fragment_btnEditImages.setOnClickListener(this)
    }

    private fun setCountry(){
        countryNameList = ArrayList()

        for (country in countryList)
            countryNameList.add(country.il_adi!!)

        countryArrayAdapter = ArrayAdapter(v.context, R.layout.spinner_item, countryNameList)
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_advert_details_page3_fragment_spinnerCountry.adapter = countryArrayAdapter

        if (isCreated){
            AddAdvertDetailsFragment.countryIn?.let {
                add_advert_details_page3_fragment_spinnerCountry.setSelection(it, true)
            }
        } else if (AddAdvertDetailsFragment.countryIn != null){
            add_advert_details_page3_fragment_spinnerCountry.setSelection(AddAdvertDetailsFragment.countryIn!!, true)

            selectedCountryIn = AddAdvertDetailsFragment.countryIn!!
            addAdvertDetailsPage3ViewModel.getCityList(countryList.get(selectedCountryIn).id)
        }

        add_advert_details_page3_fragment_spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedCountryIn = p2
                    addAdvertDetailsPage3ViewModel.getCityList(countryList.get(selectedCountryIn).id)

                    AddAdvertDetailsFragment.countryIn = selectedCountryIn
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedCountryIn = 0
                    addAdvertDetailsPage3ViewModel.getCityList(countryList.get(selectedCountryIn).id)

                    AddAdvertDetailsFragment.countryIn = selectedCountryIn
                }
            }
        }

        if (!isCreated)
            isCreated = true
    }

    private fun observeLiveData(){
        addAdvertDetailsPage3ViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addAdvertDetailsPage3ViewModel.countryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!isCreated){
                    countryList = it
                    setCountry()

                    Singleton.countryList = it
                }
            }
        })

        addAdvertDetailsPage3ViewModel.cityList.observe(viewLifecycleOwner, Observer {
            it?.let {
                cityList = it
                fillCityDataFromListAndSpinner(cityList)

                Singleton.cityList = it
            }
        })

        addAdvertDetailsPage3ViewModel.districtList.observe(viewLifecycleOwner, Observer {
            it?.let {
                districtList = it
                fillDistrictDataFromListAndSpinner(districtList)

                Singleton.districtList = it
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.add_advert_details_page3_fragment_relativeImages -> checkGalleryPermission()
                R.id.add_advert_details_page3_fragment_btnEditImages -> startGalleryIntent()
            }
        }
    }

    private fun checkGalleryPermission(){
        if (ContextCompat.checkSelfPermission(v.context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((v.context as Activity), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), IMG_CODE)
        else
            startGalleryIntent()
    }

    private fun startGalleryIntent() {
        galleryIntent = Intent()

        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryIntent.action = Intent.ACTION_GET_CONTENT

        resultLauncher.launch(Intent.createChooser(galleryIntent, "İlan Resimlerini Seç"))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == IMG_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startGalleryIntent()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun fillCityDataFromListAndSpinner(cityList: List<City>){
        cityNameList = ArrayList()

        for (city in cityList)
            cityNameList.add(city.county_adi!!)

        cityArrayAdapter = ArrayAdapter(v.context, R.layout.spinner_item, cityNameList)
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_advert_details_page3_fragment_spinnerCity.adapter = cityArrayAdapter

        if (isCreated){
            AddAdvertDetailsFragment.cityIn?.let {
                if (it < (cityNameList.size - 1))
                    add_advert_details_page3_fragment_spinnerCity.setSelection(it, true)
            }
        } else if (AddAdvertDetailsFragment.cityIn != null){
            if (AddAdvertDetailsFragment.cityIn!! < (cityNameList.size - 1)){
                add_advert_details_page3_fragment_spinnerCity.setSelection(AddAdvertDetailsFragment.cityIn!!, true)
                selectedCityIn = AddAdvertDetailsFragment.cityIn!!
                addAdvertDetailsPage3ViewModel.getDistrictList(cityList.get(selectedCityIn).id)
            }
        }

        add_advert_details_page3_fragment_spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedCityIn = p2
                    addAdvertDetailsPage3ViewModel.getDistrictList(cityList.get(selectedCityIn).id)

                    AddAdvertDetailsFragment.cityIn = selectedCityIn
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedCityIn = 0
                    addAdvertDetailsPage3ViewModel.getDistrictList(cityList.get(selectedCityIn).id)
                }
            }
        }
    }

    private fun fillDistrictDataFromListAndSpinner(districtList: List<District>){
        districtNameList = ArrayList()

        for (disctrict in districtList)
            districtNameList.add(disctrict.districtname!!)

        districtArrayAdapter = ArrayAdapter(v.context, R.layout.spinner_item, districtNameList)
        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_advert_details_page3_fragment_spinnerDistrict.adapter = districtArrayAdapter

        if (isCreated){
            AddAdvertDetailsFragment.districtIn?.let {
                if (it < (districtNameList.size - 1))
                    add_advert_details_page3_fragment_spinnerDistrict.setSelection(it, true)
            }
        } else if (AddAdvertDetailsFragment.districtIn != null){
            if (AddAdvertDetailsFragment.districtIn!! < (districtNameList.size - 1)){
                add_advert_details_page3_fragment_spinnerDistrict.setSelection(AddAdvertDetailsFragment.districtIn!!, true)
                selectedDistrictIn = AddAdvertDetailsFragment.districtIn!!
            }
        }

        add_advert_details_page3_fragment_spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedDistrictIn = p2
                    AddAdvertDetailsFragment.districtIn = selectedDistrictIn
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedDistrictIn = 0
                }
            }
        }
    }

    private fun getIncreaseTime(day: Int, isFullDate: Boolean) : String{
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"))
        val date: SimpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
        date.setTimeZone(TimeZone.getTimeZone("GMT+3"))
        cal.add(Calendar.DAY_OF_MONTH, day)
        val currentLocalTime = cal.time
        val localTime: String = date.format(currentLocalTime)

        if (isFullDate)
            return localTime.split(" ")[0]
        else
            return localTime
    }

    companion object{
        var advertTitle: String? = null
        var advertContent: String? = null
        var advertPrice: Int? = null
        var advertIncreaseDate: String? = null
        var advertExchange: String? = null
        var advertDate: String? = null
        var advertFullDateWithTime: String? = null
        var advertStream: Int? = null
        var imagePartList: ArrayList<MultipartBody.Part>? = null
    }
}