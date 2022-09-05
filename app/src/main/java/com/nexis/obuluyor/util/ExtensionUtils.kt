package com.nexis.obuluyor.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.nexis.obuluyor.R
import com.nexis.obuluyor.model.Advert
import com.nexis.obuluyor.model.Favorite
import com.nexis.obuluyor.model.SubCategory
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
    advert.images?.let {
        if (it.isNotEmpty())
            view.downloadImageUrl(it.get(0).url)
    }
}

@BindingAdapter("android:downloadFavoriteAdvertImage")
fun downloadFavoriteAdvertImage(view: ImageView, favorite: Favorite){
    favorite.images?.let {
        if (it.isNotEmpty())
            view.downloadImageUrl(it.get(it.size -1).url)
    }
}

@BindingAdapter("android:setAdvertPrice")
fun setAdvertPrice(view: TextView, advert: Advert){
    advert.price?.let {
        view.text = "${AppUtils.getFormattedPrice(it.toFloat())} ${advert.exchange}"
    }
}

@BindingAdapter("android:setFavoriteAdvertPrice")
fun setFavoriteAdvertPrice(view: TextView, favorite: Favorite){
    favorite.price?.let {
        view.text = "${AppUtils.getFormattedPrice(it.toFloat())} ${favorite.exchange}"
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

@BindingAdapter("android:setHintEditMessage")
fun setHintEditMessage(v: EditText, userName: String?){
    v.hint = "$userName adlı kullanıcıya bir mesaj gönder"
}

@BindingAdapter("android:setMessageDetailAdvertName")
fun setMessageDetailAdvertName(v: TextView, advertName: String?){
    v.text = "İlan: $advertName"
}

@BindingAdapter("android:setMessageDetailAdvertDate")
fun setMessageDetailAdvertDate(v: TextView, advertDate: String?){
    v.text = "Tarih: $advertDate"
}

@BindingAdapter("android:setMessageDetailAdvertSenderName")
fun setMessageDetailAdvertSenderName(v: TextView, userName: String?){
    v.text = "Gönderen: $userName"
}

@BindingAdapter("android:setAdvertModuleTitle")
fun setAdvertModuleTitle(v: TextView, moduleName: String?){
    v.text = "$moduleName Bilgisi *"
}

@BindingAdapter("android:setAdvertModuleContent")
fun setAdvertModuleContent(v: EditText, moduleName: String?){
    v.setHint("$moduleName bilgisini giriniz")
}

@BindingAdapter("android:setPropertyCheck")
fun setPropertyCheck(v: ImageView, isCheck: Int?){
    var checkBitmap: Bitmap

    if (isCheck != null){
        v.setBackgroundResource(R.drawable.check_bg)
        v.setImageResource(R.drawable.icon_check)
    } else {
        v.setBackgroundResource(R.drawable.uncheck_bg)
        v.setImageResource(R.drawable.icon_close)
    }

    v.setPadding(2)
}