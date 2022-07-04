package com.nexis.obuluyor.util

import com.nexis.obuluyor.R
import com.nexis.obuluyor.api.AppAPI
import com.nexis.obuluyor.model.User
import com.nexis.obuluyor.repository.*
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

object AppUtils {
    lateinit var countriesRepository: CountriesRepository
    lateinit var citiesRepository: CitiesRepository
    lateinit var signUpRepository: SignUpRepository
    lateinit var signInRepository: SignInRepository
    lateinit var subCategoriesRepository: SubCategoriesRepository
    lateinit var categoriesRepository: CategoriesRepository
    lateinit var advertsRepository: AdvertsRepository
    lateinit var userDataRepository: UserDataRepository

    lateinit var disposable: CompositeDisposable
    lateinit var categoryImageMap: HashMap<String, Int>

    lateinit var mUser: User

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
            ))
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()

        return Retrofit.Builder()
            .baseUrl(Singleton.BASE_URL)
            .client(clientAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AppAPI::class.java)
    }

    fun getFormattedPrice(price: Float) : String{
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber = formatter.format(price)

        return formattedNumber
    }
}