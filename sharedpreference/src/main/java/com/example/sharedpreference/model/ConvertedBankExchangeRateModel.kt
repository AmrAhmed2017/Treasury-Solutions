package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class ConvertedBankExchangeRateModel(
    @SerializedName("buyPrice") val buyPrice: Double?,
    @SerializedName("sellPrice") val sellPrice: Double?,
    @SerializedName("lastUpdate") val lastUpdate: String?,
    @SerializedName("bankId") val bankId: Int?,
    @SerializedName("bank") val bank: BankModel?
)
