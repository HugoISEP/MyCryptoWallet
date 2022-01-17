package com.example.mycryptowallet.data.DAO

import androidx.room.*
import com.example.mycryptowallet.data.Entity.Crypto
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDAO {
    @Query("SELECT * FROM crypto_table")
    fun getAll(): Flow<List<Crypto>>

    @Query("SELECT * FROM crypto_table WHERE name LIKE :name ")
    fun findByName(name: String): Flow<Crypto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crypto: Crypto)

    @Query("UPDATE crypto_table SET amount = :amount, amountValue = :amountValue WHERE name = :name")
    suspend fun update(name: String, amount: Double, amountValue: Double)

    @Delete
    suspend fun delete(crypto: Crypto)

    @Query("DELETE FROM crypto_table")
    fun deleteAll()
}