package com.alexis.morison.nasaimages.library.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.NumberPicker
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.adapters.ApodItemsAdapter
import com.alexis.morison.nasaimages.apod.models.APOD
import com.alexis.morison.nasaimages.library.adapters.LibraryItemsAdapter
import com.alexis.morison.nasaimages.library.models.Library
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter

private const val searchExtra = "search"
private const val startExtra = "start"
private const val endExtra = "end"

class LibraryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var progressBar: ProgressBar
    private lateinit var btnNextPage: MaterialButton
    private lateinit var btnPrevPage: MaterialButton

    private var requestQueue: RequestQueue? = null

    private var libraryList = mutableListOf<Library>()

    private var query = ""
    private var startDate = ""
    private var endDate = ""

    private var page = 1
    private var maxPage = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            query = it.getString(searchExtra)!!
            startDate = it.getString(startExtra)!!
            endDate = it.getString(endExtra)!!
        }

        requestQueue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_library, container, false)

        setViews(v)

        setListeners()

        if (libraryList.size > 0) {

            setRecyclerView()

            btnNextPage.visibility = View.VISIBLE
            btnPrevPage.visibility = View.VISIBLE
        }
        else search()

        return v
    }

    private fun setViews(v: View) {

        progressBar = v.findViewById(R.id.library_main_progress)
        btnNextPage = v.findViewById(R.id.btn_next_page)
        btnPrevPage = v.findViewById(R.id.btn_prev_page)
        recyclerView = v.findViewById(R.id.library_items_recycler)
    }

    private fun setListeners() {

        btnPrevPage.setOnClickListener {

            if (page > 1) {

                page -= 1
                search()
            }
        }

        btnNextPage.setOnClickListener {

            if (page < maxPage) {

                page += 1
                search()
            }

            if (libraryList.size == 0) {

                maxPage = page
            }
        }
    }

    private fun setRecyclerView() {

        viewManager = LinearLayoutManager(context)
        viewAdapter = LibraryItemsAdapter(libraryList)

        progressBar.visibility = View.GONE

        recyclerView.apply {

            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun search() {

        libraryList.clear()

        btnNextPage.visibility = View.GONE
        btnPrevPage.visibility = View.GONE

        setRecyclerView()

        val url = "https://images-api.nasa.gov/search?q=$query&page=$page&year_start=$startDate&year_end=$endDate&media_type=image"

        progressBar.visibility = View.VISIBLE

        val json = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                val items = response.getJSONObject("collection").getJSONArray("items")

                if (items.length() > 0) {

                    for (i in 0 until items.length()) {

                        val oneLibrary = items.getJSONObject(i)

                        val title = oneLibrary
                                .getJSONArray("data")
                                .getJSONObject(0)
                                .getString("title").toString()

                        val nasaID = oneLibrary
                                .getJSONArray("data")
                                .getJSONObject(0)
                                .getString("nasa_id").toString()

                        val description = oneLibrary
                            .getJSONArray("data")
                            .getJSONObject(0)
                            .getString("description").toString()

                        val date = oneLibrary
                                .getJSONArray("data")
                                .getJSONObject(0)
                                .getString("date_created").toString()

                        val dateFormatted = date.substring(0, 10)

                        val href = oneLibrary
                                .getJSONArray("links")
                                .getJSONObject(0)
                                .getString("href").toString()

                        val libraryObj = Library(title, href, nasaID, description, dateFormatted)

                        libraryList.add(libraryObj)
                    }

                    setRecyclerView()

                    btnNextPage.visibility = View.VISIBLE
                    btnPrevPage.visibility = View.VISIBLE
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
        fun newInstance(param1: String, param2: String, param3: String) =

            LibraryFragment().apply {
                arguments = Bundle().apply {
                    putString(searchExtra, param1)
                    putString(startExtra, param2)
                    putString(endExtra, param3)
                }
            }
    }
}