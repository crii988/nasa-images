package com.alexis.morison.nasaimages.rovers.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.library.adapters.LibraryItemsAdapter
import com.alexis.morison.nasaimages.library.models.Library
import com.alexis.morison.nasaimages.rovers.adapters.RoverItemsAdapter
import com.alexis.morison.nasaimages.rovers.models.Rover
import com.alexis.morison.nasaimages.rovers.models.RoverQuery
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import org.json.JSONArray
import java.util.*

private const val searchExtra = "search"

class RoverFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var progressBar: ProgressBar
    private lateinit var btnNextPage: MaterialButton
    private lateinit var btnPrevPage: MaterialButton

    private lateinit var toolbar: MaterialToolbar

    private var query: RoverQuery? = null

    private var requestQueue: RequestQueue? = null

    private var roverList = mutableListOf<Rover>()

    private var page = 1
    private var maxPage = 100

    private val apiKey = "XdRrmURyk5bW91jnAyoHbaAngJrF8vKIiQiZI6AV"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            query = it.getSerializable(searchExtra) as RoverQuery
        }

        requestQueue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_rover, container, false)

        setViews(v)

        if (roverList.size > 0) {

            setRecyclerView()

            btnNextPage.visibility = View.VISIBLE
            btnPrevPage.visibility = View.VISIBLE
        }
        else search()

        return v
    }

    private fun setViews(v: View) {

        progressBar = v.findViewById(R.id.rover_main_progress)
        btnNextPage = v.findViewById(R.id.btn_next_page_rover)
        btnPrevPage = v.findViewById(R.id.btn_prev_page_rover)
        recyclerView = v.findViewById(R.id.rover_items_recycler)

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = query!!.rover
    }

    private fun setRecyclerView() {

        viewManager = LinearLayoutManager(context)
        viewAdapter = RoverItemsAdapter(roverList)

        progressBar.visibility = View.GONE

        recyclerView.apply {

            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun makeSearchQuery(): String {

        var url = "https://api.nasa.gov/mars-photos/api/v1/rovers/${query!!.rover}/"

        if (query!!.isLatest) url += "latest_photos?api_key=$apiKey"
        else {

            url += "photos?api_key=$apiKey"

            if (query!!.isFilterCamera) {

                url += "&camera=${query!!.camera}"
            }

            url += if (query!!.isSol) "&sol=${query!!.sol}"
            else "&earth_date==${query!!.date}"
        }

        return url
    }

    private fun search() {

        roverList.clear()

        btnNextPage.visibility = View.GONE
        btnPrevPage.visibility = View.GONE

        setRecyclerView()

        val queryUrl = makeSearchQuery()

        progressBar.visibility = View.VISIBLE

        val json = JsonObjectRequest(
            Request.Method.GET, queryUrl, null,
            { response ->

                var items: JSONArray? = null

                items = if (query!!.isLatest) response.getJSONArray("latest_photos")
                else response.getJSONArray("photos")

                if (items.length() > 0) {

                    for (i in 0 until items.length()) {

                        val onePhoto = items.getJSONObject(i)

                        val camera = onePhoto.getJSONObject("camera").getString("full_name").toString()

                        val earthDate = onePhoto.getString("earth_date").toString()
                        val solDate = onePhoto.getInt("sol").toString()

                        val roverName = onePhoto.getJSONObject("rover").getString("name").toString()
                        val status = onePhoto.getJSONObject("rover").getString("status").toString()

                        val imgSrc = onePhoto.getString("img_src").toString()

                        val rover = Rover(
                            camera,
                            earthDate,
                            solDate,
                            roverName,
                            status,
                            imgSrc
                        )

                        roverList.add(rover)
                    }

                    setRecyclerView()

                    if (!query!!.isLatest) {

                        btnNextPage.visibility = View.VISIBLE
                        btnPrevPage.visibility = View.VISIBLE
                    }
                }
                else {

                    progressBar.visibility = View.GONE
                }
            },
            { _ ->
                progressBar.visibility = View.GONE
            }
        )

        requestQueue?.add(json)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: RoverQuery) =
                RoverFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(searchExtra, param1)
                    }
                }
    }
}