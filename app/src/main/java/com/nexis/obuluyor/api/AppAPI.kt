package com.nexis.obuluyor.api

import com.nexis.obuluyor.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*

interface AppAPI {
    @POST("ana_kategori.php")
    fun getCategories() : Single<List<Category>>

    @POST("alt_kategori.php")
    @FormUrlEncoded
    fun getSubCategories(@Field("ana_kategori") categoryId: Int) : Single<List<SubCategory>>

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

    @POST("ilanlar.php")
    @FormUrlEncoded
    fun getAdverts(
        @Field("confirm") confirm: Int,
        @Field("aktifilanlar") activeAdverts: Int,
        @Field("categories") categories: String?,
        @Field("random") random: Int,
        @Field("randomlimit") randomLimit: Int
    ) : Single<List<Advert>>

    @POST("ilanlar.php")
    @FormUrlEncoded
    fun getUserAdverts(@Field("confirm") confirm: Int, @Field("aktifilanlar") activeAdverts: Int, @Field("userId") userId: Int) : Single<List<Advert>>

    @POST("ilanlar.php")
    @FormUrlEncoded
    fun getAdvertsBySearchResult(@Field("confirm") confirm: Int, @Field("aktifilanlar") activeAdverts: Int) : Single<List<Advert>>

    @POST("ilanlar.php")
    @FormUrlEncoded
    fun getOtherAdverts(
        @Field("confirm") confirm: Int,
        @Field("aktifilanlar") activeAdverts: Int,
        @Field("fiyatdustu") priceDrops: String?,
        @Field("acil") urgent: String?,
        @Field("suresibitenler") last48Hours: String?
    ) : Single<List<Advert>>

    @POST("ilanlar.php")
    @FormUrlEncoded
    fun getAdvertsByUser(
        @Field("confirm") confirm: Int,
        @Field("aktifilanlar") activeAdverts: Int,
        @Field("userId") userId: Int,
    ) : Single<List<Advert>>

    @POST("uye_bilgisi.php")
    @FormUrlEncoded
    fun getUserData(@Field("uyeid") userId: Int) : Single<Sign>

    @POST("magazalar.php")
    fun getStoreList() : Single<List<Store>>

    @POST("uye_guncelle.php")
    @FormUrlEncoded
    fun updateUserData(
        @Field("user_id") userId: Int,
        @Field("ad_soyad") userFullName: String,
        @Field("dogum_tarihi") userBirthday: String,
        @Field("cinsiyet") userGender: String,
        @Field("parola") userPassword: String,
        @Field("telefon") userPhone: String,
        @Field("gsm") userGsm: String,
        @Field("il") userCountry: Int,
        @Field("ilce") userCity: Int
    ) : Single<Result>

    @POST("favoriler.php")
    @FormUrlEncoded
    fun getFavorites(@Field("user_id") userId: Int) : Single<List<Favorite>>

    @POST("favorilere_ekle.php")
    @FormUrlEncoded
    fun addFavorite(@Field("user_id") userId: Int, @Field("ilan_id") advertId: Int) : Single<Result>

    @POST("favori_sil.php")
    @FormUrlEncoded
    fun removeFavorite(@Field("user_id") userId: Int, @Field("ilan_id") advertId: Int, @Field("favori_id") favoriteId: Int) : Single<Result>

    @POST("sendmessage.php")
    @FormUrlEncoded
    fun sendMessage(@Field("userId") userId: Int, @Field("targetId") targetId: Int, @Field("messageContent") messageContent: String, @Field("advertId") advertId: Int) : Single<Result>

    @POST("messages.php")
    @FormUrlEncoded
    fun getMessages(@Field("userId") userId: Int, @Field("messageType") messageType: String) : Single<List<Message>>

    @Multipart
    @POST("magaza_ekle.php")
    fun addStore(
        @Part("uyeId") userId: RequestBody,
        @Part("magazaadi") storeName: RequestBody,
        @Part("adres") storeAddress: RequestBody,
        @Part("aciklama") storeDescription: RequestBody,
        @Part("kategori") categoryId: RequestBody,
        @Part("sure") time: RequestBody,
        @Part("bitis") timeEnd: RequestBody,
        @Part("odemeturu") paymentType: RequestBody,
        @Part("fiyat") price: RequestBody,
        @Part imageLogo: MultipartBody.Part,
        @Part storeSlider: MultipartBody.Part,
        @Part("magazakota") storeQuota: RequestBody
    ) : Single<AddStore>

    @POST("store_payment_info.php")
    fun getStorePaymentInfo() : Single<StorePaymentInfo>

    @Multipart
    @POST("updateStoreData.php")
    fun updateStoreData(
        @Part("userId") userId: RequestBody,
        @Part("storeName") storeName: RequestBody,
        @Part("storeDescription") storeDescription: RequestBody,
        @Part imageLogo: MultipartBody.Part?,
        @Part storeSlider: MultipartBody.Part?
    ) : Single<UpdateStore>

    @POST("getModulesForCategories.php")
    @FormUrlEncoded
    fun getModulesForCategories(@Field("categoriesData") categoriesData: String) : Single<List<ModuleId>>

    @POST("getModules.php")
    @FormUrlEncoded
    fun getModules(@Field("moduleIdData") moduleIdData: String) : Single<List<Module>>

    @POST("getProps.php")
    @FormUrlEncoded
    fun getProps(@Field("advertId") advertId: Int) : Single<List<Prop>>

    @POST("getPropsDataForProps.php")
    @FormUrlEncoded
    fun getPropsData(@Field("propsIdData") propsIdData: String?, @Field("modulesIdData") modulesIdData: String?) : Single<List<PropData>>

    @POST("getPropTitles.php")
    @FormUrlEncoded
    fun getPropTitles(@Field("groupsIdData") groupsIdData: String) : Single<List<GroupTitle>>

    @POST("getModuleContents.php")
    @FormUrlEncoded
    fun getModuleContents(@Field("itemId") itemId: Int) : Single<List<ModuleContent>>

    @POST("ilan_ekle.php")
    @FormUrlEncoded
    fun addNewAdvert(
        @Field("uyeId") userId: Int,
        @Field("phone") userPhone: Int?,
        @Field("message") message: Int?,
        @Field("title") advertTitle: String?,
        @Field("content") advertContent: String?,
        @Field("type") advertType: Int?,
        @Field("price") advertPrice: Int?,
        @Field("exchange") advertExchange: String?,
        @Field("cargoprice") cargoPrice: Int?,
        @Field("cargoarrive") cargoArrive: Int?,
        @Field("cargo") advertCargo: String?,
        @Field("city") advertCountry: Int,
        @Field("districts") advertCity: Int,
        @Field("locality") advertLocality: Int,
        @Field("lat") advertLat: String?,
        @Field("lng") advertLng: String?,
        @Field("zoom") advertZoom: String?,
        @Field("confirm") advertConfirm: Int?,
        @Field("dates") advertDates: String?,
        @Field("categories") advertCategories: String,
        @Field("yayin") advertStream: Int?,
        @Field("bitis") advertFinishDate: String?,
        @Field("fiyat2") advertPrice2: String?,
        @Field("fiyat3") advertPrice3: String?,
        @Field("bitiszamani") advertFinishFullDate: String?,
        @Field("odeme") advertPay: Int?,
        @Field("firmadi") companyName: String?,
        @Field("telefon") phone: String?,
        @Field("fax") fax: String?,
        @Field("gsm") gsm: String?,
        @Field("web") web: String?,
        @Field("vergidairesi") taxAdmin: String?,
        @Field("vergino") taxNo: String?,
        @Field("kurulusyili") createDate: String?,
        @Field("isletmeturu") businessType: String?,
        @Field("yetkili") director: String?,
        @Field("ciro") endorsement: String?,
        @Field("calisansayisi") employeesAmount: String?,
        @Field("hakkinda") about: String?,
        @Field("fadres") adres: String?,
        @Field("gosterim") viewAmount: Int?,
        @Field("fiyatdustu") priceDown: String?,
        @Field("moduleContentIdData") moduleContentIdData: String,
        @Field("moduleContentNameData") moduleContentNameData: String,
        @Field("moduleIdData") moduleIdData: String,
        @Field("moduleClassData") moduleClassData: String,
        @Field("propIdData") propIdData: String,
        @Field("propValData") propValData: String
    ) : Single<Advert>

    @Multipart
    @POST("ilan_resim_ekle.php")
    fun addImageForAdvert(
        @Part("ilanId") advertId: RequestBody,
        @Part advertImage: MultipartBody.Part?
    ) : Single<AddAdvertImage>
}