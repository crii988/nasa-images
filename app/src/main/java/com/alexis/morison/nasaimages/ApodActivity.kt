package com.alexis.morison.nasaimages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.adapters.ApodItemsAdapter
import com.alexis.morison.nasaimages.adapters.MainItemsAdapter
import com.alexis.morison.nasaimages.models.APOD
import com.alexis.morison.nasaimages.models.MainItem
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_apod.*
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApodActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var requestQueue: RequestQueue? = null

    private val apiKey = "XdRrmURyk5bW91jnAyoHbaAngJrF8vKIiQiZI6AV"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod)

        requestQueue = Volley.newRequestQueue(applicationContext)

        toolbar = toolbar_apod_id

        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        setViews()

        //setRecyclerView()

        getLastApod()
    }

    private fun setViews() {

        recyclerView = apod_items_recycler
    }

    private fun setRecyclerView(listItems: List<APOD>) {

        //val listItems = getLastApod()

        viewManager = LinearLayoutManager(this)
        viewAdapter = ApodItemsAdapter(listItems)

        recyclerView.apply {

            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun getLastApod() {

        val apodList = mutableListOf<APOD>()

        val current = LocalDateTime.now()

        val last = current.minusDays(10)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val endDate = current.format(formatter)
        val startDate = last.format(formatter)

        val url = "https://api.nasa.gov/planetary/apod?start_date=$startDate&end_date=$endDate&api_key=$apiKey"

        val json = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->

                for (i in 0 until response.length()) {

                    val oneApod: JSONObject = response.getJSONObject(i)

                    if (oneApod.getString("media_type").toString() == "image") {

                        var copy = ""
                        var hdUrl = ""

                        try {
                            copy = oneApod.getString("copyright")
                            hdUrl = oneApod.getString("hdurl")
                        }
                        catch (ex: Exception) {
                            Log.d("asdasd", ex.message.toString())
                        }

                        val apodObject = APOD(
                            copy,
                            oneApod.getString("date"),
                            oneApod.getString("explanation"),
                            hdUrl,
                            oneApod.getString("title"),
                            oneApod.getString("url"),
                        )

                        apodList.add(apodObject)
                    }
                }

                apodList.reverse()

                setRecyclerView(apodList)
            },
            { _ ->

            }
        )

        requestQueue?.add(json)


    }
}