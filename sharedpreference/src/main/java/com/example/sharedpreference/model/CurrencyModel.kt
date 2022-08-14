package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class CurrencyModel(
    @SerializedName("abbreviationAr") val arabicAbbreviation: String?,
    val symbol: String?,
    val imageUrl: String?,
    val name: String?,
    val abbreviation: String?,
    val id: Int
)
