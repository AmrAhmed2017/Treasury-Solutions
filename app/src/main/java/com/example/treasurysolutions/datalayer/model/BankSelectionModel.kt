package com.example.treasurysolutions.datalayer.model

data class BankSelectionModel(
    var websiteUrl: String?,
    val imageUrl: String?,
    var name: String?,
    var abbreviation: String?,
    var id: Int,
    var selected: Boolean = false
)
