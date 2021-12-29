package com.example.mycryptowallet.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.mycryptowallet.api.CryptoApiService
import com.example.mycryptowallet.model.CandlestickData
import com.example.mycryptowallet.model.TimeInterval
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel : ViewModel() {

    private val cryptoApiService = CryptoApiService.retrofitService
    private val pairSelected = MutableLiveData<String>()
    private val intervalSelected = MutableLiveData<TimeInterval>()
    private val _candlesData = MutableLiveData<List<CandlestickData>?>()

    fun getCandlesData(timeInterval: TimeInterval, pairSelected: String) {
        Log.d("getCandlesData", "enter")
        var interval = ""
        var startTime: Long = 0
        val currentEpochTime = Calendar.getInstance().timeInMillis
        val oneDayInMillis = 86400000
        when(timeInterval) {
            TimeInterval.YEAR -> {
                interval = "1w"
                // Current time minus one year in milliseconds
                startTime = currentEpochTime - oneDayInMillis * 365
            }
            TimeInterval.MONTH -> {
                interval = "1d"
                // Current time minus one month in milliseconds
                startTime = currentEpochTime - 2629800000
            }
            TimeInterval.WEEK -> {
                interval = "6h"
                // Current time minus one year in milliseconds
                startTime = currentEpochTime - oneDayInMillis * 7
            }
            TimeInterval.DAY -> {
                interval = "1h"
                // Current time minus one year in milliseconds
                startTime = currentEpochTime - oneDayInMillis
            }

        }
        viewModelScope.launch{
            try {
                val response = cryptoApiService.getPriceHistoryFromPair(pairSelected ,interval, startTime)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    val list: MutableList<CandlestickData> = mutableListOf()
                    if (content != null) {
                        for (d in content) {
                            list.add(CandlestickData(d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9], d[10], d[11]))
                        }
                    }
                    Log.d("content", list.toString())
                    // _candlesData.value = content
                } else {
                    Log.d("getCryptoPrice", "failure")
                }

            } catch (e: Exception) {
                Log.d("getCryptoPrice", "failure: ${e.message}")
            }
        }
    }

    fun testing(timeInterval: TimeInterval, pairSelected: String) {
        Log.d("getCandlesData", "testing")

    }

    val pieChartData: LiveData<PieDataSet> = liveData {
        val dataObjects = listOf(Pair("MANA", 123.0F), Pair("BTC", 0.0001F), Pair("ETH", 0.005F), Pair("SAND", 5.0F))
        val entries: MutableList<PieEntry> = ArrayList()
        for (data in dataObjects) {
            // turn data into Entry objects
            try {
                val response = cryptoApiService.getCryptoPrice(data.first + "USDT")

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    val usdValue = content!!.price.toFloat() * data.second
                    entries.add(PieEntry(usdValue, data.first, content))
                } else {
                    Log.d("getCryptoPrice", "failure")
                }

            } catch (e: Exception) {
                Log.d("getCryptoPrice", "failure")
            }
        }
        val dataSet = PieDataSet(entries, null)
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        emit(dataSet)
    }

}