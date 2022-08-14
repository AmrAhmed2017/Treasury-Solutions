package com.example.treasurysolutions.datalayer.model

data class CurrencySelectionModel(
    var arabicAbbreviation: String?,
    var symbol: String?,
    var imageUrl: String?,
    var name: String?,
    var abbreviation: String?,
    var id: Int,
    var selected: Boolean = false
)
