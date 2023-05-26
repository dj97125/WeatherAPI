package com.example.weatherapi.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.common.IMAGES_URL
import com.example.weatherapi.common.IMAGE_FORMAT
import com.example.weatherapi.common.giveMeFormatDate
import com.example.weatherapi.databinding.ItemWeatherBinding
import com.example.weatherapi.model.network.response.ForeCast
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class ForeCastAdapter(
    private val forecast: MutableList<ForeCast> = mutableListOf()
) : RecyclerView.Adapter<ForeCastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(forecastNew: List<ForeCast>) {
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.binding.apply {
            tvTime.text = forecast[position].dt_txt?.giveMeFormatDate()?.substring(11)
            tvTemp.text = forecast[position].main?.temp?.roundToInt().toString() + "°"
            Picasso.get()
                .load(IMAGES_URL + forecast[position].weather?.first()?.icon + IMAGE_FORMAT)
                .into(ivWeather)
        }
    }
}