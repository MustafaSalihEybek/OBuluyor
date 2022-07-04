package com.nexis.obuluyor.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.nexis.obuluyor.R
import com.nexis.obuluyor.model.Advert
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun String.show(v: View, msg: String){
    Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
}

fun ImageView.downloadImageUrl(imageUrl: String?){
    val options = RequestOptions()
        .placeholder(placeHolderProgress(context))
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(imageUrl)
        .into(this)
}

fun placeHolderProgress(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadImg")
fun downloadImage(view: ImageView, url: String?){
    view.downloadImageUrl(url)
}

@BindingAdapter("android:downloadAdvertImage")
fun downloadAdvertImage(view: ImageView, advert: Advert){
    if (advert.images.size > 0)
        view.downloadImageUrl(advert.images.get(0).url)
}

@BindingAdapter("android:setAdvertPrice")
fun setAdvertPrice(view: TextView, advert: Advert){
    advert.price?.let {
        view.text = "${AppUtils.getFormattedPrice(it.toFloat())} ${advert.exchange}"
    }
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
