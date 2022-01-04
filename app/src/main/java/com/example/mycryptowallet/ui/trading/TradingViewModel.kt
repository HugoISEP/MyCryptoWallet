package com.example.mycryptowallet.ui.trading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TradingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is trading bot Fragment"
    }
    val text: LiveData<String> = _text
}