package com.alexis.morison.nasaimages.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.main.adapters.MainItemsAdapter
import com.alexis.morison.nasaimages.main.models.MainItem
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()

        setRecyclerView()
    }

    private fun setViews() {

        recyclerView = main_items_recycler
    }

    private fun setRecyclerView() {

        val listItems = listOf(
            MainItem(1, "APOD", "Astronomy Picture of the Day",),
            //MainItem(2, "Earth", "Unlock the significant public investment in earth observation data"),
            //MainItem(3, "EPIC", "Earth Polychromatic Imaging Camera"),
            //MainItem(4, "Mars Rover Photos", "Image data gathered by NASA's Curiosity, Opportunity, and Spirit rovers on Mars"),
            MainItem(5, "NASA Image Library", "Access to the NASA Image Library site at images.nasa.gov"),
        )

        viewManager = LinearLayoutManager(this)
        viewAdapter = MainItemsAdapter(listItems)

        recyclerView.apply {

            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}