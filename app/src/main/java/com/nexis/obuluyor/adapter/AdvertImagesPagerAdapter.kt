package com.nexis.obuluyor.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.nexis.obuluyor.util.downloadImageUrl

class AdvertImagesPagerAdapter(val imageUrlList: ArrayList<String?>, val mContext: Context) : PagerAdapter() {
    private lateinit var imgPage: ImageView

    override fun getCount() = imageUrlList.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        imgPage = ImageView(mContext)
        imgPage.downloadImageUrl(imageUrlList.get(position))

        container.addView(imgPage)
        return imgPage
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}