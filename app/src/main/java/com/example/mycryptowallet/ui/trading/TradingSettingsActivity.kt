package com.example.mycryptowallet.ui.trading

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mycryptowallet.R
import com.example.mycryptowallet.service.Constant
import com.example.mycryptowallet.service.Constant.NOTIFICATION_TOPIC
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_trading_settings.*


class TradingSettingsActivity: AppCompatActivity(){
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var initialInvestmentTextView: TextInputEditText
    private lateinit var notificationSwitchView: SwitchMaterial
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Init activity and components
        setContentView(R.layout.activity_trading_settings)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        initialInvestmentTextView = findViewById(R.id.textField)
        notificationSwitchView = findViewById(R.id.notificationSwitch)
        saveButton = findViewById(R.id.saveButton)
        val items = resources.getStringArray(R.array.trade_duration)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, items)
        val materialAutoCompleteTextView = (textInputLayout.editText as MaterialAutoCompleteTextView)
        val editTextFilledExposedDropdown = findViewById<AutoCompleteTextView>(R.id.filled_exposed_dropdown)
        editTextFilledExposedDropdown.setAdapter(adapter)

        // Set form values
        val defaultTradeDuration = items.find { it.equals(sharedPreferences.getString(Constant.TRADE_DURATION, items.get(1))) }!!
        materialAutoCompleteTextView.setText(defaultTradeDuration,false)
        notificationSwitchView.isChecked = sharedPreferences.getBoolean(Constant.NOTIFICATION_PREFERENCE, false)
        initialInvestmentTextView.setText(sharedPreferences.getFloat(Constant.INITIAL_TRADING_WALLET, 0f).toString())
        initialInvestmentTextView.addTextChangedListener(textWatcher)

        // OnClick listener on the save button
        saveButton.setOnClickListener {
            sharedPreferences.edit().putFloat(Constant.INITIAL_TRADING_WALLET, initialInvestmentTextView.text.toString().toFloat()).apply()
            sharedPreferences.edit().putBoolean(Constant.NOTIFICATION_PREFERENCE, notificationSwitch.isChecked).apply()
            sharedPreferences.edit().putString(Constant.TRADE_DURATION, materialAutoCompleteTextView.text.toString()).apply()
            if (sharedPreferences.getBoolean(Constant.NOTIFICATION_PREFERENCE, true)){
                FirebaseMessaging.getInstance().subscribeToTopic(NOTIFICATION_TOPIC)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(NOTIFICATION_TOPIC)
            }
            finish()
        }
    }

    // Text watcher to disable the save button if no initial investment entered
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val initialInvestment: String = initialInvestmentTextView.text.toString().trim()
            saveButton.isEnabled = initialInvestment.isNotEmpty()
        }
        override fun afterTextChanged(s: Editable) {}
    }

}
