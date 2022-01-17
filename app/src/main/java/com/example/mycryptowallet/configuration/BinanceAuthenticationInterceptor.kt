package com.example.mycryptowallet.configuration

import com.example.mycryptowallet.BuildConfig

class BinanceAuthenticationInterceptor(
    override val API_KEY: String = BuildConfig.BINANCE_API_KEY,
    override val API_PASSWORD: String = BuildConfig.BINANCE_API_PASSWORD
) : AuthenticationInterceptor {
}