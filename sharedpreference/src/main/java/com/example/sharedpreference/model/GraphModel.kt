package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class GraphModel(
    @SerializedName("date") val date: String,
    @SerializedName("buyPrice") val buyPrice: Double,
    @SerializedName("sellPrice") val sellPrice: Double,
)
