package com.example.mycryptowallet.service

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*


class LineChartXAxisTimeFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {

        // Convert float value to date string
        val emissionsSecondsSince1970Time = value.toLong()

        // Formatted Date
        val sdf = SimpleDateFormat("dd/MM")
        return sdf.format(Date(emissionsSecondsSince1970Time))
    }
}

class LineChartXAxisHourTimeFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {

        // Convert float value to date string
        val emissionsSecondsSince1970Time = value.toLong()

        // Formatted Date
        val sdf = SimpleDateFormat("HH'h'")
        return sdf.format(Date(emissionsSecondsSince1970Time))
    }
}