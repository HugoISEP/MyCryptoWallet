package com.example.mycryptowallet.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycryptowallet.NewCryptoActivity
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.CryptosApplication
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.ui.dashboard.detail.CryptoDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardFragment : Fragment() {

    val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory((requireActivity().application as CryptosApplication).cryptoRepository)
    }
    private val newCryptoActivityRequestCode = 1
    private val cryptoDetailActivityRequestCode = 2

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CryptoListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(root.context)

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val intent = Intent(root.context, CryptoDetailActivity::class.java)
                        intent.putExtra("crypto", adapter.currentList[position])
                        startActivityForResult(intent, cryptoDetailActivityRequestCode)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        val intent = Intent(root.context, CryptoDetailActivity::class.java)
                        intent.putExtra("crypto", adapter.currentList[position])
                        startActivityForResult(intent, cryptoDetailActivityRequestCode)

                    }
                })
        )

        dashboardViewModel.allCryptos.observe(viewLifecycleOwner) {
                cryptos -> cryptos.let { adapter.submitList(it) }
        }

        val fab = root.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(root.context, NewCryptoActivity::class.java)
            startActivityForResult(intent, newCryptoActivityRequestCode)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newCryptoActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringArrayListExtra(NewCryptoActivity.EXTRA_REPLY)?.let { reply ->
                val crypto = Crypto(reply[0], reply[1], 1.0, 0.0, 0.0)
                dashboardViewModel.insert(crypto)
            }
        }
    }


}