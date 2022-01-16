package com.example.mycryptowallet.ui.trading

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycryptowallet.R
import com.example.mycryptowallet.model.CryptoOrder
import java.text.SimpleDateFormat
import java.util.*


class TradeRecyclerAdapter(private val dataSet: List<Pair<CryptoOrder, CryptoOrder>>): RecyclerView.Adapter<TradeRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.trade_card_item,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // market name
        holder.itemTitle.text = dataSet[position].first.market
        // number of coins
        holder.itemSecondaryText.text = String.format("%.2f %s",
            dataSet[position].first.filledSize,
            dataSet[position].first.market.split("/USD")[0])
        // date sell transaction
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.FRANCE)
        holder.itemDateText.text = sdf.format(dataSet[position].second?.createdAt)

        // diff price
        val diffPrice = dataSet[position].first.filledSize * (dataSet[position].second.avgFillPrice!! - dataSet[position].first.avgFillPrice!!)
        holder.itemSupportingText.text = String.format("%.2f USD", diffPrice)
        holder.itemSupportingText.setTextColor(if (diffPrice > 0) Color.GREEN else Color.RED)
    }

    override fun getItemCount() =  dataSet.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView = itemView.findViewById(R.id.title)
        var itemSecondaryText: TextView = itemView.findViewById(R.id.secondaryText)
        var itemSupportingText: TextView = itemView.findViewById(R.id.supportingText)
        var itemDateText: TextView = itemView.findViewById(R.id.date)
    }
}