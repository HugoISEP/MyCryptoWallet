package com.example.mycryptowallet

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewCryptoActivity : AppCompatActivity() {
    private lateinit var editCryptoViewName: EditText
    private lateinit var editCryptoViewToken: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_crypto)
        editCryptoViewName = findViewById(R.id.edit_crypto_name)
        editCryptoViewToken = findViewById(R.id.edit_crypto_token)
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editCryptoViewName.text) || TextUtils.isEmpty(editCryptoViewToken.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val extras: ArrayList<String> = arrayListOf()
                extras.add(editCryptoViewName.text.toString())
                extras.add( editCryptoViewName.text.toString())
                replyIntent.putStringArrayListExtra(EXTRA_REPLY, extras)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.cryptolistsql.REPLY"
    }
}