package com.nexis.obuluyor.util

import com.nexis.obuluyor.R
import com.nexis.obuluyor.api.AppAPI
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object AppUtils {
    lateinit var disposable: CompositeDisposable
    lateinit var categoryImageMap: HashMap<String, Int>

    fun getCategoryMap() : HashMap<String, Int> {
        categoryImageMap = HashMap()

        categoryImageMap.put("Emlak", R.drawable.home_icon)
        categoryImageMap.put("Hayvanlar Alemi", R.drawable.animal_icon)
        categoryImageMap.put("İkinci El ve Sıfır Alışveriş", R.drawable.shopping_icon)
        categoryImageMap.put("İş İlanları", R.drawable.job_posting_icon)
        categoryImageMap.put("İş Makineleri & Sanayi", R.drawable.job_machine_icon)
        categoryImageMap.put("Özel Ders Verenler", R.drawable.special_lesson_icon)
        categoryImageMap.put("Vasıta", R.drawable.vehicle_icon)
        categoryImageMap.put("Yardımcı Arayanlar", R.drawable.job_helper_icon)
        categoryImageMap.put("Yedek Parça & Tuning", R.drawable.vehicle_repair_icon)

        return categoryImageMap
    }

    fun getAppAPI() : AppAPI {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val clientAPI: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(
                Singleton.clientUsername,
                Singleton.clientPassword
            )).build()

        return Retrofit.Builder()
            .baseUrl(Singleton.BASE_URL)
            .client(clientAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AppAPI::class.java)
    }
}