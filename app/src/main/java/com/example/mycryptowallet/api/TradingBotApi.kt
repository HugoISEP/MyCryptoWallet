package com.example.mycryptowallet.api

import com.example.mycryptowallet.configuration.CryptoAuthenticationInterceptor
import com.example.mycryptowallet.model.CryptoOrder
import com.example.mycryptowallet.model.FtxBalance
import com.example.mycryptowallet.service.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(CryptoAuthenticationInterceptor())
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()


private val gson : Gson by lazy {
    GsonBuilder().setLenient().create()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(Constant.TRADING_BOT_URL)
    .client(client)
    .build()

interface TradingBotApi {
    @GET("status")
    suspend fun getStatus(): Response<String>

    @GET("bot/{id}/status")
    suspend fun getBotStatus(@Path("id") id: Int): Response<String>

    @GET("bot/{id}/balances")
    suspend fun getBotBalances(@Path("id") id: Int): Response<List<FtxBalance>>

    @GET("bot/{id}/markets")
    suspend fun getBotMarkets(@Path("id") id: Int): Response<List<String>>

    @GET("bot/{id}/ordersHistory")
    suspend fun getBotOrderHistory(
        @Path("id") id: Int,
        market: String?,
        side: String?,
        order_type: String?,
        start_time: Float?,
        end_time: Float?): Response<List<CryptoOrder>>

}

object TradingBotApiService {
    val retrofitService : TradingBotApi by lazy {
        retrofit.create(TradingBotApi::class.java)
    }
}