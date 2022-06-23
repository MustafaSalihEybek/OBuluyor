package com.nexis.obuluyor.api

import com.nexis.obuluyor.model.*
import io.reactivex.Single
import retrofit2.http.*

interface AppAPI {
    @POST("ana_kategori.php")
    fun getCategories() : Single<List<Category>>

    @POST("iller.php")
    fun getCountries() : Single<List<Country>>

    @POST("ilceler.php")
    @FormUrlEncoded
    fun getCities(@Field("il") countryId: Int) : Single<List<City>>

    @POST("uye_ol.php")
    @FormUrlEncoded
    fun signUpUser(
        @Field("ad_soyad") fullName: String,
        @Field("dogum_tarihi") birthday: String,
        @Field("cinsiyet") gender: String,
        @Field("eposta") email: String,
        @Field("parola") password: String,
        @Field("telefon") phone: String,
        @Field("gsm") gsm: String,
        @Field("il") country: Int,
        @Field("ilce") city: Int
    ) : Single<Sign>

    @POST("kullanici_girisi.php")
    @FormUrlEncoded
    fun signInUser(@Field("eposta") email: String, @Field("sifre") password: String) : Single<Sign>
}