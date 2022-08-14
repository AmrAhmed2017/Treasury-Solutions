package com.example.sharedpreference.model

import com.google.gson.annotations.SerializedName

data class GraphResponse(
    @SerializedName("weekValues") val weekValues: List<GraphModel>,
    @SerializedName("monthValues") val monthValues: List<GraphModel>,
    @SerializedName("yearValues") val yearValues: List<GraphModel>
    )
