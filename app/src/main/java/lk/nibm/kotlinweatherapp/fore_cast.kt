package lk.nibm.kotlinweatherapp

import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class fore_cast : AppCompatActivity() {

    private val apiKey = "b71ecedd9c8b464a07bfbb6576a34a12"
    private lateinit var locationAutoCompleteTextView: AutoCompleteTextView
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var locationTextView: TextView // Declare locationTextView

    private val adapter = ForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fore_cast)

        locationAutoCompleteTextView = findViewById(R.id.locationAutoCompleteTextView)
        searchButton = findViewById(R.id.searchButton)
        recyclerView = findViewById(R.id.recyclerView)
        locationTextView = findViewById(R.id.locationTextView) // Initialize locationTextView

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        recyclerView.adapter = adapter

        val cityAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            arrayOf("Colombo", "Kandy", "Galle")
        )
        locationAutoCompleteTextView.setAdapter(cityAdapter)

        searchButton.setOnClickListener {
            val selectedLocation = locationAutoCompleteTextView.text.toString()
            if (selectedLocation.isNotEmpty()) {
                WeatherTask().execute(selectedLocation)
            }
        }
    }

    inner class WeatherTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            try {
                val city = params[0] ?: return null
                val apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey"
                return URL(apiUrl).readText(Charsets.UTF_8)
            } catch (e: Exception) {
                e.printStackTrace() // Log the exception
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                if (result != null) {
                    val jsonObj = JSONObject(result)
                    val list = jsonObj.getJSONArray("list")
                    val forecastList = mutableListOf<ForecastAdapter.ForecastItem>()

                    for (i in 0 until 4) {
                        val day = list.getJSONObject(i * 8)
                        val main = day.getJSONObject("main")
                        val tempKelvin = main.getDouble("temp")
                        val tempCelsius = tempKelvin - 273.15
                        val temp = String.format("%.2f", tempCelsius)
                        val dateText = getDateText(day.getString("dt"))

                        forecastList.add(ForecastAdapter.ForecastItem(dateText, "$temp °C"))
                    }

                    adapter.setForecastItems(forecastList)
                    locationTextView.text = "Location: ${locationAutoCompleteTextView.text}"
                }
            } catch (e: Exception) {
                e.printStackTrace() // Log the exception
            }
        }


        private fun getDateText(timestamp: String): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = Date(timestamp.toLong() * 1000)
            return sdf.format(date)
        }
    }
}
