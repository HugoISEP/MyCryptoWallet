package com.example.mycryptowallet.ui.dashboard.detail

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.CryptosApplication
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.data.Entity.Order
import com.example.mycryptowallet.data.Entity.OrderType
import com.google.android.material.floatingactionbutton.FloatingActionButton

//not used, see activity related
class CryptoDetailFragment : Fragment() {


    companion object {
        fun newInstance() = CryptoDetailFragment()
    }
    private lateinit var crypto: Crypto
    val cryptoDetailViewModel: CryptoDetailViewModel by viewModels {
        CryptoDetailViewModelFactory((requireActivity().application as CryptosApplication).orderRepository, crypto, (requireActivity().application as CryptosApplication).cryptoRepository)
    }
    private val newOrderActivityRequestCode = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.crypto_detail_fragment, container, false)
        val extras = activity?.intent?.extras
        if (extras != null) {
            crypto = extras.get("crypto") as Crypto
        }

        val nameTextView = root.findViewById<TextView>(R.id.crypto_name)
        val tokenTextView = root.findViewById<TextView>(R.id.crypto_token)
        val amountTextView = root.findViewById<TextView>(R.id.crypto_amount)

        nameTextView.text = crypto.name
        tokenTextView.text = crypto.token
        amountTextView.text = crypto.amount.toString()

        val addOrderButton = root.findViewById<FloatingActionButton>(R.id.add_order)
        addOrderButton.setOnClickListener {
            val intent = Intent(root.context, NewOrderActivity::class.java)
            startActivityForResult(intent, newOrderActivityRequestCode)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newOrderActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringArrayListExtra("new order")?.let { reply ->
                val order = Order(crypto.name, OrderType.BUY, reply[1].toDouble(), reply[2].toDouble())
                cryptoDetailViewModel.insert(order)
            }
        }
    }

}