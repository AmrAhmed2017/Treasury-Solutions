package com.example.treasurysolutions.mapper

import com.example.sharedpreference.model.BankModel
import com.example.sharedpreference.model.CurrencyModel
import com.example.treasurysolutions.datalayer.model.BankSelectionModel
import com.example.treasurysolutions.datalayer.model.CurrencySelectionModel

class CurrencyModelMapper() {

    companion object {

        fun map(currencyModelList: List<CurrencyModel>): List<CurrencySelectionModel> {
            val currencies = mutableListOf<CurrencySelectionModel>()
            currencyModelList.forEach {
                currencies.add(
                    CurrencySelectionModel(
                        arabicAbbreviation = it.arabicAbbreviation,
                        symbol = it.symbol,
                        imageUrl = it.imageUrl,
                        name = it.name,
                        abbreviation = it.abbreviation,
                        id = it.id,
                        selected = false
                    )
                )

            }
            return currencies
        }

        fun map(currencySelectionModel: CurrencySelectionModel) = CurrencyModel(
            arabicAbbreviation = currencySelectionModel.arabicAbbreviation,
            symbol = currencySelectionModel.symbol,
            imageUrl = currencySelectionModel.imageUrl,
            name = currencySelectionModel.name,
            abbreviation = currencySelectionModel.abbreviation,
            id = currencySelectionModel.id
        )
    }
}