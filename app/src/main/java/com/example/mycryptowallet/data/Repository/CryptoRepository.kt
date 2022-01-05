package com.example.mycryptowallet.data.Repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import com.example.mycryptowallet.data.DAO.CryptoDAO
import com.example.mycryptowallet.data.Entity.Crypto

class CryptoRepository(private val cryptoDAO: CryptoDAO) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
     val allCryptos: Flow<List<Crypto>> = cryptoDAO.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(crypto: Crypto) {
        cryptoDAO.insert(crypto)
    }
}