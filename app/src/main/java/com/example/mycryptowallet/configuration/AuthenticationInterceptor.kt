package com.example.mycryptowallet.configuration

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

internal class AuthenticationInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        Log.d("AuthenticationIntercept", "good")
        originalRequest.headers.newBuilder().add("API_KEY", "API_VALUE")
        return chain.proceed(originalRequest)
    }

}