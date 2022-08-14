package com.example.treasurysolutions.datalayer.repo

import com.example.core.base.BaseRepo
import com.example.network.Endpoints
import com.example.sharedpreference.model.ExchangeRatesResponse
import com.example.sharedpreference.model.ConfigurationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfigurationsRepo @Inject constructor(): BaseRepo() {

    suspend fun getConfigurations() = withContext(Dispatchers.IO) {
        networkManager.getRequest(
            api = Endpoints.CONFIGURATIONS,
            parseClass = ConfigurationResponse::class.java
        )
    }

    fun setConfigurations(config: ConfigurationResponse){
        sharedPrefManager.setConfig(config)
    }

}