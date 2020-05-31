package com.david.myapplication.news.api_services.retrofit_service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://newsapi.org"

class RetrofitService {
    companion object{
        fun <T> create(service:Class<T>):T{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(service)
        }
    }
}