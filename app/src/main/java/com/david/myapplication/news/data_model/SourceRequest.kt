package com.david.myapplication.news.data_model


import com.google.gson.annotations.SerializedName

data class SourceRequest(
    @SerializedName("id")
    val id: Any,
    @SerializedName("name")
    val name: String
)