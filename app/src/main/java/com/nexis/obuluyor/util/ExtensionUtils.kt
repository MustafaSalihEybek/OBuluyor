package com.nexis.obuluyor.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun String.show(v: View, msg: String){
    Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
}

@BindingAdapter("android:setCategoryImage")
fun setCategoryImage(v: ImageView, categoryName: String?){
    categoryName?.let {
        val imageBitmap: Bitmap = BitmapFactory.decodeResource(
            v.resources,
            AppUtils.getCategoryMap().getValue(
                categoryName.trim()
            )
        )
        v.setImageBitmap(imageBitmap)
    }
}

@BindingAdapter("android:setAdvertCategory")
fun setAdvertCategory(v: TextView, categoryName: String?){
    categoryName?.let {
        v.text = "KATEGORİLER > $it"
    }
}
