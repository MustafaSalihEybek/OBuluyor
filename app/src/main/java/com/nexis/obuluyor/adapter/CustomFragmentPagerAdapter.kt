package com.nexis.obuluyor.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CustomFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var fragmentList: ArrayList<Fragment> = ArrayList()
    private var titleList: ArrayList<String> = ArrayList()

    override fun getCount() = fragmentList.size

    override fun getItem(position: Int) = fragmentList.get(position)

    fun addFragment(fragment: Fragment, title: String){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun getPageTitle(position: Int) = titleList.get(position)
}