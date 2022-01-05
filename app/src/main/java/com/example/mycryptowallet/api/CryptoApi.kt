package com.example.mycryptowallet.api

import com.example.mycryptowallet.configuration.AuthenticationInterceptor
import com.example.mycryptowallet.model.CandlestickData
import com.example.mycryptowallet.model.CryptoApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.mycryptowallet.service.Constant.BINANCE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory


private val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthenticationInterceptor())
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

private val gson : Gson by lazy {
    GsonBuilder().setLenient().create()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BINANCE_URL)
    .client(client)
    .build()

interface CryptoApiRoot {
    @GET("ticker/price")
    suspend fun getCryptoPrice(@Query("symbol", encoded = true) symbolPair: String): Response<CryptoApi>

    @GET("klines")
    suspend fun getPriceHistoryFromPair(
            @Query("symbol", encoded = true) symbolPair: String,
            @Query("interval", encoded = true) interval: String,
            @Query("startTime", encoded = true) startTime: Long
    ): Response<ArrayList<ArrayList<String>>>
}

object CryptoApiService {
    val retrofitService : CryptoApiRoot by lazy {
        retrofit.create(CryptoApiRoot::class.java)
    }
}