package com.example.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.ErrorResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    inline fun CoroutineScope.launchCatching(
        noinline block: suspend CoroutineScope.() -> Unit,
        crossinline onError: (Throwable) -> Unit,

        ) {
        launch(
            CoroutineExceptionHandler { _, throwable -> onError(throwable) },
            block = block
        )
    }

    val errorLiveData: MutableLiveData<ErrorResponse> by lazy {
        MutableLiveData<ErrorResponse>()
    }

    fun handleError(t: Throwable) {
        val error = ErrorResponse(t.message)
        errorLiveData.postValue(error)
    }


    /**
     * this function is used to prevent duplicated code in
     * error handling.
     * for example, look at "handlePromoCodeError" function in
     * "BookingJourneyViewModel"
     * **/
//    fun errorResponse(t: Throwable) = when (t) {
//        is com.example.network.ErrorResponse -> {
//            t
//        }
//        is HttpException -> {
//            ErrorResponse(StringLocale.instance.localize(R.string.network_error))
//        }
//        is UnknownHostException -> {
//            ErrorResponse(StringLocale.instance.localize(R.string.no_internet))
//        }
//        else -> {
//            ErrorResponse("something went wrong")
//        }
//    }

//    fun setCurrentAppVersion(appVersion: String) {
//        prefRepo.setCurrentAppVersion(appVersion)
//    }
//
//    fun getCurrentAppVersion() = prefRepo.getCurrentAppVersion()
}