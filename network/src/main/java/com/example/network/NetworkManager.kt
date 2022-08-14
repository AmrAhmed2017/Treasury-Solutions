package com.example.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Path
import java.io.IOException
import javax.inject.Inject

class NetworkManager@Inject constructor() {

    val networkModule = NetworkModule()

    private val headers: MutableMap<String, String> =
        object : HashMap<String, String>() {
            init {
                put(Constant.ACCEPT, Constant.APPLICATION_JSON)
            }
        }

    private fun updateHeaders() {

        headers[Constant.ACCEPT_LANGUAGE] = "en"
    }

    suspend fun <T> getRequest(
        api: String,
        param: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).getRequest(api, headers, param), parseClass
            )
        }
    }

    suspend fun <T> getRequest(
        api: String,
//        fromCurrencyId: String,
//        toCurrencyId: String,
//        orderBy: String,
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).getRequestByPathParam(api, headers), parseClass
            )
        }
    }

    suspend fun <T> postRequest(
        api: String,
        body: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).postRequest(api, headers, body), parseClass
            )
        }
    }

    suspend fun <T> putRequest(
        api: String,
        body: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).putRequest(api, headers, body), parseClass
            )
        }
    }

    suspend fun <T> putRequestWithQuery(
        api: String,
        param: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).putRequestWithQuery(api, headers, param), parseClass
            )
        }
    }

    suspend fun <T> deleteRequest(
        api: String,
        param: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).deleteRequest(api, headers, param), parseClass
            )
        }
    }

    suspend fun <T> postMultiPart(
        api: String,
        body: Map<String, Any> = HashMap(),
        file: MultipartBody.Part,
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).postMultiPart(api, headers, body, file), parseClass
            )
        }
    }

    suspend fun <T> postMultiPart(
        api: String,
        body: Map<String, RequestBody?> = HashMap(),
        multipleFiles: List<MultipartBody.Part?>,
        singleFile: MultipartBody.Part,
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).postMultiPart(api, headers, body, multipleFiles = multipleFiles, singleFile = singleFile), parseClass
            )
        }
    }

    suspend fun <T> putMultiPart(
        api: String,
        body: Map<String, Any> = HashMap(),
        file: MultipartBody.Part,
        parseClass: Class<T>,
    ): BaseResponse<T> {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                networkModule.provideRetrofitClient().create(
                    APIService::class.java
                ).putMultiPart(api, headers, body, file), parseClass
            )
        }
    }

    @Throws(
        ErrorResponse::class,
        IOException::class,
        InstantiationException::class,
        IllegalAccessException::class,
        JSONException::class
    )
    private fun <T> parseResponse(
        response: Response<JsonElement>,
        parseClass: Class<T>,
    ): BaseResponse<T> {
        return try {
            val gson = GsonBuilder().serializeNulls().create()

            if (!response.isSuccessful) {
                var errorResponse = ErrorResponse()
                if (response.errorBody() != null) {
                    try {
                        val obj = JSONObject(response.errorBody()!!.string())
                        try {
                            val temp = Gson().fromJson(obj.toString(), ErrorResponse::class.java)
                            temp?.let { errorResponse = it }
                        } catch (e: IllegalStateException) {
                        }
                    } catch (ex: JSONException) {
                    }
                }
                errorResponse.code = response.code()
                errorResponse.name = response.message()
                throw errorResponse
            } else {

                BaseResponse<T>().apply {
                    if (response.body()!!.asJsonObject.has(Constant.MESSAGE))
                        message = response.body()!!.asJsonObject[Constant.MESSAGE].asString
                    data = gson.fromJson(response.body()!!.asJsonObject[Constant.DATA], parseClass)
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}