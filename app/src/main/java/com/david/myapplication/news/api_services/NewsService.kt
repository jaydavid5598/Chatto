package com.david.myapplication.news.api_services

import com.david.myapplication.news.data_model.NewsDataRequest
import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
        @GET("/v2/top-headlines?country=ph&apiKey=fd0f86035f9a49bca075e66d72ea549a")
        fun fetchAllData(): Call<NewsDataRequest>
}
