package com.alexis.morison.nasaimages.main

import android.app.slice.Slice
import android.app.slice.SliceItem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.main.adapters.MainItemsAdapter
import com.alexis.morison.nasaimages.main.models.MainItem
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var toolbar: MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()

        setListeners()

        setRecyclerView()
    }

    private fun setViews() {

        recyclerView = main_items_recycler

        toolbar = toolbar_id_main
        toolbar.inflateMenu(R.menu.main_menu)
    }

    private fun setListeners() {

        toolbar.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.menu_favs -> {

                    Toast.makeText(this, "Favs", Toast.LENGTH_SHORT).show()

                    true
                }
                R.id.menu_settings -> {

                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()

                    true
                }
                else -> {

                    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()

                    true
                }
            }
        }
    }

    private fun setRecyclerView() {

        val listItems = listOf(
            MainItem(
                1,
                resources.getString(R.string.apod_title),
                resources.getString(R.string.apod_description),
                "Astronomy Picture of the Day (APOD) is a website provided by NASA and Michigan Technological University (MTU). According to the website, \"Each day a different image or photograph of our universe is featured, along with a brief explanation written by a professional astronomer.\" \n" +
                        "The photograph does not necessarily correspond to a celestial event on the exact day that it is displayed, and images are sometimes repeated. However, the pictures and descriptions often relate to current events in astronomy and space exploration."),

            //MainItem(2, "Earth", "Unlock the significant public investment in earth observation data"),
            //MainItem(3, "EPIC", "Earth Polychromatic Imaging Camera"),
            MainItem(
                4,
                resources.getString(R.string.rovers_title),
                resources.getString(R.string.rovers_description),
                "This API is designed to collect image data gathered by NASA's Curiosity, Opportunity, and Spirit rovers on Mars and make it more easily available to other developers, educators, and citizen scientists. This API is maintained by Chris Cerami.\n" +
                        "Each rover has its own set of photos stored in the database, which can be queried separately. There are several possible queries that can be made against the API. Photos are organized by the sol (Martian rotation or day) on which they were taken, counting up from the rover's landing date. A photo taken on Curiosity's 1000th Martian sol exploring Mars, for example, will have a sol attribute of 1000. If instead you prefer to search by the Earth date on which a photo was taken, you can do that too."),

            MainItem(
                5,
                resources.getString(R.string.library_title),
                resources.getString(R.string.library_description),
                "NASA's image library, images.nasa.gov, consolidates imagery and videos in one searchable locations. Users can download content in multiple sizes and resolutions and see the metadata associated with images, including EXIF/camera data on many images."),
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