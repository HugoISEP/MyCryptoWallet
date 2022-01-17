package com.example.mycryptowallet.model

data class FtxBalance(
    val coin: String,
    val total: Float,
    val free: Float,
    val availableWithoutBorrow: Float,
    val usdValue: Float,
    val spotBorrow: Float) {
}