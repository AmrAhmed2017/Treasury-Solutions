package com.example.network

import com.google.gson.annotations.SerializedName

class BaseResponse<T> {
    @SerializedName("data")
    var data: T? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("errorList")
    val errorList: ArrayList<String> = ArrayList()

}