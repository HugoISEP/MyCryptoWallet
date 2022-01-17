package com.example.mycryptowallet.ui.dashboard.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.CryptosApplication
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.data.Entity.Order
import com.example.mycryptowallet.data.Entity.OrderType
import com.example.mycryptowallet.data.Repository.CryptoRepository
import com.example.mycryptowallet.data.Repository.OrderRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class CryptoDetailActivity : AppCompatActivity() {
    private lateinit var crypto: Crypto
    private val newOrderActivityRequestCode = 1
    private val cryptoDetailViewModel: CryptoDetailViewModel by viewModels {
        CryptoDetailViewModelFactory((application as CryptosApplication).orderRepository, crypto, (application as CryptosApplication).cryptoRepository)
    }

    // Allows to run suspend function by using a new coroutine
    private val scope = CoroutineScope(newSingleThreadContext("cryptoUpdate"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_detail)

        val recyclerView = findViewById<RecyclerView>(R.id.orderList)
        val adapter = OrderListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        val extras = intent.extras
        if (extras != null) {
            crypto = extras.get("crypto") as Crypto
        }

        val nameTextView = findViewById<TextView>(R.id.crypto_name)
        val tokenTextView = findViewById<TextView>(R.id.crypto_token)
        val amountTextView = findViewById<TextView>(R.id.crypto_amount)

        nameTextView.text = crypto.name
        tokenTextView.text = crypto.token
        amountTextView.text = crypto.amount.toString()

        val addOrderButton = findViewById<FloatingActionButton>(R.id.add_order)
        addOrderButton.setOnClickListener {
            val intent = Intent(applicationContext, NewOrderActivity::class.java)
            startActivityForResult(intent, newOrderActivityRequestCode)
        }

        cryptoDetailViewModel.orders.observe(this,
            { orders -> orders.let { adapter.submitList(it) }})

        cryptoDetailViewModel.currentCrypto.observe(this,
            { crypto -> crypto.let { amountTextView.text = it.amount.toString() }})



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newOrderActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringArrayListExtra("new order")?.let { reply ->
                val orderType: OrderType = if(reply[2] == OrderType.BUY.toString()) {
                    OrderType.BUY
                } else {
                    OrderType.SELL
                }
                val order = Order(crypto.token, orderType, reply[0].toDouble(), reply[1].toDouble())
                cryptoDetailViewModel.insert(order)


                scope.launch {
                    var newAmount = 0.0
                    var newAmountValue = 0.0
                    val orders = (application as CryptosApplication).orderRepository.ordersFor(crypto.token)
                    orders.first().forEach {
                            order -> run {
                                if(order.type == OrderType.BUY) {
                                    newAmount += order.amount
                                    newAmountValue += order.amountValue
                                } else if (order.type == OrderType.SELL) {
                                    newAmount -= order.amount
                                    newAmountValue -= order.amountValue
                                }
                            }
                    }
                    cryptoDetailViewModel.updateCrypto(crypto.name, newAmount, newAmountValue)
                }



            }
        }
    }
}