package com.example.mycryptowallet.ui.dashboard.detail

import androidx.lifecycle.*
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.data.Entity.Order
import com.example.mycryptowallet.data.Repository.CryptoRepository
import com.example.mycryptowallet.data.Repository.OrderRepository

import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CryptoDetailViewModel(private val repository: OrderRepository, private val crypto: Crypto, private val cryptoRepository: CryptoRepository) : ViewModel() {
    val orders: LiveData<List<Order>> = repository.ordersFor(crypto.token).asLiveData()
    val currentCrypto = cryptoRepository.getCryptoByName(crypto.name).asLiveData()
    fun insert(order:Order) = viewModelScope.launch { repository.insert(order) }
    fun updateCrypto(name: String, amount: Double, amountValue: Double) = viewModelScope.launch { cryptoRepository.update(name, amount, amountValue) }
}

class CryptoDetailViewModelFactory(private val repository: OrderRepository, private val crypto: Crypto, private val cryptoRepository: CryptoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CryptoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CryptoDetailViewModel(repository, crypto, cryptoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}