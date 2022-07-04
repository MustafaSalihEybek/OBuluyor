package com.nexis.obuluyor.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences {
    private var sharedPref: SharedPreferences
    private var sharedEdit: SharedPreferences.Editor
    private var mContext: Context

    constructor(mContext: Context){
        this.mContext = mContext
        sharedPref = mContext.getSharedPreferences("User", Context.MODE_PRIVATE)
        sharedEdit = sharedPref.edit()
    }

    fun saveUserId(userId: Int){
        sharedEdit.putInt("UserId", userId)
        sharedEdit.commit()
    }

    fun getSavedUserId() = sharedPref.getInt("UserId", -1)
}