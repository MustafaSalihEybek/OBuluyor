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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.obuluyor.R
import com.nexis.obuluyor.databinding.FragmentUpdateStoreDataBinding
import com.nexis.obuluyor.model.Store
import com.nexis.obuluyor.util.RealPathUtil
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.UpdateStoreDataViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_store.*
import kotlinx.android.synthetic.main.fragment_update_store_data.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdateStoreDataFragment : Fragment(), View.OnClickListener {
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
    private var logoPart: MultipartBody.Part? = null
    private var sliderPart: MultipartBody.Part? = null
    private lateinit var filePath: String

    private lateinit var v: View
    private lateinit var navDirections: NavDirections
    private lateinit var updateStoreBinding: FragmentUpdateStoreDataBinding
    private lateinit var updateStoreDataViewModel: UpdateStoreDataViewModel

    private lateinit var userIdBody: RequestBody
    private lateinit var storeNameBody: RequestBody
    private lateinit var storeDescriptionBody: RequestBody
    private lateinit var txtStoreName: String
    private lateinit var txtStoreDescription: String

    private lateinit var userStoreData: Store
    private var userId: Int = -1

    private fun init(){
        arguments?.let {
            userId = UpdateStoreDataFragmentArgs.fromBundle(it).userId
            userStoreData = UpdateStoreDataFragmentArgs.fromBundle(it).userStoreData

            updateStoreDataViewModel = ViewModelProvider(this).get(UpdateStoreDataViewModel::class.java)
            observeLiveData()
            updateStoreBinding.store = userStoreData

            resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK){
                    result.data?.let {
                        it.data?.let {
                            imgUri = it
                            filePath = RealPathUtil.getRealPath(v.context, imgUri)
                            imageBitmap = BitmapFactory.decodeFile(filePath)

                            if (selectedImage.equals("Logo")){
                                logoPath = filePath
                                update_store_data_fragment_imgLogo.setImageBitmap(imageBitmap)

                                imageFile = File(logoPath)
                                imageBody = imageFile.asRequestBody("multipart/form-data".toMediaType())
                                logoPart = MultipartBody.Part.createFormData("logo", imageFile.name, imageBody)
                            } else{
                                sliderPath = filePath
                                update_store_data_fragment_imgSlider.setImageBitmap(imageBitmap)

                                imageFile = File(sliderPath)
                                imageBody = imageFile.asRequestBody("multipart/form-data".toMediaType())
                                sliderPart = MultipartBody.Part.createFormData("magazaslider", imageFile.name, imageBody)
                            }
                        }
                    }
                }
            }

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)
            update_store_data_fragment_btnClose.setOnClickListener(this)
            update_store_data_fragment_btnUpdate.setOnClickListener(this)
            update_store_data_fragment_imgLogo.setOnClickListener(this)
            update_store_data_fragment_imgSlider.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateStoreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_store_data, container, false)
        return updateStoreBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        updateStoreDataViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        updateStoreDataViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)

                object : CountDownTimer(1000, 1000){
                    override fun onTick(p0: Long) {}

                    override fun onFinish() {
                        backToPage(userId, v)
                    }
                }.start()
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgClose -> backToPage(userId, v)
                R.id.update_store_data_fragment_btnClose -> backToPage(userId, v)
                R.id.update_store_data_fragment_btnUpdate -> updateUserData(userId)
                R.id.update_store_data_fragment_imgLogo -> checkGalleryPermission("Logo")
                R.id.update_store_data_fragment_imgSlider -> checkGalleryPermission("Slider")
            }
        }
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

    private fun updateUserData(userId: Int){
        txtStoreName = update_store_data_fragment_editStoreName.text.toString().trim()
        txtStoreDescription = update_store_data_fragment_editStoreDescription.text.toString().trim()

        if (!txtStoreName.isEmpty()){
            if (!txtStoreDescription.isEmpty()){
                if (!txtStoreName.equals(userStoreData.magazaadi)){
                    if (!txtStoreDescription.equals(userStoreData.aciklama)){
                        userIdBody = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                        storeNameBody = txtStoreName.toRequestBody("text/plain".toMediaTypeOrNull())
                        storeDescriptionBody = txtStoreDescription.toRequestBody("text/plain".toMediaTypeOrNull())

                        updateStoreDataViewModel.updateUserStoreData(userIdBody, storeNameBody, storeDescriptionBody, logoPart, sliderPart)
                    } else
                        txtStoreDescription.show(v, "Lütfen öncekinden farklı bir mağaza açıklaması belirleyiniz")
                } else
                    txtStoreName.show(v, "Lütfen öncekinden farklı bir mağaza adı belirleyiniz")
            } else
                txtStoreDescription.show(v, "Lütfen mağaza hakkında bir açıklama belirleyiniz")
        } else
            txtStoreName.show(v, "Lütfen mağaza için bir isim belirleyiniz")
    }

    private fun backToPage(userId: Int, v: View){
        navDirections = UpdateStoreDataFragmentDirections.actionUpdateStoreDataFragmentToProfileFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}