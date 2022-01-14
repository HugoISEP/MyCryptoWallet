package com.example.mycryptowallet.ui.trading

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mycryptowallet.api.TradingBotApiService
import com.example.mycryptowallet.model.CryptoOrder
import com.example.mycryptowallet.model.FtxBalance
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class TradingViewModel : ViewModel() {

    private val tradingBotApiService = TradingBotApiService.retrofitService

    val balances: LiveData<List<FtxBalance>> = liveData {
        try {
            val response = tradingBotApiService.getBotBalances(1)

            if (response.isSuccessful && response.body() != null) {
                val content = response.body()?.filter { it.usdValue != 0f }
                emit(content!!)
            } else {
                Log.d("totalBalance", "failure")
            }

        } catch (e: Exception) {
            Log.d("totalBalance", e.message.toString())
        }
    }

    val totalBalance: LiveData<Double?> = liveData {
        val totalUsdValue = balances.value?.sumOf { it.usdValue.toDouble() }
        emit(totalUsdValue)
    }


    val weekTrades: LiveData<MutableList<Pair<CryptoOrder, CryptoOrder?>>> = liveData {
        try {
            val currentEpochTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            val sevenDayInMillis = 604800
            val epochTimeOneWeekAgo = currentEpochTime - sevenDayInMillis
            val response = tradingBotApiService.getBotOrderHistory(id=1, startTime=epochTimeOneWeekAgo)
            if (response.isSuccessful && response.body() != null) {
                val content = response.body()
                val orders = content!!.toMutableList()
                val ordersAlreadyProcessed: MutableList<String> = ArrayList()
                val list: MutableList<Pair<CryptoOrder, CryptoOrder?>> = ArrayList()
                orders.forEachIndexed { index, it ->
                    if (it.side == "sell" && it.filledSize != 0f){
                        val buyOrder = orders.subList(index, orders.size).firstOrNull { cryptoOrder ->
                            cryptoOrder.side == "buy"
                            && cryptoOrder.market == it.market
                            && cryptoOrder.filledSize == it.filledSize  }
                        if (buyOrder != null) {
                            ordersAlreadyProcessed.add(buyOrder.id)
                            list.add(Pair(buyOrder, it))
                        }
                    } else if (it.side == "buy" && !ordersAlreadyProcessed.contains(it.id)) {
                        list.add(Pair(it, null))
                    }
                }
                emit(list)
            } else {
                Log.d("weekTrades", "failure")
            }

        } catch (e: Exception) {
            Log.d("weekTrades error", e.message.toString())
        }
    }


}