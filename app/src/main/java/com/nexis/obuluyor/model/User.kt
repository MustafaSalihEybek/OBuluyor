package com.nexis.obuluyor.model

data class User(
    val Id: Int = 0,
    val ad_soyad: String?,
    val dogum_tarihi: String?,
    val cinsiyet: String?,
    val eposta: String?,
    val parola: String?,
    val telefon: String?,
    val gsm: String?,
    val il: String?,
    val ilce: String?,
    val kayit_tarihi: String?,
    val aktivasyon: Int = 0,
    val aktivasyonkodu: String?
)
