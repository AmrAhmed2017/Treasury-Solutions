package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class EGPCurrenciesRatesResponse(
    @SerializedName("toCurrency") val toCurrencyModel: ExchangeToCurrencyModel?,
    @SerializedName("exchangeRates") val exchangeRates: List<EGPCurrenciesRatesModel>?,
)
