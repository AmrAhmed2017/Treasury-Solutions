package com.example.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.sharedpreference.Constant.BANK_KEY
import com.example.sharedpreference.Constant.CONFIG_KEY
import com.example.sharedpreference.Constant.CURRENCY_KEY
import com.example.sharedpreference.Constant.EXCHANGE_KEY
import com.example.sharedpreference.model.BankModel
import com.example.sharedpreference.model.ConfigurationResponse
import com.example.sharedpreference.model.CurrencyModel
import com.example.sharedpreference.model.ExchangeRatesResponse
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor(@ApplicationContext context: Context) {

    private var encryptedSharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = encryptedSharedPreferences.edit()
    }

    fun setString(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    private fun getString(key: String) = encryptedSharedPreferences.getString(key, null)

    private fun setInteger(key: String, value: Int){
        editor.putInt(key, value)
        editor.commit()
    }

    private fun getInteger(key: String) = encryptedSharedPreferences.getInt(key, -1)

    fun setConfig(value: ConfigurationResponse){
        val jsonText = Gson().toJson(value)
        editor.putString(CONFIG_KEY, jsonText)
        editor.commit()
    }

    fun getConfig(): ConfigurationResponse?{
        val json = getString(CONFIG_KEY)
        return json?.let {
            Gson().fromJson(it, ConfigurationResponse::class.java)
        }
    }

    fun setExchangeRates(value: ExchangeRatesResponse){
        val jsonText = Gson().toJson(value)
        editor.putString(EXCHANGE_KEY, jsonText)
        editor.commit()
    }

    fun getExchangeRates(): ExchangeRatesResponse?{
        val json = getString(EXCHANGE_KEY)
        return json?.let {
            Gson().fromJson(it, ExchangeRatesResponse::class.java)
        }
    }

    private fun setObject(key: String, obj: Any){
        val jsonText = Gson().toJson(obj)
        editor.putString(key, jsonText)
        editor.commit()
    }

    private fun getObject(key: String): BankModel?{
        val json = getString(key)
        return json?.let {
            Gson().fromJson(it, BankModel::class.java)
        }
    }

    fun setSelectedBank(bank: BankModel){
        setObject(BANK_KEY, bank)
    }

    fun getSelectedBank(): BankModel?{
        val json = getString(BANK_KEY)
        return json?.let {
            Gson().fromJson(it, BankModel::class.java)
        }
    }

    fun setSelectedCurrency(currency: CurrencyModel){
        setObject(CURRENCY_KEY, currency)
    }

    fun getSelectedCurrency(): CurrencyModel?{
        val json = getString(CURRENCY_KEY)
        return json?.let {
            Gson().fromJson(it, CurrencyModel::class.java)
        }
    }
}