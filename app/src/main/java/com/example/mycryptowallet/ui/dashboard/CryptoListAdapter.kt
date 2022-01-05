package com.example.mycryptowallet.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.Entity.Crypto

class CryptoListAdapter : ListAdapter<Crypto, CryptoListAdapter.DashboardViewHolder>(CryptoComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): DashboardViewHolder {
        return  DashboardViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name)
    }

    class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cryptoItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            cryptoItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): DashboardViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
                return DashboardViewHolder(view)
            }
        }
    }

    class CryptoComparator : DiffUtil.ItemCallback<Crypto>() {
        override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
            return oldItem.name == newItem.name
        }
    }
}