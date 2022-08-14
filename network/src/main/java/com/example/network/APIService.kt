package com.example.network

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET
    @JvmSuppressWildcards
    suspend fun getRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @QueryMap param: Map<String, Any>?
    ): Response<JsonElement>

    @GET
    @JvmSuppressWildcards
    suspend fun getRequestByPathParam(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
//        @Path("fromCurrencyId") fromCurrencyId: String,
//        @Path("toCurrencyId") toCurrencyId: String,
//        @Path("orderby") orderBy: String
    ): Response<JsonElement>

    @POST
    @JvmSuppressWildcards
    suspend fun postRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @Body body: Map<String, Any>?
    ): Response<JsonElement>

    @PUT
    @JvmSuppressWildcards
    suspend fun putRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @Body body: Map<String, Any>?
    ): Response<JsonElement>


    @PUT
    @JvmSuppressWildcards
    suspend fun putRequestWithQuery(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @QueryMap param: Map<String, Any>?,
    ): Response<JsonElement>

    @DELETE
    @JvmSuppressWildcards
    suspend fun deleteRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @QueryMap param: Map<String, Any>?
    ): Response<JsonElement>

    @Multipart
    @POST
    @JvmSuppressWildcards
    suspend fun postMultiPart(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @PartMap body: Map<String, Any>?,
        @Part file: MultipartBody.Part?
    ): Response<JsonElement>

    @Multipart
    @POST
    @JvmSuppressWildcards
    suspend fun postMultiPart(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @PartMap body: Map<String, RequestBody?>?,
        @Part multipleFiles: List<MultipartBody.Part?>,
        @Part singleFile: MultipartBody.Part?
    ): Response<JsonElement>

    @Multipart
    @PUT
    @JvmSuppressWildcards
    suspend fun putMultiPart(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @PartMap body: Map<String, Any>?,
        @Part file: MultipartBody.Part?
    ): Response<JsonElement>
}