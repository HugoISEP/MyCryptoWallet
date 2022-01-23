package com.example.mycryptowallet.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.mycryptowallet.api.BinanceApiService
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.data.Repository.CryptoRepository
import com.example.mycryptowallet.model.CandlestickData
import com.example.mycryptowallet.model.TimeInterval
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(private val cryptoRepository: CryptoRepository) : ViewModel() {

    private val allCryptos: LiveData<List<Crypto>> = cryptoRepository.allCryptos.asLiveData()

    private val cryptoApiService = BinanceApiService.retrofitService
    lateinit var timeInterval: TimeInterval
    private val _lineDataSet = MutableLiveData<LineDataSet>()
    fun lineDataSet(): LiveData<LineDataSet> {
        return _lineDataSet
    }


    fun pieChartData() = Transformations.switchMap(allCryptos){
        getChartData(it)
    }

    fun getCandlesData(pairSelected: String) {
        var interval = ""
        var startTime: Long = 0
        val currentEpochTime = Calendar.getInstance().timeInMillis
        val oneDayInMillis = 86400000
        when(timeInterval) {
            TimeInterval.YEAR -> {
                interval = "1w"
                // Current time minus one year in milliseconds
                startTime = currentEpochTime - 31556952000
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
                    val entries: MutableList<Entry> = ArrayList()
                    if (content != null) {
                        for (d in content) {
                            val candleData = CandlestickData(d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9], d[10], d[11])
                            entries.add(Entry(candleData.closeTime.toFloat(), candleData.close.toFloat()))
                        }
                    }
                    val lineDataSet = LineDataSet(entries, "lineChart")
                    _lineDataSet.value = lineDataSet
                } else {
                    Log.d("getCryptoPrice", "failure")
                }

            } catch (e: Exception) {
                Log.d("getCryptoPrice", "failure: ${e.message}")
            }
        }
    }

    fun getChartData(cryptos :List<Crypto>) = liveData {
        val entries: MutableList<PieEntry> = ArrayList()
        // turn data into Entry objects
        for (crypto in cryptos) {
            try {
                val response = cryptoApiService.getCryptoPrice(crypto.token + "USDT")

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()!!
                    crypto.currentValue = content.price.toDouble()
                    val usdValue = content.price.toFloat() * crypto.amount
                    entries.add(PieEntry(usdValue.toFloat(), crypto.token, crypto))
                } else {
                    Log.d("getCryptoPrice", "failure")
                }

            } catch (e: Exception) {
                Log.d("getCryptoPrice", e.message.toString())
            }
        }
        val dataSet = PieDataSet(entries, null)
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        emit(dataSet)
    }

}