package com.example.sharedpreference.model

data class MetalResponse(
    val fromCurrencyId: Int,
    val goldPrices: List<GoldModel>,
    val silverPrices: List<SilverModel>
)