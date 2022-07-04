package com.nexis.obuluyor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Advert(
    val Id: Int = 0,
    val uyeId: Int = 0,
    val phone: String?,
    val message: String?,
    val title: String?,
    val content: String?,
    val type: Int = 0,
    val price: String?,
    val exchange: String?,
    val cargoprice: String?,
    val cargoarrive: String?,
    val cargo: String?,
    val city: String?,
    val districts: String?,
    val locality: String?,
    val lat: String?,
    val lng: String?,
    val zoom: String?,
    val confirm: String?,
    val dates: String?,
    val category1: Int = 0,
    val category2: Int = 0,
    val category3: Int = 0,
    val category4: Int = 0,
    val category5: Int = 0,
    val category6: Int = 0,
    val category7: Int = 0,
    val category8: Int = 0,
    val category9: Int = 0,
    val category10: Int = 0,
    val yayin: Int = 0,
    val bitis: String?,
    val fiyat2: String?,
    val fiyat3: String?,
    val bitiszamani: String?,
    val odeme: String?,
    val firmadi: String?,
    val telefon: String?,
    val fax: String?,
    val gsm: String?,
    val web: String?,
    val vergidairesi: String?,
    val vergino: String?,
    val kurulusyili: String?,
    val isletmeturu: String?,
    val yetkili: String?,
    val ciro: String?,
    val calisansayisi: String?,
    val hakkinda: String?,
    val fadres: String?,
    val gosterim: Int = 0,
    val fiyatdustu: String?,
    val images: @RawValue List<Image>,
    val uyebilgisi: @RawValue User
) : Parcelable
