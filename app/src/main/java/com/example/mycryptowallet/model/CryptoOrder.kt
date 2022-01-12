package com.example.mycryptowallet.model

import java.util.*


data class CryptoOrder(
    val id: String,
    val clientId: String?,
    val market: String,
    val side: String,
    val price: Float,
    val size: Float,
    val status: String,
    val filledSize: Float,
    val remainingSize: Float,
    val reduceOnly: Boolean,
    val liquidation: Boolean,
    val avgFillPrice: Float?,
    val postOnly: Boolean,
    val ioc: Boolean,
    val createdAt: Date,
    val future: String?
    ) {
}