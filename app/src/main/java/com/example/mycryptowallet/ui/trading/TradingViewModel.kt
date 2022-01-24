package com.example.mycryptowallet.ui.trading

import android.app.Application
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.*
import com.example.mycryptowallet.api.TradingBotApiService
import com.example.mycryptowallet.model.CryptoOrder
import com.example.mycryptowallet.service.Constant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class TradingViewModel(application: Application) : AndroidViewModel(application) {

    private val tradingBotApiService = TradingBotApiService.retrofitService
    private val reloadTrigger = MutableLiveData(false)

    init {
        refresh()
    }

    val balances = Transformations.switchMap(reloadTrigger){
        fetchBalances()
    }

    val weekTrades = Transformations.switchMap(reloadTrigger){
        fetchWeekTrades()
    }

    fun refresh() {
        reloadTrigger.value = !(reloadTrigger.value as Boolean)
    }

    // Get balances of the trading bot
    private fun fetchBalances() = liveData {
        try {
            val response = tradingBotApiService.getBotBalances(1)

            if (response.isSuccessful && response.body() != null) {
                val content = response.body()?.filter { it.usdValue != 0f }
                emit(content!!)
            } else {
                Log.d("balances", "failure")
            }

        } catch (e: Exception) {
            Log.d("balances", e.message.toString())
        }
    }

    // Get week trades of the trading bot
    fun fetchWeekTrades() = liveData {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication<Application?>().applicationContext)
        val tradeDuration = sharedPreferences.getString(Constant.TRADE_DURATION, "Week")
        val timeInSeconds = when(tradeDuration){
            "Day" -> 86400
            "Month" -> 2629800
            else -> 604800 // for "Week" value
        }
        try {
            val currentEpochTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            val epochTimeAgo = currentEpochTime - timeInSeconds
            val response = tradingBotApiService.getBotOrderHistory(id=1, startTime=epochTimeAgo)
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
            Log.d("weekTrades", e.message.toString())
        }
    }

}