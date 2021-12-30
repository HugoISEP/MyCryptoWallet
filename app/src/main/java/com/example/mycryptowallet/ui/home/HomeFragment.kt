package com.example.mycryptowallet.ui.home

import android.graphics.Color.BLACK
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mycryptowallet.R
import com.example.mycryptowallet.model.CryptoApi
import com.example.mycryptowallet.model.TimeInterval
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val cardView = root.findViewById(R.id.card) as MaterialCardView
        val pieChart = root.findViewById(R.id.pieChart) as PieChart
        initializePieChart(pieChart)
        homeViewModel.pieChartData.observe(viewLifecycleOwner, Observer<PieDataSet> { it ->
            it.valueTextSize = 15f
            it.valueTextColor = BLACK
            pieChart.centerText = String.format(
                "%.2f $",
                it.values.sumByDouble { it.value.toDouble() })
            pieChart.data = PieData(it)
            pieChart.animateXY(1000, 1000);
            pieChart.invalidate()
        })

        val lineChart = root.findViewById(R.id.lineChart) as LineChart
        homeViewModel.lineDataSet().observe(viewLifecycleOwner, Observer<LineDataSet> { it ->
            lineChart.description.isEnabled = false
            lineChart.visibility = View.VISIBLE
            lineChart.data = LineData(it)
            lineChart.animateXY(1000, 1000)
            lineChart.invalidate()
        })

        pieChart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    cardView.visibility = View.VISIBLE
                    lineChart.visibility = View.INVISIBLE
                    setCardValues(root, e)
                }
            }

            override fun onNothingSelected() {
                cardView.visibility = View.INVISIBLE
                lineChart.visibility = View.INVISIBLE
            }

        })

        return root
    }

    private fun initializePieChart(pieChart: PieChart){
        pieChart.setNoDataText("Loading ...")
        pieChart.isClickable = true
        pieChart.setEntryLabelColor(BLACK)
        pieChart.setCenterTextSize(30F)
        pieChart.setNoDataTextColor(BLACK)
        pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD)
        pieChart.description.isEnabled = false
        pieChart.legend.setDrawInside(true)
        pieChart.extraBottomOffset = 10f
        pieChart.isRotationEnabled = false
    }

    private fun setCardValues(root: View, entry: Entry){
        val crypto = entry.data as CryptoApi
        val cryptoSymbol = crypto.symbol.split("USDT")[0]
        Log.d("setOnChartValueSelected", crypto.symbol.split("USDT").toString())

        val titleView = root.findViewById<TextView>(R.id.cardTitle)
        titleView.text = crypto.symbol

        val subTitleView = root.findViewById<TextView>(R.id.subTitle)
        subTitleView.text = String.format("You own %.2f %s", entry.y, cryptoSymbol)

        val supportingTextView = root.findViewById<TextView>(R.id.supportingText)
        supportingTextView.text = String.format("Price: 1 %s = %.2f USD", cryptoSymbol, crypto.price.toFloat())

        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val oneYearButton = root.findViewById<MaterialButton>(R.id.oneYearButton)
        oneYearButton.setOnClickListener {
            homeViewModel.getCandlesData(TimeInterval.YEAR, crypto.symbol)
        }
        val oneMonthButton = root.findViewById<MaterialButton>(R.id.oneMonthButton)
        oneMonthButton.setOnClickListener {
            homeViewModel.getCandlesData(TimeInterval.MONTH, crypto.symbol)
        }
        val oneWeekButton = root.findViewById<MaterialButton>(R.id.oneWeekButton)
        oneWeekButton.setOnClickListener {
            homeViewModel.getCandlesData(TimeInterval.WEEK, crypto.symbol)
        }
        val oneDayButton = root.findViewById<MaterialButton>(R.id.oneDayButton)
        oneDayButton.setOnClickListener {
            homeViewModel.getCandlesData(TimeInterval.DAY, crypto.symbol)
        }

    }

}
