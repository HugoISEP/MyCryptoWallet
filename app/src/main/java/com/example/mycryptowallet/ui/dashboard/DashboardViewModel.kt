package com.example.mycryptowallet.ui.dashboard

import androidx.lifecycle.*
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.data.Repository.CryptoRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

class DashboardViewModel(private val repository: CryptoRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    val allCryptos: LiveData<List<Crypto>> = repository.allCryptos.asLiveData()
    fun insert(crypto: Crypto) = viewModelScope.launch { repository.insert(crypto) }
}

class DashboardViewModelFactory(private val repository: CryptoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}