package com.example.weatherapi.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.databinding.ItemWeatherBinding
import com.example.weatherapi.model.network.response.ForeCastResponse

class ForeCastAdapter(private val forecast: MutableList<ForeCastResponse>) :
    RecyclerView.Adapter<ForeCastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(forecastNew: List<ForeCastResponse>) {
        forecast.addAll(forecastNew)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ItemWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = forecast.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}