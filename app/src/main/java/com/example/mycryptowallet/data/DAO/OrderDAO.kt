package com.example.mycryptowallet.data.DAO

import androidx.room.*
import com.example.mycryptowallet.data.Entity.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDAO {
    @Query("SELECT * FROM order_table WHERE cryptoToken LIKE :token " +
            "LIMIT 1000")
    fun findByCryptoToken(token: String): Flow<List<Order>>

    @Insert
    suspend fun insert(order: Order)

    @Query("DELETE FROM order_table")
    fun deleteAll()
}