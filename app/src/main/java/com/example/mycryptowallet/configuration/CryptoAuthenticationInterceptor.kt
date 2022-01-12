package com.example.mycryptowallet.configuration

import com.example.mycryptowallet.BuildConfig

class CryptoAuthenticationInterceptor(
    override val API_KEY: String = BuildConfig.CRYPTO_API_KEY,
    override val API_PASSWORD: String = BuildConfig.CRYPTO_API_PASSWORD
) : AuthenticationInterceptor {
}