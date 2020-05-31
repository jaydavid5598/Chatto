package com.david.myapplication.covid19.api_services

import com.david.myapplication.covid19.data_model.CountryDataRequest
import retrofit2.Call
import retrofit2.http.GET

interface CovidService {
    @GET("/v2/countries")
    fun fetchAllData(): Call<List<CountryDataRequest>>
}
