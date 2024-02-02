package lk.nibm.kotlinweatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.kotlinweatherapp.R

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    data class ForecastItem(val date: String, val temperature: String)

    private val forecastItems = mutableListOf<ForecastItem>()

    // ViewHolder class to hold references to views
    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val temperatureTextView: TextView = itemView.findViewById(R.id.temperatureTextView)
    }

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(itemView)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = forecastItems[position]
        holder.dateTextView.text = item.date
        holder.temperatureTextView.text = item.temperature
    }

    // Return the number of items in the data set
    override fun getItemCount(): Int {
        return forecastItems.size
    }

    // Update the adapter with new forecast data
    fun setForecastItems(items: List<ForecastItem>) {
        forecastItems.clear()
        forecastItems.addAll(items)
        notifyDataSetChanged()
    }
}
