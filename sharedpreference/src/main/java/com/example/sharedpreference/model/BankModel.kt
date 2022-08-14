package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class BankModel(
    @SerializedName("websiteUrl") val websiteUrl: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("abbreviation") val abbreviation: String?,
    @SerializedName("id") val id: Int
)
