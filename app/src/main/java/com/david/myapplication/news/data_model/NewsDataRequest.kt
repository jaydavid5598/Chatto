package com.david.myapplication.news.data_model


import com.google.gson.annotations.SerializedName

data class NewsDataRequest(
    @SerializedName("articles")
    val articles: List<ArticleRequest>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)