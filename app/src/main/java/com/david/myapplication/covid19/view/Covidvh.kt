package com.david.myapplication.covid19.view
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.david.myapplication.R
import com.david.myapplication.covid19.data_model.CountryDataRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.covid_data_layout.view.*

class Covidvh(private val country : List<CountryDataRequest>) : RecyclerView.Adapter<Covidvh.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.covid_data_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = country.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = country[position]
        Picasso.get().load(pos.countryInfo.flag).into(holder.countryFlag)
        holder.countryName.text = pos.country
        holder.countryCases.text = pos.cases.toString()
        holder.countryRecovered.text = pos.recovered.toString()
        holder.countryDeaths.text = pos.deaths.toString()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryFlag: ImageView = itemView.country_flag
        val countryName: TextView = itemView.country_name
        val countryCases: TextView = itemView.country_cases
        val countryRecovered: TextView = itemView.country_recovered
        val countryDeaths: TextView = itemView.country_deaths

    }

}