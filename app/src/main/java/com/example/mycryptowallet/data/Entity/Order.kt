package com.example.mycryptowallet.data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class OrderType {
    BUY, SELL
}

@Entity(tableName = "order_table")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val cryptoToken: String,
    val type: OrderType,
    val amount: Double,
    val amountValue: Double,
) {
    constructor(cryptoName: String, type: OrderType, amount: Double, amountValue: Double) : this(0, cryptoName, type, amount, amountValue)
}