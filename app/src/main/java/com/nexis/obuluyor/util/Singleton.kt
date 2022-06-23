package com.nexis.obuluyor.util

import androidx.viewpager.widget.ViewPager

class Singleton {
    companion object{
        val BASE_URL: String = "https://obuluyor.com.tr/api/"
        val clientUsername: String = "mobil"
        val clientPassword: String = "mU!/-*+eSf30-54!_!Y"
        var mViewPager: ViewPager? = null

        fun setPage(pIn: Int){
            mViewPager?.let {
                it.currentItem = pIn
            }
        }
    }
}