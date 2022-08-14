package com.example.treasurysolutions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.network.BaseResponse
import com.example.sharedpreference.model.*
import com.example.treasurysolutions.datalayer.model.BankSelectionModel
import com.example.treasurysolutions.datalayer.model.CurrencySelectionModel
import com.example.treasurysolutions.datalayer.repo.ConfigurationsRepo
import com.example.treasurysolutions.datalayer.repo.ConvertRepo
import com.example.treasurysolutions.datalayer.repo.HomeRepo
import com.example.treasurysolutions.mapper.BankModelMapper
import com.example.treasurysolutions.mapper.CurrencyModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var convertRepo: ConvertRepo

    val convertRatesMutableLiveData = MutableLiveData<BaseResponse<ConvertRatesResponse>>()
    val bankRatesMutableLiveData = MutableLiveData<BaseResponse<ExchangeRatesResponse>>()
    val banksMutableLiveData = MutableLiveData<List<BankSelectionModel>?>()
    val currenciesMutableLiveData = MutableLiveData<List<CurrencySelectionModel>?>()
    val savedRatesMutableLiveData = MutableLiveData<ExchangeRatesResponse?>()
    val egpCurrenciesRatesMLD = MutableLiveData<BaseResponse<EGPCurrenciesRatesResponse>>()
    val savedBankMutableLiveData = MutableLiveData<BankModel?>()
    val selectedCurrencyMutableLiveData = MutableLiveData<CurrencyModel?>()
    val dollarCurrencyMutableLiveData = MutableLiveData<CurrencyModel?>()
    val savedCurrencyMutableLiveData = MutableLiveData<CurrencyModel?>()
    var selectedCurrency: ToCurrencyModel? = null
    var selectedBank: BankModel? = null
    var selectedBanksList = listOf<BankSelectionModel>()

    fun getConvertRates(fromCurrencyId: Int, toCurrencyId: Int, bankId: Int, value: Int) {
        viewModelScope.launchCatching(
            block = {
                val response =
                    convertRepo.getConvertRates(fromCurrencyId, toCurrencyId, bankId, value)
                convertRatesMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getSavedBanks() {
        viewModelScope.launchCatching(
            block = {
                val response = convertRepo.getSavedBanks()
                response?.let {

                    banksMutableLiveData.value = BankModelMapper.map(response)
                }
            }, onError = ::handleError
        )
    }

    fun getSavedCurrencies() {
        viewModelScope.launchCatching(
            block = {
                val response = convertRepo.getSavedCurrencies()
                response?.let {

                    currenciesMutableLiveData.value = CurrencyModelMapper.map(response)
                }
            }, onError = ::handleError
        )
    }

    fun getDollarCurrency() {
        viewModelScope.launchCatching(
            block = {
                val response = convertRepo.getSavedCurrencies()
                dollarCurrencyMutableLiveData.value = response?.get(1)
            }, onError = ::handleError
        )
    }

    fun getSelectedBank() {
        viewModelScope.launchCatching(
            block = {
                val response = convertRepo.getSelectedBank()
                savedBankMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getSelectedCurrency() {
        viewModelScope.launchCatching(
            block = {
                val response = convertRepo.getSelectedCurrency()
                selectedCurrencyMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getBankMapper(banks: List<BankModel>) = BankModelMapper.map(banks)

    fun getBankMapper(bankSelectionModel: BankSelectionModel) =
        BankModelMapper.map(bankSelectionModel)

    fun getCurrencyMapper(currencySelectionModel: CurrencySelectionModel) =
        CurrencyModelMapper.map(currencySelectionModel)
}