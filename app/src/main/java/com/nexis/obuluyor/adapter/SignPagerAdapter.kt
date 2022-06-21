package com.nexis.obuluyor.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SignPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getCount() = fragmentList.size

    override fun getItem(position: Int) = fragmentList.get(position)

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
    }
}