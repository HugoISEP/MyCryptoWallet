package com.example.mycryptowallet.ui.trading

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycryptowallet.R
import com.example.mycryptowallet.model.CryptoOrder
import com.example.mycryptowallet.service.Constant.INITIAL_TRADING_WALLET
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
            if (it != null ){
                textView.text = String.format("%.2f $", it)

                val walletChangePercentage = it * 100 / initialWallet - 100
                walletPercentageTextView.setTextColor(if (walletChangePercentage > 0) Color.GREEN else Color.RED)
                walletPercentageTextView.text = String.format("%.2f %%", walletChangePercentage)
            }
        })

        tradingViewModel.balances.observe(viewLifecycleOwner, Observer {

            val totalUsdValue = it!!.sumOf { it.usdValue.toDouble() }
            textView.text = String.format("%.2f $", totalUsdValue)

            val walletChangePercentage = totalUsdValue * 100 / initialWallet - 100
            walletPercentageTextView.setTextColor(if (walletChangePercentage > 0) Color.GREEN else Color.RED)
            walletPercentageTextView.text = String.format("%.2f %%", walletChangePercentage)

            it.forEach { balance ->
                val chip = Chip(root.context)
                chip.text = String.format("%.2f %s", balance.free, balance.coin)
                chipGroup.addView(chip)
            }
        })

        val numberOfTradesTextView: TextView = root.findViewById(R.id.numberOfTrades)
        val positiveTradesTextView: TextView = root.findViewById(R.id.positiveTrades)
        val negativeTradesTextView: TextView = root.findViewById(R.id.negativeTrades)
        tradingViewModel.weekTrades.observe(viewLifecycleOwner, Observer { list ->
            val completeTrades = list.stream().filter { it.second != null}.collect(Collectors.toList())
            numberOfTradesTextView.text = completeTrades.size.toString()
            positiveTradesTextView.text = completeTrades.stream().filter { it.first.avgFillPrice!! < it.second!!.avgFillPrice!!}.collect(Collectors.toList()).size.toString()
            positiveTrades.setTextColor(Color.GREEN)
            negativeTradesTextView.text = completeTrades.stream().filter { it.first.avgFillPrice!! > it.second!!.avgFillPrice!!}.collect(Collectors.toList()).size.toString()
            negativeTrades.setTextColor(Color.RED)

            val layoutManager = LinearLayoutManager(root.context)
            val recyclerView: RecyclerView = root.findViewById(R.id.tradeRecyclerView)
            recyclerView.layoutManager = layoutManager
            val adapter = TradeRecyclerAdapter(completeTrades as List<Pair<CryptoOrder, CryptoOrder>>)
            recyclerView.adapter = adapter
        })

        val button = root.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        button.setOnClickListener {
            val intent = Intent(root.context, TradingSettingsActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}