package com.example.mycryptowallet.data

import android.app.Application
import com.example.mycryptowallet.data.Database.AppDatabase
import com.example.mycryptowallet.data.Repository.CryptoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CryptosApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope)}
    val repository by lazy { CryptoRepository(database.cryptoDAO())}
}