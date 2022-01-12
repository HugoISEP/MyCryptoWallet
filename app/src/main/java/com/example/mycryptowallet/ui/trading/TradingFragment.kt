package com.example.mycryptowallet.ui.trading

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mycryptowallet.R
import com.example.mycryptowallet.service.Constant.INITIAL_TRADING_WALLET
import kotlinx.android.synthetic.main.fragment_trading.*
import java.util.stream.Collectors

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

        val initialWalletView: TextView = root.findViewById(R.id.initialWallet)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(root.context /* Activity context */)
        val initialWallet = sharedPreferences.getFloat(INITIAL_TRADING_WALLET, 0f)
        initialWalletView.text = String.format("initial: %.2f $", initialWallet)

        val walletPercentageTextView: TextView = root.findViewById(R.id.walletChangePercentage)
        val textView: TextView = root.findViewById(R.id.totalWallet)
        tradingViewModel.totalBalance.observe(viewLifecycleOwner, Observer {
            textView.text = String.format("%.2f $", it)

            val walletChangePercentage = it * 100 / initialWallet - 100
            walletPercentageTextView.setTextColor(if (walletChangePercentage > 0) Color.GREEN else Color.RED)
            walletPercentageTextView.text = String.format("%.2f %%", walletChangePercentage)
        })

        val numberOfTradesTextView: TextView = root.findViewById(R.id.numberOfTrades)
        val positiveTradesTextView: TextView = root.findViewById(R.id.positiveTrades)
        val negativeTradesTextView: TextView = root.findViewById(R.id.negativeTrades)
        tradingViewModel.weekTrades.observe(viewLifecycleOwner, Observer { list ->
            numberOfTradesTextView.text = list.stream().filter { it.second != null}.collect(Collectors.toList()).size.toString()
            positiveTradesTextView.text = list.stream().filter { it.second != null && it.first.avgFillPrice!! < it.second!!.avgFillPrice!!}.collect(Collectors.toList()).size.toString()
            positiveTrades.setTextColor(Color.GREEN)
            negativeTradesTextView.text = list.stream().filter { it.second != null && it.first.avgFillPrice!! > it.second!!.avgFillPrice!!}.collect(Collectors.toList()).size.toString()
            negativeTrades.setTextColor(Color.RED)
        })

        return root
    }
}