package com.example.mycryptowallet

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mycryptowallet.api.BinanceApiService
import com.example.mycryptowallet.api.TradingBotApiService
import com.example.mycryptowallet.service.Constant.INITIAL_TRADING_WALLET
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val cryptoApiService = BinanceApiService.retrofitService
    private val tradingBotApiService = TradingBotApiService.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_trading))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        getCryptoPrice()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        sharedPreferences.edit().putFloat(INITIAL_TRADING_WALLET, 50f).apply()

    }

    private fun getCryptoPrice() {
        GlobalScope.launch(Dispatchers.Main) {
            Log.d("Enter", "enter")
            try {
                val response = tradingBotApiService.getBotStatus(1)
                Log.d("SUCCESS", "response: " + response)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()
                    Log.d("getCryptoPrice", content.toString())
                } else {
                    Log.d("getCryptoPrice", "failure")
                }

            } catch (e: Exception) {
                Log.d("getCryptoPrice", "failure: " + e.message)
            }
        }
    }

}