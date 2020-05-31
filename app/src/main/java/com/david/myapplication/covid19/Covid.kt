package com.david.myapplication.covid19
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.myapplication.R
import com.david.myapplication.covid19.api_services.CovidService
import com.david.myapplication.covid19.api_services.retrofit_service.RetrofitService
import com.david.myapplication.covid19.data_model.CountryDataRequest
import com.david.myapplication.covid19.view.Covidvh
import kotlinx.android.synthetic.main.frag_covid.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Covid : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_covid, container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Covid-19 Status"
        fetchDataCovid()

    }

    private fun fetchDataCovid() {
        val api = RetrofitService.create(CovidService::class.java)
        api.fetchAllData().enqueue(object : Callback<List<CountryDataRequest>>{
            override fun onResponse(call: Call<List<CountryDataRequest>>, response: Response<List<CountryDataRequest>>) {
                d("CONNECT:", response.code().toString())
                response.body()?.let { showData(it) }

            }
            override fun onFailure(call: Call<List<CountryDataRequest>>, t: Throwable) {
                d("CONNECT:", t.message.toString())
            }
        })
    }

    private fun showData(country: List<CountryDataRequest>) {
        recyclerview_covid.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            adapter = Covidvh(country)
        }
    }

}