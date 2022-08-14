package com.example.treasurysolutions.presentation.configurations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.network.BaseResponse
import com.example.sharedpreference.model.ExchangeRatesResponse
import com.example.treasurysolutions.datalayer.repo.ConfigurationsRepo
import com.example.sharedpreference.model.ConfigurationResponse
import com.example.treasurysolutions.datalayer.model.BankSelectionModel
import com.example.treasurysolutions.datalayer.model.CurrencySelectionModel
import com.example.treasurysolutions.mapper.BankModelMapper
import com.example.treasurysolutions.mapper.CurrencyModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfigurationsViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var configurationsRepo: ConfigurationsRepo

    val configMutableLiveData = MutableLiveData<BaseResponse<ConfigurationResponse>>()
    var selectedBanksList = listOf<BankSelectionModel>()
    var selectedCurrenciesList = listOf<CurrencySelectionModel>()

    fun getConfigurations() {
        viewModelScope.launchCatching(
            block = {
                val response = configurationsRepo.getConfigurations()
                response.data?.let {
                    it.banks.let {

                        selectedBanksList = BankModelMapper.map(it)
                    }

                    it.currencies.let {

                        selectedCurrenciesList = CurrencyModelMapper.map(it)
                    }
                }
                configMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun setConfigurations(config: ConfigurationResponse) {
        configurationsRepo.setConfigurations(config)
    }

    fun getBankMapper(bankSelectionModel: BankSelectionModel) = BankModelMapper.map(bankSelectionModel)

    fun getCurrencyMapper(currencySelectionModel: CurrencySelectionModel) = CurrencyModelMapper.map(currencySelectionModel)
}