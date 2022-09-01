package com.nexis.obuluyor.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.util.RealPathUtil
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AddStoreViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_store.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddStoreFragment : Fragment(), View.OnClickListener {
    private var IMG_CODE: Int = 11
    private lateinit var galleryIntent: Intent
    private lateinit var imageBitmap: Bitmap
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var imgUri: Uri
    private lateinit var selectedImage: String
    private lateinit var logoPath: String
    private lateinit var sliderPath: String
    private lateinit var imageFile: File
    private lateinit var imageBody: RequestBody
    private lateinit var logoPart: MultipartBody.Part
    private lateinit var sliderPart: MultipartBody.Part
    private lateinit var filePath: String

    private lateinit var userIdBody: RequestBody
    private lateinit var storeNameBody: RequestBody
    private lateinit var storeAddressBody: RequestBody
    private lateinit var storeDescriptionBody: RequestBody
    private lateinit var categoryIdBody: RequestBody
    private lateinit var timeBody: RequestBody
    private lateinit var timeEndBody: RequestBody
    private lateinit var paymentTypeBody: RequestBody
    private lateinit var priceBody: RequestBody
    private lateinit var storeQuotaBody: RequestBody

    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var addStoreViewModel: AddStoreViewModel

    private lateinit var txtStoreName: String
    private lateinit var txtStoreAddress: String
    private lateinit var txtStoreDescription: String
    private var selectedPaymentType: String? = null
    private var selectedDate: String? = null
    private var selectedMonth: Int = 0
    private var selectedPaymentInfoIn: Int = -1
    private var userId: Int = -1

    private lateinit var paymentTypeAdapter: ArrayAdapter<CharSequence>
    private lateinit var storeMonthOfDayAdapter: ArrayAdapter<CharSequence>
    private lateinit var storePaymentInfoList: ArrayAdapter<*>
    private lateinit var advertAmountList: Array<Int>
    private lateinit var advertPriceList: Array<Int>
    private lateinit var storeInfoList: ArrayList<String>

    private fun init(){
        arguments?.let {
            userId = AddStoreFragmentArgs.fromBundle(it).userId

            addStoreViewModel = ViewModelProvider(this).get(AddStoreViewModel::class.java)
            observeLiveData()
            addStoreViewModel.getStorePaymentInfo()

            resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK){
                    result.data?.let {
                        it.data?.let {
                            imgUri = it
                            filePath = RealPathUtil.getRealPath(v.context, imgUri)
                            imageBitmap = BitmapFactory.decodeFile(filePath)

                            if (selectedImage.equals("Logo")){
                                logoPath = filePath
                                add_store_fragment_imgLogo.setImageBitmap(imageBitmap)

                                imageFile = File(logoPath)
                                imageBody = imageFile.asRequestBody("multipart/form-data".toMediaType())
                                logoPart = MultipartBody.Part.createFormData("logo", imageFile.name, imageBody)
                            } else{
                                sliderPath = filePath
                                add_store_fragment_imgSlider.setImageBitmap(imageBitmap)

                                imageFile = File(sliderPath)
                                imageBody = imageFile.asRequestBody("multipart/form-data".toMediaType())
                                sliderPart = MultipartBody.Part.createFormData("magazaslider", imageFile.name, imageBody)
                            }
                        }
                    }
                }
            }

            paymentTypeAdapter = ArrayAdapter.createFromResource(v.context, R.array.PaymentTypeList, R.layout.spinner_item)
            paymentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            add_store_fragment_spinnerPaymentType.adapter = paymentTypeAdapter

            add_store_fragment_spinnerPaymentType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    p0?.let {
                        selectedPaymentType = it.getItemAtPosition(p2).toString()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    p0?.let {
                        selectedPaymentType = it.getItemAtPosition(0).toString()
                    }
                }
            }

            storeMonthOfDayAdapter = ArrayAdapter.createFromResource(v.context, R.array.StoreMonthOfDayList, R.layout.spinner_item)
            storeMonthOfDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            add_store_fragment_spinnerStoreMonthOfDay.adapter = storeMonthOfDayAdapter

            add_store_fragment_spinnerStoreMonthOfDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    p0?.let {
                        if (p2 < 2)
                            selectedMonth = ((p2 + 1) * 3)
                        else
                            selectedMonth = 12

                        selectedDate = getIncreaseTime(selectedMonth)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    p0?.let {
                        selectedMonth = 3
                        selectedDate = getIncreaseTime(selectedMonth)
                    }
                }
            }

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)
            add_store_fragment_imgLogo.setOnClickListener(this)
            add_store_fragment_imgSlider.setOnClickListener(this)
            add_store_fragment_btnAdd.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(it.id){
                R.id.custom_toolbar_imgClose -> backToPage(userId)
                R.id.add_store_fragment_btnAdd -> addStore(userId)
                R.id.add_store_fragment_imgLogo -> checkGalleryPermission("Logo")
                R.id.add_store_fragment_imgSlider -> checkGalleryPermission("Slider")
            }
        }
    }

    private fun getIncreaseTime(month: Int) : String{
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"))
        val date: SimpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
        date.setTimeZone(TimeZone.getTimeZone("GMT+3"))
        cal.add(Calendar.MONTH, month)
        val currentLocalTime = cal.time
        val localTime: String = date.format(currentLocalTime)
        val fullDateStr = localTime.split(" ")[0]

        return fullDateStr
    }

    private fun checkGalleryPermission(imageName: String){
        selectedImage = imageName

        if (ContextCompat.checkSelfPermission(v.context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((v.context as Activity), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), IMG_CODE)
        else
            startGalleryIntent()
    }

    private fun startGalleryIntent(){
        galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(galleryIntent)
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

    private fun observeLiveData(){
        addStoreViewModel.errorMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                it.show(v, it)
            }
        })

        addStoreViewModel.successMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                it.show(v, it)

                object : CountDownTimer(1000, 1000){
                    override fun onTick(p0: Long) {}

                    override fun onFinish() {
                        backToPage(userId)
                    }
                }.start()
            }
        })

        addStoreViewModel.storePaymentInfo.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                advertAmountList = arrayOf(it.kota1!!, it.kota2!!, it.kota3!!)
                advertPriceList = arrayOf(it.a1!!, it.a2!!, it.a3!!)
                storeInfoList = ArrayList()

                for (aIn in advertAmountList.indices)
                    storeInfoList.add("${advertAmountList.get(aIn)} kota ${advertPriceList.get(aIn)} TL")

                storePaymentInfoList = ArrayAdapter(v.context, R.layout.spinner_item, storeInfoList)
                storePaymentInfoList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                add_store_fragment_spinnerStorePaymentInfo.adapter = storePaymentInfoList

                add_store_fragment_spinnerStorePaymentInfo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        p0?.let {
                            selectedPaymentInfoIn = p2
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        p0?.let {
                            selectedPaymentInfoIn = 0
                        }
                    }
                }
            }
        })
    }

    private fun backToPage(userId: Int){
        navDirections = AddStoreFragmentDirections.actionAddStoreFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun addStore(userId: Int){
        txtStoreName = add_store_fragment_editStoreName.text.toString().trim()
        txtStoreAddress = add_store_fragment_editStoreAddress.text.toString().trim()
        txtStoreDescription = add_store_fragment_editStoreDescription.text.toString().trim()

        if (!logoPath.isNullOrEmpty()){
            if (!sliderPath.isNullOrEmpty()){
                if (!txtStoreName.isEmpty()){
                    if (!txtStoreAddress.isEmpty()){
                        if (!txtStoreDescription.isEmpty()){
                            if (!selectedPaymentType.isNullOrEmpty()){
                                if (!selectedDate.isNullOrEmpty()){
                                    if (selectedPaymentInfoIn != -1){
                                        userIdBody = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                                        storeNameBody = txtStoreName.toRequestBody("text/plain".toMediaTypeOrNull())
                                        storeAddressBody = txtStoreAddress.toRequestBody("text/plain".toMediaTypeOrNull())
                                        storeDescriptionBody = txtStoreDescription.toRequestBody("text/plain".toMediaTypeOrNull())
                                        categoryIdBody = "0".toRequestBody("text/plain".toMediaTypeOrNull())
                                        timeBody = selectedMonth.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                                        timeEndBody = selectedDate!!.toRequestBody("text/plain".toMediaTypeOrNull())
                                        paymentTypeBody = selectedPaymentType!!.toRequestBody("text/plain".toMediaTypeOrNull())
                                        priceBody = advertPriceList.get(selectedPaymentInfoIn).toString().toRequestBody("text/plain".toMediaTypeOrNull())
                                        storeQuotaBody = advertAmountList.get(selectedPaymentInfoIn).toString().toRequestBody("text/plain".toMediaTypeOrNull())

                                        addStoreViewModel.addStore(
                                            userIdBody,
                                            storeNameBody,
                                            storeAddressBody,
                                            storeDescriptionBody,
                                            categoryIdBody,
                                            timeBody,
                                            timeEndBody,
                                            paymentTypeBody,
                                            priceBody,
                                            logoPart,
                                            sliderPart,
                                            storeQuotaBody
                                        )
                                    }else
                                        "message".show(v, "Lütfen listeden kota miktarını seçiniz")
                                }else
                                    "message".show(v, "Lütfen listeden süreyi seçiniz")
                            }else
                                "message".show(v, "Lütfen listeden ödeme türünü seçiniz")
                        }else
                            txtStoreDescription.show(v, "Lütfen mağaza hakkında bir açıklama belirleyiniz")
                    }else
                        txtStoreAddress.show(v, "Lütfen mağaza için bir url adresi belirleyiniz")
                }else
                    txtStoreName.show(v, "Lütfen mağaza için bir isim belirleyiniz")
            }else
                "message".show(v, "Lütfen mağaza için bir slider seçiniz")
        }else
            "message".show(v, "Lütfen mağaza için bir logo seçiniz")
    }
}