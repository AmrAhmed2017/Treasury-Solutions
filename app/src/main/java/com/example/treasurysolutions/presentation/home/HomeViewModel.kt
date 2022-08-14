package com.example.treasurysolutions.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.network.BaseResponse
import com.example.sharedpreference.model.*
import com.example.treasurysolutions.datalayer.repo.ConfigurationsRepo
import com.example.treasurysolutions.datalayer.repo.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {


    @Inject
    lateinit var homeRepo: HomeRepo

    val ratesMutableLiveData = MutableLiveData<BaseResponse<ExchangeRatesResponse>>()
    val bankRatesMutableLiveData = MutableLiveData<BaseResponse<ExchangeRatesResponse>>()
    val banksMutableLiveData = MutableLiveData<List<BankModel>?>()
    val savedRatesMutableLiveData = MutableLiveData<ExchangeRatesResponse?>()
    val graphRatesMutableLiveData = MutableLiveData<GraphResponse?>()
    val egpCurrenciesRatesMLD = MutableLiveData<BaseResponse<EGPCurrenciesRatesResponse>>()
    val savedBankMutableLiveData = MutableLiveData<BankModel?>()
    val savedCurrencyMutableLiveData = MutableLiveData<CurrencyModel?>()
    var selectedCurrency: ToCurrencyModel? = null
    var selectedBank: BankModel? = null

    fun getExchangeRates() {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getExchangeRates()
                ratesMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getBankExchangeRates(bankId: Int) {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getBankExchangeRates(bankId)
                bankRatesMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getCurrenciesRatesToEGP() {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getCurrenciesRatesToEGP(selectedCurrency?.toCurrencyId)
                egpCurrenciesRatesMLD.value = response
            }, onError = ::handleError
        )
    }

    fun getSavedBanks() {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getSavedBanks()
                banksMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getSavedExchangeRates() {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getSavedExchangeRates()
                savedRatesMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getGraphData() {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getGraphData()
                graphRatesMutableLiveData.value = response.data
            }, onError = ::handleError
        )
    }

    fun setExchangeRates(config: ExchangeRatesResponse) {
        homeRepo.setExchangeRates(config)
    }

    fun getSelectedBank() {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getSelectedBank()
                savedBankMutableLiveData.value = response
            }, onError = ::handleError
        )
    }

    fun getSelectedCurrency() {
        viewModelScope.launchCatching(
            block = {
                val response = homeRepo.getSelectedCurrency()
                savedCurrencyMutableLiveData.value = response
            }, onError = ::handleError
        )
    }
}