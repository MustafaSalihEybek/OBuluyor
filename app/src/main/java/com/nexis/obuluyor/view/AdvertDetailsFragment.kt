package com.nexis.obuluyor.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AdvertDetailsAdapter
import com.nexis.obuluyor.adapter.AdvertImagesPagerAdapter
import com.nexis.obuluyor.databinding.FragmentAddAdvertDetailsBinding
import com.nexis.obuluyor.databinding.FragmentAdvertDetailsBinding
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.model.AdvertDetail
import com.nexis.obuluyor.model.Category
import com.nexis.obuluyor.util.AppUtils
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_advert_details.*

class AdvertDetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var advertDetailsBinding: FragmentAdvertDetailsBinding
    private lateinit var navDirections: NavDirections

    private lateinit var categoryData: Category
    private lateinit var advertData: Advert
    private var userId: Int = -1
    private lateinit var imagesPagerAdapter: AdvertImagesPagerAdapter
    private lateinit var imageList: ArrayList<String?>
    private lateinit var advertDetailList: ArrayList<AdvertDetail>
    private lateinit var advertDetailsAdapter: AdvertDetailsAdapter

    private lateinit var buttonList: Array<Button>
    private var selectedBtnIn: Int = 0

    private fun init(){
        arguments?.let {
            categoryData = AdvertDetailsFragmentArgs.fromBundle(it).categoryData
            userId = AdvertDetailsFragmentArgs.fromBundle(it).userId
            advertData = AdvertDetailsFragmentArgs.fromBundle(it).advertData

            advertDetailsBinding.advert = advertData
            imageList = ArrayList()

            for (image in advertData.images)
                imageList.add(image.url)

            imagesPagerAdapter = AdvertImagesPagerAdapter(imageList, v.context)
            advert_details_fragment_viewPager.adapter = imagesPagerAdapter
            advert_details_fragment_pageIndicator.setViewPager(advert_details_fragment_viewPager)

            custom_toolbar_imgClose.visibility = View.VISIBLE
            custom_toolbar_imgClose.setOnClickListener(this)

            advert_details_fragment_recyclerView.setHasFixedSize(true)
            advert_details_fragment_recyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            advertDetailList = fillAdvertDetailList(advertData)
            advertDetailsAdapter = AdvertDetailsAdapter(advertDetailList)
            advert_details_fragment_recyclerView.adapter = advertDetailsAdapter

            advert_details_fragment_btnAdvertDetails.setOnClickListener(this)
            advert_details_fragment_btnAdvertContent.setOnClickListener(this)
            advert_details_fragment_btnAdvertLocation.setOnClickListener(this)

            buttonList = arrayOf(
                advert_details_fragment_btnAdvertDetails,
                advert_details_fragment_btnAdvertContent,
                advert_details_fragment_btnAdvertLocation
            )
            selectButton(0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        advertDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_advert_details, container, false)
        return advertDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.custom_toolbar_imgClose -> backToPage(userId, categoryData)
                R.id.advert_details_fragment_btnAdvertDetails -> selectButton(0)
                R.id.advert_details_fragment_btnAdvertContent -> selectButton(1)
                R.id.advert_details_fragment_btnAdvertLocation -> selectButton(2)
            }
        }
    }

    private fun backToPage(userId: Int, category: Category){
        navDirections = AdvertDetailsFragmentDirections.actionAdvertDetailsFragmentToAdvertsFragment(userId, category)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun fillAdvertDetailList(advert: Advert) : ArrayList<AdvertDetail> {
        advertDetailList = ArrayList()

        advertDetailList.add(AdvertDetail("Fiyat", AppUtils.getFormattedPrice(advert.price!!.toFloat())))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))
        /*advertDetailList.add(AdvertDetail("Yıl", ))
        advertDetailList.add(AdvertDetail("Yakıt", ))
        advertDetailList.add(AdvertDetail("Vites", ))
        advertDetailList.add(AdvertDetail("Km", ))
        advertDetailList.add(AdvertDetail("Kasa Tipi", ))
        advertDetailList.add(AdvertDetail("Motor Hacmi", ))
        advertDetailList.add(AdvertDetail("Motor Gücü", ))*/

        return advertDetailList
    }

    private fun selectButton(btnIn: Int){
        for (i in buttonList.indices){
            if (i == btnIn){
                buttonList.get(i).setBackgroundResource(R.drawable.advert_detail_btn_selected)
                buttonList.get(i).setTextColor(ContextCompat.getColor(v.context, R.color.advertDetailsSelectedBtnTxtColor))

                if (i == 0){
                    advert_details_fragment_recyclerView.visibility = View.VISIBLE
                } else {
                    advert_details_fragment_recyclerView.visibility = View.GONE
                }
            } else {
                buttonList.get(i).setBackgroundResource(R.drawable.advert_detail_btn_bg)
                buttonList.get(i).setTextColor(ContextCompat.getColor(v.context, R.color.advertDetailsBtnTxtColor))
            }
        }
    }
}