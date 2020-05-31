package com.david.myapplication.news.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.david.myapplication.R
import com.david.myapplication.news.data_model.ArticleRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_data_layout.view.*

class Newsvh (val news : List<ArticleRequest>) : RecyclerView.Adapter<Newsvh.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_data_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val pos = news[position]
        holder.newsName?.text = pos.source.name
        holder.newsAuthor?.text = pos.author
        holder.newsDescription?.text = pos.description
        holder.newsLink?.text = pos.url
        holder.newsTitle?.text = pos.title
        holder.newsPublishedAt?.text = pos.publishedAt
        Picasso.get().load(pos.urlToImage).into(holder.newsImage)
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val newsName: TextView? = itemView.news_name
    val newsDescription: TextView? = itemView.news_description
    val newsAuthor: TextView? = itemView.news_author
    val newsLink: TextView? = itemView.news_link
    val newsPublishedAt: TextView? = itemView.news_publishedAt
    val newsTitle: TextView? = itemView.news_title
    val newsImage: ImageView? = itemView.news_image
    }

}