package com.alexis.morison.nasaimages.apod.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.adapters.ApodItemsAdapter
import com.alexis.morison.nasaimages.apod.models.APOD
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import kotlin.Exception


class ApodFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var progressBar: ProgressBar

    private var requestQueue: RequestQueue? = null

    private val apiKey = "XdRrmURyk5bW91jnAyoHbaAngJrF8vKIiQiZI6AV"

    private var apodList = mutableListOf<APOD>()

    private var savedState: Bundle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestQueue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_apod, container, false)

        setViews(v)

        if (savedState != null) {

            apodList = savedState!!.getParcelableArrayList("data")!!

            setRecyclerView()
        }
        else {
            getLastApod()
        }

        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()

        savedState = Bundle()

        savedState!!.putParcelableArrayList("data", ArrayList(apodList))
    }

    private fun setViews(v: View) {

        progressBar = v.findViewById(R.id.apod_main_progress)
        recyclerView = v.findViewById(R.id.apod_items_recycler)
    }

    private fun setRecyclerView() {

        viewManager = LinearLayoutManager(context)
        viewAdapter = ApodItemsAdapter(apodList)

        progressBar.visibility = View.GONE

        recyclerView.apply {

            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun getLastApod() {

        val current = LocalDateTime.now()
        val last = current.minusDays(15)

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

                        try {
                            copy = oneApod.getString("copyright")
                        }
                        catch (ex: Exception) {
                            Log.d("asdasd", ex.message.toString())
                        }

                        val apodObject = APOD(
                            copy,
                            oneApod.getString("date"),
                            oneApod.getString("explanation"),
                            oneApod.getString("hdurl"),
                            oneApod.getString("title"),
                            oneApod.getString("url"),
                        )

                        apodList.add(apodObject)
                    }
                }

                apodList.reverse()

                setRecyclerView()
            },
            { _ ->
                // Por error no hago nada..
            }
        )

        requestQueue?.add(json)
    }
}