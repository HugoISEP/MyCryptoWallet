package com.example.mycryptowallet.ui.dashboard.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.example.mycryptowallet.R
import com.example.mycryptowallet.data.Entity.OrderType

class NewOrderActivity : AppCompatActivity() {
    private lateinit var editOrderAmount: EditText
    private lateinit var editOrderAmountValue: EditText
    private lateinit var buyRadioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        editOrderAmount = findViewById(R.id.edit_order_amount)
        editOrderAmountValue = findViewById(R.id.edit_order_amount_value)
        buyRadioButton = findViewById(R.id.buy_order)

        val saveButton = findViewById<Button>(R.id.button_save_order)
        saveButton.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editOrderAmount.text) || TextUtils.isEmpty(editOrderAmountValue.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val extras: ArrayList<String> = arrayListOf()
                extras.add(editOrderAmount.text.toString())
                extras.add(editOrderAmountValue.text.toString())
                if(buyRadioButton.isChecked) {
                    extras.add(OrderType.BUY.toString())
                } else {
                    extras.add(OrderType.SELL.toString())
                }
                replyIntent.putStringArrayListExtra("new order", extras)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}