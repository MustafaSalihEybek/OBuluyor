package com.nexis.obuluyor.model

data class Message(
    val Id: Int?,
    val gonderen: Int?,
    val gonderilen: Int?,
    val konu: Int?,
    val mesaj: String?,
    val tarih: String?,
    val okundu: Int?,
    val gonderensil: Int?,
    val gonderilensil: Int?,
    val advert: Advert?
)
