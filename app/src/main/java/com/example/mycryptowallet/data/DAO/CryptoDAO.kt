package com.example.mycryptowallet.data.DAO

import androidx.room.*
import com.example.mycryptowallet.data.Entity.Crypto
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDAO {
    @Query("SELECT * FROM crypto_table")
    fun getAll(): Flow<List<Crypto>>

    @Query("SELECT * FROM crypto_table WHERE name LIKE :name " +
            "LIMIT 1")
    fun findByName(name: String): Crypto

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crypto: Crypto)

    @Update
    fun update(crypto: Crypto)

    @Delete
    suspend fun delete(crypto: Crypto)

    @Query("DELETE FROM crypto_table")
    fun deleteAll()
}