package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class FromCurrencyModel(
    @SerializedName("abbreviationAr") val arabicAbb: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("abbreviation") val englishAbb: String?,
    @SerializedName("id") val id: Int
)
