package com.example.treasurysolutions.datalayer.repo

import com.example.core.base.BaseRepo
import com.example.network.Endpoints
import com.example.sharedpreference.model.MetalResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MetalRepo @Inject constructor() : BaseRepo() {

    suspend fun getMetalPrices() = withContext(Dispatchers.IO) {
        val currencyId = getSelectedCurrency()?.id
        networkManager.getRequest(
            api = Endpoints.METAL_PRICES + "$currencyId",
            parseClass = MetalResponse::class.java
        )
    }

    private suspend fun getSelectedCurrency() = withContext(Dispatchers.IO){
        sharedPrefManager.getSelectedCurrency()
    }
}