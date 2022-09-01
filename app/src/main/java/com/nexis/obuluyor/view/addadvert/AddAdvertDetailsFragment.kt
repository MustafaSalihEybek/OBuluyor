package com.nexis.obuluyor.view.addadvert

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.CustomFragmentPagerAdapter
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.model.Module
import com.nexis.obuluyor.model.ModuleContent
import com.nexis.obuluyor.model.SubCategory
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddAdvertDetailsViewModel
import kotlinx.android.synthetic.main.custom_advert_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_advert_details.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class AddAdvertDetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var addAdvertDetailsViewModel: AddAdvertDetailsViewModel
    private var GPS_CODE: Int = 11
    private val REQUEST_CHECK_SETTINGS = 21

    private lateinit var pagerAdapter: CustomFragmentPagerAdapter
    private lateinit var categoryData: Category
    private lateinit var subCategoryList: Array<SubCategory>
    private var pageTitleList = arrayOf("İlan Özelliklerini Seçiniz", "İlan Özelliklerini Seçiniz", "İlan Bilgilerini Giriniz")
    private var userId: Int = -1

    private lateinit var editedCategoryList: ArrayList<Int>
    private lateinit var categoriesData: String
    private lateinit var moduleContentIdData: String
    private lateinit var moduleContentNameData: String
    private lateinit var moduleIdData: String
    private lateinit var moduleClassData: String

    private lateinit var advertId: RequestBody
    private var aIn: Int = 0

    private lateinit var locationRequest: LocationRequest

    private fun init(){
        arguments?.let {
            userId = AddAdvertDetailsFragmentArgs.fromBundle(it).userId
            categoryData = AddAdvertDetailsFragmentArgs.fromBundle(it).categoryData
            subCategoryList = AddAdvertDetailsFragmentArgs.fromBundle(it).subCategoryList

            pagerAdapter = CustomFragmentPagerAdapter(childFragmentManager)

            pagerAdapter.addFragment(AddAdvertDetailsPage1Fragment(userId, categoryData, subCategoryList), "Özellikler")
            pagerAdapter.addFragment(AddAdvertDetailsPage2Fragment(userId, categoryData, subCategoryList), "Özellikler")
            pagerAdapter.addFragment(AddAdvertDetailsPage3Fragment(), "Detaylar")

            add_advert_details_fragment_viewPager.adapter = pagerAdapter

            locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 5000
            locationRequest.fastestInterval = 2000

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

            addAdvertDetailsViewModel = ViewModelProvider(this).get(AddAdvertDetailsViewModel::class.java)
            observeLiveData()

            custom_advert_toolbar_txtTitle.text = pageTitleList.get(0)
            custom_advert_toolbar_imgClose.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_advert_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        add_advert_details_fragment_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                add_advert_details_fragment_txtProgress.text = "Temel bilgiler (${position + 1} / 3)"
                custom_advert_toolbar_txtTitle.text = pageTitleList.get(position)
                add_advert_details_fragment_progressBar.setProgress((position + 1))
                setAddAdvertBtn(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun observeLiveData(){
        addAdvertDetailsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addAdvertDetailsViewModel.advertData.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.Id?.let {
                    advertId = it.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    addAdvertDetailsViewModel.addAdvertNewImage(advertId, AddAdvertDetailsPage3Fragment.imagePartList.get(aIn))
                }
            }
        })

        addAdvertDetailsViewModel.addAdvertImage.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.Id != null){
                    if (aIn < (AddAdvertDetailsPage3Fragment.imagePartList.size - 1)){
                        aIn++
                        addAdvertDetailsViewModel.addAdvertNewImage(advertId, AddAdvertDetailsPage3Fragment.imagePartList.get(aIn))
                    } else {
                        "message".show(v, "İlan Başarıyla Eklendi")
                        backToMainPage(userId)
                    }
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_advert_toolbar_imgClose -> backToPage(userId, subCategoryList, categoryData)
                R.id.add_advert_details_fragment_btnAddAdvert -> addNewAdvert()
            }
        }
    }

    private fun addNewAdvert(){
        if (AddAdvertDetailsPage1Fragment.page1ModuleContentList.size > 0 && AddAdvertDetailsPage1Fragment.page1ModuleList.size > 0){
            if (AddAdvertDetailsPage3Fragment.imagePartList.size > 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(v.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions((v.context as Activity), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), GPS_CODE)
                    else {
                        if (isGPSEnabled()){
                            LocationServices.getFusedLocationProviderClient(v.context)
                                .requestLocationUpdates(locationRequest, object : LocationCallback(){
                                    override fun onLocationResult(p0: LocationResult) {
                                        LocationServices.getFusedLocationProviderClient(v.context)
                                            .removeLocationUpdates(this)

                                        if (p0.locations.size > 0){
                                            val lIn: Int = p0.locations.size - 1

                                            val lat: Double = p0.locations.get(lIn).latitude
                                            val lng: Double = p0.locations.get(lIn).longitude

                                            moduleContentIdData = getModuleContentData(AddAdvertDetailsPage1Fragment.page1ModuleContentList, true)
                                            moduleContentNameData = getModuleContentData(AddAdvertDetailsPage1Fragment.page1ModuleContentList, false)

                                            moduleIdData = getModuleData(AddAdvertDetailsPage1Fragment.page1ModuleList, true)
                                            moduleClassData = getModuleData(AddAdvertDetailsPage1Fragment.page1ModuleList, false)

                                            addAdvertDetailsViewModel.addNewAdvert(
                                                userId,
                                                1,
                                                null,
                                                AddAdvertDetailsPage3Fragment.advertTitle,
                                                AddAdvertDetailsPage3Fragment.advertContent,
                                                null,
                                                AddAdvertDetailsPage3Fragment.advertPrice,
                                                AddAdvertDetailsPage3Fragment.advertExchange,
                                                null,
                                                null,
                                                null,
                                                AddAdvertDetailsPage3Fragment.advertCountryIn,
                                                AddAdvertDetailsPage3Fragment.advertCityIn,
                                                4001,
                                                lat.toString(),
                                                lng.toString(),
                                                "14",
                                                0,
                                                AddAdvertDetailsPage3Fragment.advertDate,
                                                categoriesData,
                                                AddAdvertDetailsPage3Fragment.advertStream,
                                                AddAdvertDetailsPage3Fragment.advertIncreaseDate,
                                                null,
                                                null,
                                                AddAdvertDetailsPage3Fragment.advertFullDateWithTime,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                0,
                                                null,
                                                moduleContentIdData,
                                                moduleContentNameData,
                                                moduleIdData,
                                                moduleClassData,
                                                AddAdvertDetailsPage2Fragment.page2PropIdData,
                                                AddAdvertDetailsPage2Fragment.page2PropValData
                                            )
                                        }
                                    }
                                }, Looper.getMainLooper())
                        } else {
                            turnOnGPS()
                        }
                    }
                }
            }
        } else
            "mesaj".show(v, "Lütfen ilan hakkında biraz daha bilgi giriniz")
    }

    private fun isGPSEnabled() : Boolean {
        var locationManager: LocationManager? = null
        var isEnabled: Boolean = false

        if (locationManager == null){
            locationManager = v.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isEnabled
    }

    private fun turnOnGPS(){
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(v.context)
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.result
                "message".show(v, "GPS başarıyla açıldı")
            } catch (e: ApiException){
                when (e.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                v.context as Activity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (ex: SendIntentException) {
                            ex.printStackTrace()
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> "message".show(v, "Bu cihaz gps i desteklemiyor")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == GPS_CODE && grantResults.isNotEmpty() && grantResults.get(0) == PackageManager.PERMISSION_GRANTED)
            addNewAdvert()

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun backToPage(userId: Int, subCategoryList: Array<SubCategory>, category: Category){
        val subCategories: ArrayList<SubCategory> = ArrayList(subCategoryList.toMutableList())
        subCategories.removeAt(subCategoryList.size - 1)

        navDirections = AddAdvertDetailsFragmentDirections.actionAddAdvertDetailsFragmentToAddAdvertSelectSubCategoriesFragment(
            userId,
            subCategories.toTypedArray(),
            category
        )
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun setAddAdvertBtn(pIn: Int){
        if (pIn == 2){
            add_advert_details_fragment_btnAddAdvert.isClickable = true
            add_advert_details_fragment_btnAddAdvert.text = "İlanı Yükle"
            add_advert_details_fragment_btnAddAdvert.alpha = 1f

            add_advert_details_fragment_btnAddAdvert.setOnClickListener(this)
        } else {
            add_advert_details_fragment_btnAddAdvert.isClickable = false
            add_advert_details_fragment_btnAddAdvert.text = "Devam Et"
            add_advert_details_fragment_btnAddAdvert.alpha = 0.75f
        }
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

    private fun getModuleContentData(moduleContentList: ArrayList<ModuleContent>, dataId: Boolean) : String {
        var moduleData: String = ""

        for (mIn in moduleContentList.indices){
            if (dataId){
                if (moduleContentList.get(mIn).Id == null){
                    if (mIn == (moduleContentList.size - 1))
                        moduleData += "n"
                    else
                        moduleData += "n,"
                } else {
                    if (mIn == (moduleContentList.size - 1))
                        moduleData += "${moduleContentList.get(mIn).Id}"
                    else
                        moduleData += "${moduleContentList.get(mIn).Id},"
                }
            } else {
                if (mIn == (moduleContentList.size - 1))
                    moduleData += "${moduleContentList.get(mIn).name}"
                else
                    moduleData += "${moduleContentList.get(mIn).name},"
            }
        }

        return moduleData
    }

    private fun getModuleData(moduleList: ArrayList<Module>, dataId: Boolean) : String {
        var moduleData: String = ""

        for (mIn in moduleList.indices) {
            if (mIn == (moduleList.size - 1)){
                if (dataId)
                    moduleData += "${moduleList.get(mIn).Id}"
                else
                    moduleData += "${moduleList.get(mIn).classx}"
            } else {
                if (dataId)
                    moduleData += "${moduleList.get(mIn).Id},"
                else
                    moduleData += "${moduleList.get(mIn).classx},"
            }
        }

        return moduleData
    }

    private fun backToMainPage(userId: Int){
        navDirections = AddAdvertDetailsFragmentDirections.actionAddAdvertDetailsFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}