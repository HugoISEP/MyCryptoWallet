package com.example.mycryptowallet.ui.trading

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mycryptowallet.R
import com.example.mycryptowallet.service.Constant
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_trading_settings.*


class TradingSettingsActivity: AppCompatActivity(){
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var initialInvestmentTextView: TextInputEditText
    private lateinit var notificationSwitchView: SwitchMaterial
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trading_settings)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        initialInvestmentTextView = findViewById(R.id.textField)
        notificationSwitchView = findViewById(R.id.notificationSwitch)
        saveButton = findViewById(R.id.saveButton)

        notificationSwitchView.isChecked = sharedPreferences.getBoolean(Constant.NOTIFICATION_PREFERENCE, true)
        initialInvestmentTextView.setText(sharedPreferences.getFloat(Constant.INITIAL_TRADING_WALLET, 0f).toString())
        initialInvestmentTextView.addTextChangedListener(textWatcher)

        saveButton.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(initialInvestmentTextView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                sharedPreferences.edit().putFloat(Constant.INITIAL_TRADING_WALLET, initialInvestmentTextView.text.toString().toFloat()).apply()
                sharedPreferences.edit().putBoolean(Constant.NOTIFICATION_PREFERENCE, notificationSwitch.isChecked).apply()
            }
            finish()
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val usernameInput: String = initialInvestmentTextView.text.toString().trim()
            saveButton.isEnabled = usernameInput.isNotEmpty()
        }
        override fun afterTextChanged(s: Editable) {}
    }

}
