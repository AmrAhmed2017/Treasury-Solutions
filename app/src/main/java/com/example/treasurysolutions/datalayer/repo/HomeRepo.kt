package com.example.treasurysolutions.datalayer.repo

import com.example.sharedpreference.model.ExchangeRatesResponse
import com.example.core.base.BaseRepo
import com.example.network.Endpoints
import com.example.sharedpreference.Constant
import com.example.sharedpreference.model.EGPCurrenciesRatesResponse
import com.example.sharedpreference.model.GraphResponse
import com.example.treasurysolutions.datalayer.RateSort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepo @Inject constructor() : BaseRepo() {

    suspend fun getExchangeRates() = withContext(Dispatchers.IO) {
        val bankId = getSelectedBank()?.id
        val currencyId = getSelectedCurrency()?.id
        networkManager.getRequest(
            api = Endpoints.EXCHANGE_RATES + "$currencyId/$bankId",
            parseClass = ExchangeRatesResponse::class.java
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

    suspend fun getGraphData() = withContext(Dispatchers.IO) {
        val fromCurrencyId = getSelectedCurrency()?.id
        val toCurrencyId = getDollarCurrency()?.id
        networkManager.getRequest(
            api = Endpoints.GRAPH_RATES + "$fromCurrencyId/$toCurrencyId",
            parseClass = GraphResponse::class.java
        )
    }

    suspend fun getSavedBanks() = withContext(Dispatchers.IO){
        val config = sharedPrefManager.getConfig()
        config?.banks
    }

    suspend fun getDollarCurrency() = withContext(Dispatchers.IO){
        val config = sharedPrefManager.getConfig()
        config?.currencies?.get(1)
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