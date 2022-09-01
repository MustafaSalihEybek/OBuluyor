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

    private lateinit var advertPartList: ArrayList<MultipartBody.Part>
    private lateinit var imageFile: File
    private lateinit var imageBody: RequestBody
    private lateinit var imagePart: MultipartBody.Part

    private lateinit var addAdvertDetailsPage3ViewModel: AddAdvertDetailsPage3ViewModel
    private var isCreated: Boolean = false
    private var oneTimeSelect: Boolean = false

    private fun init(){
        addAdvertDetailsPage3ViewModel = ViewModelProvider(this).get(AddAdvertDetailsPage3ViewModel::class.java)
        observeLiveData()

        if (!isCreated)
            addAdvertDetailsPage3ViewModel.getCountryList()

        exchangeAdapter = ArrayAdapter.createFromResource(v.context, R.array.ExchangeList, R.layout.spinner_item)
        exchangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        add_advert_details_page3_fragment_spinnerExchange.adapter = exchangeAdapter

        if (isCreated){
            add_advert_details_page3_fragment_spinnerExchange.setSelection(Singleton.exchangeIn, true)
            selectedExchange = advertExchange
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
            add_advert_details_page3_fragment_spinnerAdvertTime.setSelection(Singleton.advertTimeIn, true)
            selectedAdvertTime = advertTimeList.get(Singleton.advertTimeIn)
            setAdvertTime()
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
                    it.clipData?.let {
                        clipData = it
                        setAdvertImages(it)

                        Singleton.clipData = it
                    }
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
            Singleton.clipData?.let {
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

            countryList = Singleton.countryList
            setCountry()
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

    private fun setAdvertImages(clipData: ClipData){
        bitmapList = ArrayList()
        filePathList = ArrayList()
        advertPartList = ArrayList()

        add_advert_details_page3_fragment_relativeImages.visibility = View.GONE
        add_advert_details_page3_fragment_relativeViewPager.visibility = View.VISIBLE

        for (iIn in 0 until clipData.itemCount){
            filePath = RealPathUtil.getRealPath(v.context, clipData.getItemAt(iIn).uri)
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

        if (isCreated)
            add_advert_details_page3_fragment_spinnerCountry.setSelection(advertCountryIn, true)

        add_advert_details_page3_fragment_spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedCountryIn = p2
                    addAdvertDetailsPage3ViewModel.getCityList(countryList.get(selectedCountryIn).id)

                    advertCountryIn = selectedCountryIn
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedCountryIn = 0
                    addAdvertDetailsPage3ViewModel.getCityList(countryList.get(selectedCountryIn).id)

                    advertCountryIn = selectedCountryIn
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
            add_advert_details_page3_fragment_spinnerCity.setSelection(advertCityIn, true)
        }

        add_advert_details_page3_fragment_spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedCityIn = p2
                    advertCityIn = selectedCityIn
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedCityIn = 0
                    advertCityIn = selectedCityIn
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
        var advertTitle: String = ""
        var advertContent: String = ""
        var advertPrice: Int = -1
        var advertIncreaseDate: String = ""
        var advertExchange: String = ""
        var advertDate: String = ""
        var advertFullDateWithTime: String = ""
        var advertCountryIn: Int = -1
        var advertCityIn: Int = -1
        var advertStream: Int = -1
        var imagePartList: ArrayList<MultipartBody.Part> = arrayListOf()
    }
}