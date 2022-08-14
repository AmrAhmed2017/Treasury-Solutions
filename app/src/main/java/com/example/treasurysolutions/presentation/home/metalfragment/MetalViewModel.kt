package com.example.treasurysolutions.presentation.home.metalfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.network.BaseResponse
import com.example.sharedpreference.model.*
import com.example.treasurysolutions.datalayer.repo.MetalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MetalViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var metalRepo: MetalRepo

    val metalsMutableLiveData = MutableLiveData<BaseResponse<MetalResponse>>()

    fun getMetalPrices() {
        viewModelScope.launchCatching(
            block = {
                val response = metalRepo.getMetalPrices()
                metalsMutableLiveData.value = response
            }, onError = ::handleError
        )
    }
}