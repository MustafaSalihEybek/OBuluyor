package com.nexis.obuluyor.model

data class Store(
    val Id: Int = 0,
    val uyeId: Int = 0,
    val onay: Int = 0,
    val magazaadi: String?,
    val adres: String?,
    val aciklama: String?,
    val kategori: Int = 0,
    val sure: Int = 0,
    val bitis: String?,
    val odemeturu: String?,
    val fiyat: String?,
    val logo: String?,
    val magazaslider: String?,
    val magazakota: Int = 0
)
