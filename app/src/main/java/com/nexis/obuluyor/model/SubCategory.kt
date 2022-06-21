package com.nexis.obuluyor.model

data class SubCategory(
    val Id: Int = 0,
    val kategori_adi: String = "",
    val ustkategoriId: Int = 0,
    val modul: Int = 0,
    val ikon: String = "",
    val title: String = "",
    val description: String = "",
    val sira: Int = 0,
    val slink: String = "",
    val moduller: String = "",
    val durum: Int = 0,
    val fiyat1: String?,
    val fiyat2: String?,
    val fiyat3: String?,
    val tip: Int = 0
)
