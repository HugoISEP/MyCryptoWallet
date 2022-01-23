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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.CryptosApplication
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.model.TimeInterval
import com.example.mycryptowallet.service.LineChartXAxisHourTimeFormatter
import com.example.mycryptowallet.service.LineChartXAxisTimeFormatter
import com.example.mycryptowallet.ui.dashboard.DashboardViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView


class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels {
        DashboardViewModelFactory((requireActivity().application as CryptosApplication).cryptoRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        initializePieChart(root)
        initializeLineChart(root)

        return root
    }

    private fun initializePieChart(root: View){
        // Default pieChart parameters
        val pieChart = root.findViewById(R.id.pieChart) as PieChart
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

        // LiveData for pieChart
        homeViewModel.pieChartData().observe(viewLifecycleOwner, Observer<PieDataSet> { it ->
            it.valueTextSize = 15f
            it.valueTextColor = BLACK
            pieChart.centerText = String.format(
                "%.2f $",
                it.values.sumByDouble { it.value.toDouble() })
            pieChart.data = PieData(it)
            pieChart.animateXY(1000, 1000);
            pieChart.invalidate()
        })

        // On click listener on PieChart
        val cardView = root.findViewById(R.id.card) as MaterialCardView
        val lineChart = root.findViewById(R.id.lineChart) as LineChart
        pieChart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
            override fun onValueSelected(entry: Entry?, h: Highlight?) {
                if (entry != null) {
                    cardView.visibility = View.VISIBLE
                    lineChart.visibility = View.GONE
                    setCardValues(root, entry)
                }
            }

            override fun onNothingSelected() {
                cardView.visibility = View.GONE
                lineChart.visibility = View.GONE
            }

        })
    }

    private fun initializeLineChart(root: View){
        val lineChart = root.findViewById(R.id.lineChart) as LineChart
        // LiveData for lineChart
        homeViewModel.lineDataSet().observe(viewLifecycleOwner, Observer<LineDataSet> { it ->
            if (homeViewModel.timeInterval.equals(TimeInterval.DAY)){
                lineChart.xAxis.valueFormatter = LineChartXAxisHourTimeFormatter()
            } else {
                lineChart.xAxis.valueFormatter = LineChartXAxisTimeFormatter()
            }
            it.setDrawValues(false)
            lineChart.xAxis.setDrawGridLines(false);
            lineChart.axisLeft.setDrawGridLines(false);
            lineChart.axisRight.setDrawGridLines(false);
            lineChart.legend.isEnabled = false;
            lineChart.xAxis.labelCount = 10
            lineChart.description.isEnabled = false
            lineChart.visibility = View.VISIBLE
            lineChart.data = LineData(it)
            lineChart.animateXY(1000, 1000)
            lineChart.invalidate()
        })
    }

    private fun setCardValues(root: View, entry: Entry){
        Log.d("setCardValues", entry.data.toString())
        val crypto = entry.data as Crypto
        val cryptoPair = crypto.token + "USDT"

        val titleView = root.findViewById<TextView>(R.id.cardTitle)
        titleView.text = crypto.token

        val subTitleView = root.findViewById<TextView>(R.id.subTitle)
        subTitleView.text = String.format("You own %.2f %s", crypto.amount, crypto.token)

        val supportingTextView = root.findViewById<TextView>(R.id.supportingText)
        supportingTextView.text = String.format("Price: 1 %s = %.2f$", crypto.token, crypto.currentValue)

        val totalValueTextView = root.findViewById<TextView>(R.id.totalValue)
        totalValueTextView.text = String.format("Total value: %.2f$", crypto.amount * crypto.currentValue!!)

        val oneYearButton = root.findViewById<MaterialButton>(R.id.oneYearButton)
        oneYearButton.setOnClickListener {
            homeViewModel.timeInterval = TimeInterval.YEAR
            homeViewModel.getCandlesData(cryptoPair)
        }
        val oneMonthButton = root.findViewById<MaterialButton>(R.id.oneMonthButton)
        oneMonthButton.setOnClickListener {
            homeViewModel.timeInterval = TimeInterval.MONTH
            homeViewModel.getCandlesData(cryptoPair)
        }
        val oneWeekButton = root.findViewById<MaterialButton>(R.id.oneWeekButton)
        oneWeekButton.setOnClickListener {
            homeViewModel.timeInterval = TimeInterval.WEEK
            homeViewModel.getCandlesData(cryptoPair)
        }
        val oneDayButton = root.findViewById<MaterialButton>(R.id.oneDayButton)
        oneDayButton.setOnClickListener {
            homeViewModel.timeInterval = TimeInterval.DAY
            homeViewModel.getCandlesData(cryptoPair)
        }
        homeViewModel.timeInterval = TimeInterval.DAY
        homeViewModel.getCandlesData(cryptoPair)
    }

}
