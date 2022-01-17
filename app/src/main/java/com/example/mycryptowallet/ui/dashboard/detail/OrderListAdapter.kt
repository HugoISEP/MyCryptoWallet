package com.example.mycryptowallet.ui.dashboard.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.Entity.Order
import com.example.mycryptowallet.data.Entity.OrderType

class OrderListAdapter : ListAdapter<Order, OrderListAdapter.ViewHolder>(OrderComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountTextView: TextView = itemView.findViewById(R.id.orderAmount)
        private val typeTextView: TextView = itemView.findViewById(R.id.orderType)
        private val tokenTextView: TextView = itemView.findViewById(R.id.orderToken)
        private val amountValueTextView: TextView = itemView.findViewById(R.id.orderAmountValue)
        private val currencyTextView: TextView = itemView.findViewById(R.id.orderCurrency)

        fun bind(order: Order) {
            typeTextView.text = if (order.type == OrderType.BUY) {
                "+"
            } else {
                "-"
            }
            amountTextView.text = order.amount.toString()
            tokenTextView.text = order.cryptoToken
            amountValueTextView.text = order.amountValue.toString()
            currencyTextView.text = "USD"
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
                return ViewHolder(view)
            }
        }
    }



    class OrderComparator : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.amount == newItem.amount
        }
    }
}