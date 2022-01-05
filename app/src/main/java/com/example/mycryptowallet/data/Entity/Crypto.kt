package com.example.mycryptowallet.data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto_table")
data class Crypto(
    @PrimaryKey val name: String,
    val token: String,
    val currentValue: Double?,
    val amount: Double,
    val amountValue: Double?,
    ) {
}