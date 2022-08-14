package com.example.treasurysolutions.mapper

import com.example.sharedpreference.model.BankModel
import com.example.treasurysolutions.datalayer.model.BankSelectionModel

class BankModelMapper() {

    companion object {

        fun map(bankModelList: List<BankModel>): List<BankSelectionModel> {
            val banks = mutableListOf<BankSelectionModel>()
            bankModelList.forEach {
                banks.add(
                    BankSelectionModel(
                        id = it.id,
                        websiteUrl = it.websiteUrl,
                        abbreviation = it.abbreviation,
                        name = it.name,
                        selected = false,
                        imageUrl = it.imageUrl
                    )
                )

            }
            return banks
        }

        fun map(bankSelectionModel: BankSelectionModel) = BankModel(
            id = bankSelectionModel.id,
            name = bankSelectionModel.name,
            abbreviation = bankSelectionModel.abbreviation,
            websiteUrl = bankSelectionModel.websiteUrl,
            imageUrl = bankSelectionModel.imageUrl
        )
    }
}