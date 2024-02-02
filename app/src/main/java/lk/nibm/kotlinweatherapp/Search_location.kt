package lk.nibm.kotlinweatherapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL

class SearchLocation : AppCompatActivity() {

    private val API_KEY = "b71ecedd9c8b464a07bfbb6576a34a12"
    private var enteredCity = "Colombo"

    private lateinit var loaderProgressBar: ProgressBar
    private lateinit var mainContainerLayout: RelativeLayout
    private lateinit var errorTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)

        loaderProgressBar = findViewById(R.id.loader)
        mainContainerLayout = findViewById(R.id.mainContainer)
        errorTextView = findViewById(R.id.errorText)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    enteredCity = query
                    WeatherTask().execute()
                    searchView.clearFocus()
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text change if needed
                return false
            }
        })

        WeatherTask().execute()
    }

    inner class WeatherTask : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            loaderProgressBar.visibility = View.VISIBLE
            mainContainerLayout.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            try {
                val apiUrl =
                    "https://api.openweathermap.org/data/2.5/weather?q=$enteredCity&units=metric&appid=$API_KEY"
                return URL(apiUrl).readText(Charsets.UTF_8)
            } catch (e: Exception) {
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                if (result != null && result.isNotEmpty()) {
                    val jsonObj = JSONObject(result)
                    val main = jsonObj.getJSONObject("main")
                    val temperature = main.getString("temp")
                    findViewById<TextView>(R.id.address).text = enteredCity
                    findViewById<TextView>(R.id.temp).text = "$temperature°C"
                    loaderProgressBar.visibility = View.GONE
                    mainContainerLayout.visibility = View.VISIBLE
                } else {
                    showError()
                }
            } catch (e: Exception) {
                showError()
            }
        }

        private fun showError() {
            loaderProgressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
        }
    }
}
