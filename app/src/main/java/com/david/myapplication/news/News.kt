package com.david.myapplication.news

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.myapplication.R
import com.david.myapplication.news.api_services.NewsService
import com.david.myapplication.news.api_services.retrofit_service.RetrofitService
import com.david.myapplication.news.data_model.ArticleRequest
import com.david.myapplication.news.data_model.NewsDataRequest
import com.david.myapplication.news.view.Newsvh
import kotlinx.android.synthetic.main.frag_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class News : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_news, container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "News"
        fetchDataNews()
    }
    private fun fetchDataNews() {
        val api = RetrofitService.create(NewsService::class.java)
        api.fetchAllData().enqueue(object : Callback<NewsDataRequest> {
            override fun onResponse(call: Call<NewsDataRequest>, response: Response<NewsDataRequest>) {
                d("News:",response.code().toString())
                response.body()?.let{ showData(it.articles) }
            }

            override fun onFailure(call: Call<NewsDataRequest>, t: Throwable) {
               d("News:",t.message.toString())
            }
        })
    }

    private fun showData(news : List<ArticleRequest>) {
        recyclerview_news.apply {
            layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
                    adapter = Newsvh(news)
        }
    }

}