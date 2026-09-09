package com.sih2026.artisancatalog.data.remote

import com.sih2026.artisancatalog.data.remote.api.KalakritiApiService
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // Default to the host machine's Wi-Fi network address
    const val DEFAULT_BASE_URL = "http://10.10.159.148:3001/"

    @Volatile
    var currentBaseUrl: String = DEFAULT_BASE_URL

    // Dynamically rewrites request host/port if user updates serverUrl in Settings
    private val dynamicHostInterceptor = Interceptor { chain ->
        var request = chain.request()
        val targetHttpUrl = currentBaseUrl.toHttpUrlOrNull()
        if (targetHttpUrl != null) {
            val newUrl = request.url.newBuilder()
                .scheme(targetHttpUrl.scheme)
                .host(targetHttpUrl.host)
                .port(targetHttpUrl.port)
                .build()
            request = request.newBuilder().url(newUrl).build()
        }
        chain.proceed(request)
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(dynamicHostInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val instance: KalakritiApiService by lazy {
        create(DEFAULT_BASE_URL)
    }

    fun create(baseUrl: String = DEFAULT_BASE_URL): KalakritiApiService {
        currentBaseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        return Retrofit.Builder()
            .baseUrl(currentBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KalakritiApiService::class.java)
    }
}
