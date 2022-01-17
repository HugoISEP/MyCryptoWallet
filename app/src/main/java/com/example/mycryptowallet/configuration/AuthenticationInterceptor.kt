package com.example.mycryptowallet.configuration

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

interface AuthenticationInterceptor : Interceptor {

    val API_KEY: String
    val API_PASSWORD: String

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val updatedRequest = originalRequest.newBuilder().addHeader(API_KEY, API_PASSWORD).build()
        return chain.proceed(updatedRequest)
    }

}