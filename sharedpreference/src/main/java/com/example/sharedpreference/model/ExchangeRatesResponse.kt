package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
    @SerializedName("currentFromCurrency") val fromCurrency: FromCurrencyModel,
    @SerializedName("currentBank") val currentBank: CurrentBankModel,
    @SerializedName("exchangeRates") val exchangeRates: List<ToCurrencyModel>
)