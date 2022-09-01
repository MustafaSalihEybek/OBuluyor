package com.nexis.obuluyor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class Store(
    val Id: Int?,
    val uyeId: Int?,
    val onay: Int?,
    val magazaadi: String?,
    val adres: String?,
    val aciklama: String?,
    val kategori: Int?,
    val sure: Int?,
    val bitis: Date?,
    val odemeturu: String?,
    val fiyat: String?,
    val logo: String?,
    val magazaslider: String?,
    val magazakota: Int?,
    val logoUrl: String?,
    val sliderUrl: String?,
    val user: @RawValue User?,
    val advertCount: Int?
) : Parcelable
