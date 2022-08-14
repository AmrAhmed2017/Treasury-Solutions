package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class ConvertRatesResponse(
    @SerializedName("convertedBankExchangeRate") val convertedBank: ConvertedBankExchangeRateModel?,
    @SerializedName("topThreeBanks") val topBanks: List<ConvertedBankExchangeRateModel>?,
)