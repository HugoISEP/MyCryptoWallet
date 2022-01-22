package com.example.mycryptowallet.data

import android.app.Application
import com.example.mycryptowallet.data.Database.AppDatabase
import com.example.mycryptowallet.data.Repository.CryptoRepository
import com.example.mycryptowallet.data.Repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CryptosApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope)}
    val cryptoRepository by lazy { CryptoRepository(database.cryptoDAO())}
    val orderRepository by lazy { OrderRepository(database.orderDAO()) }
}