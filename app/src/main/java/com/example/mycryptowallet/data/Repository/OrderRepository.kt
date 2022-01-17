package com.example.mycryptowallet.data.Repository

import androidx.annotation.WorkerThread
import com.example.mycryptowallet.data.DAO.OrderDAO
import com.example.mycryptowallet.data.Entity.Order
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDAO: OrderDAO) {
    fun ordersFor(cryptoToken: String) : Flow<List<Order>> {
        return orderDAO.findByCryptoToken(cryptoToken)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(order: Order) {
        orderDAO.insert(order)
    }
}