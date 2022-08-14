package com.example.treasurysolutions.datalayer.repo

import com.example.sharedpreference.model.ExchangeRatesResponse
import com.example.core.base.BaseRepo
import com.example.network.Endpoints
import com.example.sharedpreference.Constant
import com.example.sharedpreference.model.ConvertRatesResponse
import com.example.sharedpreference.model.EGPCurrenciesRatesResponse
import com.example.treasurysolutions.datalayer.RateSort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConvertRepo @Inject constructor() : BaseRepo() {

    suspend fun getConvertRates(fromCurrencyId: Int, toCurrencyId: Int, bankId: Int, value: Int) = withContext(Dispatchers.IO) {
        networkManager.getRequest(
            api = Endpoints.CONVERT_RATES + "$fromCurrencyId/$toCurrencyId/$bankId/$value",
            parseClass = ConvertRatesResponse::class.java
        )
    }

    suspend fun getBankExchangeRates(bankId: Int) = withContext(Dispatchers.IO) {
        val currencyId = getSelectedCurrency()?.id
        networkManager.getRequest(
            api = Endpoints.EXCHANGE_RATES + "$currencyId/$bankId",
            parseClass = ExchangeRatesResponse::class.java
        )
    }

    suspend fun getCurrenciesRatesToEGP(toCurrencyId: Int?) = withContext(Dispatchers.IO) {
        val currencyId = getSelectedCurrency()?.id
        networkManager.getRequest(
            api = Endpoints.EXCHANGE_RATES + "$currencyId/$toCurrencyId/${RateSort.SELL.id}",
            parseClass = EGPCurrenciesRatesResponse::class.java
        )
    }

    suspend fun getSavedBanks() = withContext(Dispatchers.IO){
        val config = sharedPrefManager.getConfig()
        config?.banks
    }

    suspend fun getSavedCurrencies() = withContext(Dispatchers.IO){
        val config = sharedPrefManager.getConfig()
        config?.currencies
    }

    fun setExchangeRates(config: ExchangeRatesResponse){
        sharedPrefManager.setExchangeRates(config)
    }

    suspend fun getSavedExchangeRates() = withContext(Dispatchers.IO){
         sharedPrefManager.getExchangeRates()
    }

    suspend fun getSelectedBank() = withContext(Dispatchers.IO) {
        sharedPrefManager.getSelectedBank()
    }

    suspend fun getSelectedCurrency() = withContext(Dispatchers.IO){
        sharedPrefManager.getSelectedCurrency()
    }
}