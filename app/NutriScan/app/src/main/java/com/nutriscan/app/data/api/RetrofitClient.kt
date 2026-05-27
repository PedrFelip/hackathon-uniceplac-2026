package com.nutriscan.app.data.api

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Singleton que configura o Retrofit para comunicar com a API Open Food Facts.
 *
 * Usa o ambiente .net (staging) porque o .org (produção) costuma ficar fora do ar.
 * Para trocar para produção, mudar BASE_URL para "https://world.openfoodfacts.org/"
 */
object RetrofitClient {
    private const val BASE_URL = "https://world.openfoodfacts.net/"
    private const val CACHE_SIZE = 5L * 1024 * 1024 // 5 MB

    private val okHttpClient: OkHttpClient by lazy {
        val cacheDir = File(System.getProperty("java.io.tmpdir") ?: ".", "nutriscan_http_cache")
        cacheDir.mkdirs()

        OkHttpClient.Builder()
            .addInterceptor(RateLimitInterceptor())
            .cache(Cache(cacheDir, CACHE_SIZE))
            .build()
    }

    val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}
