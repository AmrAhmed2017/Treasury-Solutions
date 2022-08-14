package com.example.sharedpreference.model

data class ConfigurationResponse(

    val banks: List<BankModel>,
    val countries: List<CountryModel>,
    val currencies: List<CurrencyModel>

)