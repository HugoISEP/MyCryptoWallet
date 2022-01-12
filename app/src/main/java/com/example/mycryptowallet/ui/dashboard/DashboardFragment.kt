package com.example.mycryptowallet.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycryptowallet.NewCryptoActivity
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.CryptosApplication
import com.example.mycryptowallet.data.Entity.Crypto
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardFragment : Fragment() {

    val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory((requireActivity().application as CryptosApplication).repository)
    }
    private val newCryptoActivityRequestCode = 1

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



//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

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
        } else {
            Toast.makeText(
                requireContext().applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }


}