package com.nutriscan.app.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que configura o Retrofit para comunicar com a API Open Food Facts.
 *
 * Usa o ambiente .net (staging) porque o .org (produção) costuma ficar fora do ar.
 * Para trocar para produção, mudar BASE_URL para "https://world.openfoodfacts.org/"
 */
object RetrofitClient {
    private const val BASE_URL = "https://world.openfoodfacts.net/"

    val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}
