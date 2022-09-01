package com.nexis.obuluyor.view

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nexis.obuluyor.R
import com.nexis.obuluyor.adapter.AdvertDetailsAdapter
import com.nexis.obuluyor.adapter.AdvertImagesPagerAdapter
import com.nexis.obuluyor.adapter.PropertiesAdapter
import com.nexis.obuluyor.databinding.FragmentAdvertDetailsBinding
import com.nexis.obuluyor.model.*
import com.nexis.obuluyor.util.AppUtils
import com.nexis.obuluyor.util.Singleton
import com.nexis.obuluyor.util.show
import com.nexis.obuluyor.viewmodel.AdvertDetailsViewModel
import kotlinx.android.synthetic.main.fragment_advert_details.*


class AdvertDetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var advertDetailsBinding: FragmentAdvertDetailsBinding
    private lateinit var navDirections: NavDirections
    private lateinit var advertDetailsViewModel: AdvertDetailsViewModel

    private var advertList: Array<Advert>? = null
    private lateinit var userData: User
    private var isComeBySearch: Boolean = false
    private var subCategoryList: Array<SubCategory>? = null
    private var categoryData: Category? = null
    private lateinit var advertData: Advert
    private var userId: Int = -1
    private lateinit var imagesPagerAdapter: AdvertImagesPagerAdapter
    private lateinit var imageList: ArrayList<String?>
    private lateinit var advertDetailList: ArrayList<AdvertDetail>
    private lateinit var advertDetailsAdapter: AdvertDetailsAdapter

    private lateinit var buttonList: Array<Button>

    private lateinit var favoriteList: List<Favorite>
    private var favoriteId: Int = -1

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    private lateinit var propList: List<Prop>
    private lateinit var propDataList: List<PropData>
    private lateinit var groupTitleList: List<GroupTitle>
    private lateinit var propsIdData: String
    private lateinit var groupsIdData: String
    private lateinit var filteredGroupList: ArrayList<GroupTitle>

    private lateinit var propertiesAdapter: PropertiesAdapter

    private fun init(){
        arguments?.let {
            categoryData = AdvertDetailsFragmentArgs.fromBundle(it).categoryData
            userId = AdvertDetailsFragmentArgs.fromBundle(it).userId
            advertData = AdvertDetailsFragmentArgs.fromBundle(it).advertData
            subCategoryList = AdvertDetailsFragmentArgs.fromBundle(it).subCategoryList
            isComeBySearch = AdvertDetailsFragmentArgs.fromBundle(it).isComeBySearch
            advertList = AdvertDetailsFragmentArgs.fromBundle(it).advertList

            advertDetailsBinding.advert = advertData
            imageList = ArrayList()

            advertData.images?.let {
                for (image in it)
                    imageList.add(image.url)
            }

            advertData.content?.let {
                advert_details_fragment_txtContent.setHtml(it)
            }

            imagesPagerAdapter = AdvertImagesPagerAdapter(imageList, v.context)
            advert_details_fragment_viewPager.adapter = imagesPagerAdapter
            advert_details_fragment_pageIndicator.setViewPager(advert_details_fragment_viewPager)

            advert_details_fragment_imgClose.visibility = View.VISIBLE
            advert_details_fragment_imgClose.setOnClickListener(this)

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

            advertDetailsViewModel = ViewModelProvider(this).get(AdvertDetailsViewModel::class.java)
            observeLiveData()

            if (userId != -1){
                advert_details_fragment_imgHollowStar.setOnClickListener(this)
                advert_details_fragment_imgFilledStar.setOnClickListener(this)

                advertDetailsViewModel.getFavorites(userId)
                advertDetailsViewModel.getUserData(userId)
            }

            advertDetailsViewModel.getProps(advertData.Id!!)

            mapFragment = childFragmentManager.findFragmentById(R.id.advert_details_fragment_map) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                mMap = googleMap
                selectButton(0)

                var latValue: Double = 0.0
                var lngValue: Double = 0.0
                var mapZoom: Float = 14f

                advertData.lat?.let {
                    latValue = it.toDouble()
                }

                advertData.lng?.let {
                    lngValue = it.toDouble()
                }

                advertData.zoom?.let {
                    mapZoom = it.toFloat()
                }

                val mapLocation: LatLng = LatLng(latValue, lngValue)
                mMap.addMarker(MarkerOptions().position(mapLocation).title("İlan Konumu"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation, mapZoom))
            }
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

    private fun observeLiveData(){
        advertDetailsViewModel.sendMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        advertDetailsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        advertDetailsViewModel.favoriteList.observe(viewLifecycleOwner, Observer {
            it?.let {
                favoriteList = it

                if (checkFavorite(favoriteList, advertData)) {
                    advert_details_fragment_imgHollowStar.visibility = View.GONE
                    advert_details_fragment_imgFilledStar.visibility = View.VISIBLE
                } else {
                    advert_details_fragment_imgFilledStar.visibility = View.GONE
                    advert_details_fragment_imgHollowStar.visibility = View.VISIBLE
                }
            }
        })

        advertDetailsViewModel.favoriteMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
                advertDetailsViewModel.getFavorites(userId)
            }
        })

        advertDetailsViewModel.userData.observe(viewLifecycleOwner, Observer {
            it?.let {
                userData = it
                advert_details_fragment_linearSendMessage.setOnClickListener(this)
            }
        })

        advertDetailsViewModel.propList.observe(viewLifecycleOwner, Observer {
            it?.let {
                propList = it
                propsIdData = getPropsIdData(propList)

                advertDetailsViewModel.getPropsData(propsIdData)
            }
        })

        advertDetailsViewModel.propDataList.observe(viewLifecycleOwner , Observer {
            it?.let {
                propDataList = it
                groupsIdData = getGroupsIdData(propDataList)

                advertDetailsViewModel.getGroupTitles(groupsIdData)
            }
        })

        advertDetailsViewModel.groupTitleList.observe(viewLifecycleOwner, Observer {
            it?.let {
                groupTitleList = it
                filteredGroupList = getGroupNameList(groupTitleList)

                var txtGroupTitle: TextView
                var propertyRecyclerView: RecyclerView
                var layoutParams: LinearLayout.LayoutParams

                for (gIn in filteredGroupList.indices){
                    txtGroupTitle = TextView(v.context)
                    txtGroupTitle.text = filteredGroupList.get(gIn).groupName
                    txtGroupTitle.typeface = ResourcesCompat.getFont(v.context, R.font.poppins_bold)
                    txtGroupTitle.textSize = 15f
                    txtGroupTitle.setTextColor(AppUtils.getColorValue(R.attr.advertDetailsPageTxtTitleColor, v.context))
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    if (gIn == 0)
                        layoutParams.setMargins(0, 10, 0, 10)
                    else
                        layoutParams.setMargins(0, 35, 0, 10)

                    advert_details_fragment_propertyLinear.addView(txtGroupTitle, layoutParams)

                    propertyRecyclerView = RecyclerView(v.context)
                    propertyRecyclerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    propertyRecyclerView.setHasFixedSize(true)
                    propertyRecyclerView.layoutManager = GridLayoutManager(v.context, 3)
                    propertiesAdapter = PropertiesAdapter(getFilteredPropsData(propDataList, propList, filteredGroupList.get(gIn).Id!!))
                    propertyRecyclerView.adapter = propertiesAdapter

                    advert_details_fragment_propertyLinear.addView(propertyRecyclerView)
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.advert_details_fragment_imgClose -> backToPage(userId, categoryData, subCategoryList, isComeBySearch, advertList)
                R.id.advert_details_fragment_btnAdvertDetails -> selectButton(0)
                R.id.advert_details_fragment_btnAdvertContent -> selectButton(1)
                R.id.advert_details_fragment_btnAdvertLocation -> selectButton(2)
                R.id.advert_details_fragment_imgHollowStar -> addAdvertToFavorite(userId, advertData.Id!!)
                R.id.advert_details_fragment_imgFilledStar -> removeAdvertToFavorite(userId, favoriteList, advertData)
                R.id.advert_details_fragment_linearSendMessage -> Singleton.showSendMessageDialog(v, advertDetailsViewModel, userData, advertData.uyebilgisi!!, advertData.Id!!)
            }
        }
    }

    private fun addAdvertToFavorite(userId: Int, advertId: Int){
        advertDetailsViewModel.addFavorite(userId, advertId)
    }

    private fun removeAdvertToFavorite(userId: Int, favoriteList: List<Favorite>, advertData: Advert){
        favoriteId = getFavoriteId(favoriteList, advertData)

        if (favoriteId != -1)
            advertDetailsViewModel.removeFavorite(userId, advertData.Id!!, favoriteId)
        else
            "message".show(v, "İlan favorilerde bulunamadı")
    }

    private fun checkFavorite(favoriteList: List<Favorite>, advertData: Advert) : Boolean {
        for (favorite in favoriteList){
            if (favorite.favoriilanId.equals(advertData.Id))
                return true
        }

        return false
    }

    private fun getFavoriteId(favoriteList: List<Favorite>, advertData: Advert) : Int {
        for (favorite in favoriteList){
            if(favorite.favoriilanId.equals(advertData.Id))
                return favorite.favoriId
        }

        return -1
    }

    private fun backToPage(userId: Int, category: Category?, subCategoryList: Array<SubCategory>?, isComeBySearch: Boolean, advertList: Array<Advert>?){
        if (isComeBySearch)
            navDirections = AdvertDetailsFragmentDirections.actionAdvertDetailsFragmentToAdvertsBySearchResultFragment(advertList!!, userId)
        else {
            if (category == null && subCategoryList == null)
                navDirections = AdvertDetailsFragmentDirections.actionAdvertDetailsFragmentToMainFragment(userId)
            else
                navDirections = AdvertDetailsFragmentDirections.actionAdvertDetailsFragmentToAdvertsFragment(userId, category!!, subCategoryList!!)
        }

        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun fillAdvertDetailList(advert: Advert) : ArrayList<AdvertDetail> {
        advertDetailList = ArrayList()

        advertDetailList.add(AdvertDetail("İlan", advert.title))
        advertDetailList.add(AdvertDetail("Fiyat", "${AppUtils.getFormattedPrice(advert.price!!.toFloat())} ${advert.exchange}"))
        advertDetailList.add(AdvertDetail("İlan Tarihi", advert.dates))
        advertDetailList.add(AdvertDetail("İlan No", advert.Id.toString()))

        advert.properties?.let {
            if (it.isNotEmpty()) {
                for (property in it)
                    advertDetailList.add(AdvertDetail(property.title, property.content))
            }
        }

        return advertDetailList
    }

    private fun selectButton(btnIn: Int){
        for (i in buttonList.indices){
            if (i == btnIn){
                buttonList.get(i).setBackgroundResource(R.drawable.advert_detail_btn_selected)
                buttonList.get(i).setTextColor(ContextCompat.getColor(v.context, R.color.white))

                if (i == 0){
                    advert_details_fragment_recyclerView.visibility = View.VISIBLE
                    advert_details_fragment_txtContent.visibility = View.VISIBLE
                    advert_details_fragment_txtInformation.visibility = View.VISIBLE
                    advert_details_fragment_propertyLinear.visibility = View.GONE
                    mapFragment.view?.visibility = View.GONE
                } else if (i == 1) {
                    advert_details_fragment_propertyLinear.visibility = View.VISIBLE
                    advert_details_fragment_recyclerView.visibility = View.GONE
                    advert_details_fragment_txtInformation.visibility = View.GONE
                    advert_details_fragment_txtContent.visibility = View.GONE
                    mapFragment.view?.visibility = View.GONE
                } else if (i == 2) {
                    mapFragment.view?.visibility = View.VISIBLE
                    advert_details_fragment_txtContent.visibility = View.GONE
                    advert_details_fragment_recyclerView.visibility = View.GONE
                    advert_details_fragment_propertyLinear.visibility = View.GONE
                    advert_details_fragment_txtInformation.visibility = View.GONE
                }
            } else {
                buttonList.get(i).setBackgroundResource(R.drawable.advert_detail_btn_bg)

                if (Singleton.themeMode.equals("Dark"))
                    buttonList.get(i).setTextColor(ContextCompat.getColor(v.context, R.color.white))
                else
                    buttonList.get(i).setTextColor(Color.parseColor("#303030"))
            }
        }
    }

    private fun getPropsIdData(propList: List<Prop>) : String {
        var propsId: String = ""

        for (pIn in propList.indices){
            if (pIn == (propList.size - 1))
                propsId += "${propList.get(pIn).propId}"
            else
                propsId += "${propList.get(pIn).propId},"
        }

        return propsId
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

    private fun getFilteredPropsData(propDataList: List<PropData>, propList: List<Prop>, groupId: Int) : Pair<ArrayList<PropData>, ArrayList<Prop>> {
        val propsData: ArrayList<PropData> = ArrayList()
        val props: ArrayList<Prop> = ArrayList()

        for (pIn in propDataList.indices){
            propDataList.get(pIn).groupId?.let {
                if (it == groupId){
                    propsData.add(propDataList.get(pIn))
                    props.add(propList.get(pIn))
                }
            }
        }

        return Pair(propsData, props)
    }
}