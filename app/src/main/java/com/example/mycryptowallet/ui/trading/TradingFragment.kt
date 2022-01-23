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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mycryptowallet.R
import com.example.mycryptowallet.model.CryptoOrder
import com.example.mycryptowallet.model.FtxBalance
import com.example.mycryptowallet.service.Constant.INITIAL_TRADING_WALLET
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_trading.*
import java.util.stream.Collectors


class TradingFragment : Fragment() {

    private lateinit var tradingViewModel: TradingViewModel
    private var initialWallet = 0f

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tradingViewModel =
                ViewModelProvider(this).get(TradingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trading, container, false)

        swipeRefreshInitialization(root)
        initialInvestmentInitialization(root)

        tradingViewModel.balances.observe(viewLifecycleOwner, Observer {
            balancesInitialization(root, it)
        })

        tradingViewModel.weekTrades.observe(viewLifecycleOwner, Observer { list ->
            weekTradesInitialization(list, root)
        })

        tradingSettingButtonInitialization(root)

        return root
    }

    private fun swipeRefreshInitialization(root: View){
        val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener {
            tradingViewModel.refresh()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initialInvestmentInitialization(root: View){
        val initialWalletView: TextView = root.findViewById(R.id.initialWallet)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(root.context)
        initialWallet = sharedPreferences.getFloat(INITIAL_TRADING_WALLET, 0f)
        initialWalletView.text = String.format("initial: %.2f $", initialWallet)
    }


    private fun balancesInitialization(root: View, list: List<FtxBalance>){
        val walletPercentageTextView: TextView = root.findViewById(R.id.walletChangePercentage)
        val textView: TextView = root.findViewById(R.id.totalWallet)

        val totalUsdValue = list.sumOf { it.usdValue.toDouble() }
        textView.text = String.format("%.2f $", totalUsdValue)

        val walletChangePercentage = totalUsdValue * 100 / initialWallet - 100
        walletPercentageTextView.setTextColor(if (walletChangePercentage > 0) Color.GREEN else Color.RED)
        walletPercentageTextView.text = String.format("%.2f %%", walletChangePercentage)

        chipGroup.removeAllViews()
        list.forEach { balance ->
            val chip = Chip(root.context)
            chip.text = String.format("%.2f %s", balance.total, balance.coin)
            chipGroup.addView(chip)
        }
    }

    private fun weekTradesInitialization(list: MutableList<Pair<CryptoOrder, CryptoOrder?>>, root: View){
        val numberOfTradesTextView: TextView = root.findViewById(R.id.numberOfTrades)
        val positiveTradesTextView: TextView = root.findViewById(R.id.positiveTrades)
        val positiveTradesAverageTextView: TextView = root.findViewById(R.id.positiveTradesAverage)
        val negativeTradesTextView: TextView = root.findViewById(R.id.negativeTrades)
        val negativeTradesAverageTextView: TextView = root.findViewById(R.id.negativeTradesAverage)

        val completeTrades = list.filter { it.second != null}
        numberOfTradesTextView.text = completeTrades.size.toString()

        val positiveTrades = completeTrades.filter { it.first.avgFillPrice!! < it.second!!.avgFillPrice!!}
        val positiveTradesAverage = positiveTrades.sumOf { ((it.second!!.avgFillPrice!! - it.first.avgFillPrice!!) * 100 / it.first.avgFillPrice!!).toDouble()} / positiveTrades.size
        positiveTradesTextView.text = positiveTrades.size.toString()
        positiveTradesTextView.setTextColor(Color.GREEN)
        positiveTradesAverageTextView.text = String.format("%.2f%%", positiveTradesAverage)
        positiveTradesAverageTextView.setTextColor(Color.GREEN)

        val negativeTrades = completeTrades.filter { it.first.avgFillPrice!! >= it.second!!.avgFillPrice!!}
        val negativeTradesAverage = negativeTrades.sumOf { ((it.second!!.avgFillPrice!! - it.first.avgFillPrice!!) * 100 / it.first.avgFillPrice!!).toDouble()} / negativeTrades.size
        negativeTradesTextView.text = negativeTrades.size.toString()
        negativeTradesTextView.setTextColor(Color.RED)
        negativeTradesAverageTextView.text = String.format("%.2f%%", negativeTradesAverage)
        negativeTradesAverageTextView.setTextColor(Color.RED)

        val layoutManager = LinearLayoutManager(root.context)
        val recyclerView: RecyclerView = root.findViewById(R.id.tradeRecyclerView)
        recyclerView.layoutManager = layoutManager
        val adapter = TradeRecyclerAdapter(completeTrades as List<Pair<CryptoOrder, CryptoOrder>>)
        recyclerView.adapter = adapter
    }

    private fun tradingSettingButtonInitialization(root: View){
        val button = root.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        button.setOnClickListener {
            val intent = Intent(root.context, TradingSettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        tradingViewModel.refresh()
        initialInvestmentInitialization(requireView())
    }

}