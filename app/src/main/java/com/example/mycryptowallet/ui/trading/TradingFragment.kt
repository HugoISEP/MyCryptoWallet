package com.example.mycryptowallet.ui.trading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mycryptowallet.R

class TradingFragment : Fragment() {

    private lateinit var tradingViewModel: TradingViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tradingViewModel =
                ViewModelProvider(this).get(TradingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trading, container, false)
        val textView: TextView = root.findViewById(R.id.text_trading)
        tradingViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}