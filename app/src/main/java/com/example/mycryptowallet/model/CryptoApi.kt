package com.example.mycryptowallet.model

data class CryptoApi(val symbol: String, val price: String) {
    val priceFloat: Float = price.toFloat()
}