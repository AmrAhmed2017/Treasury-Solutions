package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class ExchangeToCurrencyModel(
    @SerializedName("name") val name: String,
    @SerializedName("abbreviation") val abbreviation: String,
    @SerializedName("id") val id: Int,
)
