package com.example.openin_app.mApiServices

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.openin_app.utils.LenientGsonConverterFactory
import com.example.openin_app.utils.NetworkConnectionInterceptor
import com.example.openin_app.utils.StringConverterFactory
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intuit.sdp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.inopenapp.com/"
    private const val GSON_BUILDER_DATE_TIME_FORMAT = "EEE MMM dd HH:mm:ss 'Z' yyyy"
    private const val OK_HTTP_TIME_OUT_IN_SECONDS = 1000L

    fun create(context: Context): ApiService {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", "")

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val gsonConverterFactory: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat(GSON_BUILDER_DATE_TIME_FORMAT)
            .create()

        val openInAppInterceptor = Interceptor { chain ->
            if (!token.isNullOrEmpty()) {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                    Log.e(TAG, "o̵penInAppInterceptor: $request")
                chain.proceed(request)
            } else {
                chain.proceed(chain.request())
            }
        }


        val okHttpClient = OkHttpClient.Builder()
            .followRedirects(false)
            .readTimeout(OK_HTTP_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(OK_HTTP_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addNetworkInterceptor(openInAppInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonConverterFactory))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
