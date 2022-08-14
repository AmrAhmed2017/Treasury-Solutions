package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class ToCurrencyModel(
    @SerializedName("name") val name: String,
    @SerializedName("toCurrencyId") val toCurrencyId: Int,
    @SerializedName("buyPrice") val buyPrice: Double,
    @SerializedName("sellPrice") var sellPrice: Double,
    @SerializedName("lastUpdate") val lastUpdate: String?,
    @SerializedName("imageUrl") val imageUrl: String?
)
