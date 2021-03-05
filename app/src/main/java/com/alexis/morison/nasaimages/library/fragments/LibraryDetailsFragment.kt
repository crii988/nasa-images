package com.alexis.morison.nasaimages.library.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.library.models.Library
import com.alexis.morison.nasaimages.services.UtilService
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso
import java.util.*

private const val itemParamExtra = "itemID"

class LibraryDetailsFragment : Fragment() {

    private lateinit var imageView: ZoomageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var date: TextView
    private lateinit var center: TextView
    private lateinit var btnWallpaper: Button
    private lateinit var btnWallpaperDownload: Button
    private lateinit var chipGroup: ChipGroup

    private lateinit var toolbar: MaterialToolbar

    private var itemData: Library? = null
    private var requestQueue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            itemData = it.getSerializable(itemParamExtra) as Library
        }

        requestQueue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_library_details, container, false)

        setViews(v)

        setData()

        setListeners()

        return v
    }

    private fun setViews(v: View) {

        imageView = v.findViewById(R.id.library_image_view)
        title = v.findViewById(R.id.library_title)
        description = v.findViewById(R.id.library_description)
        date = v.findViewById(R.id.library_date)
        center = v.findViewById(R.id.library_center)
        btnWallpaper = v.findViewById(R.id.btn_set_wallpaper_library)
        btnWallpaperDownload = v.findViewById(R.id.btn_download_wallpaper_library)
        chipGroup = v.findViewById(R.id.chipGroup)

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = itemData!!.query.capitalize(Locale.getDefault())
    }

    private fun setData() {

        title.text = itemData!!.title
        description.text = itemData!!.description
        date.text = itemData!!.date_created
        center.text = " " + itemData!!.center

        Picasso.get()
                .load(itemData!!.href)
                .error(R.drawable.library)
                .into(imageView)

        itemData!!.keywords.forEach {

            val chip = Chip(context)
            chip.text = it

            chip.setOnClickListener {

                val fm = context as FragmentActivity

                val fragmentTransaction = fm.supportFragmentManager.beginTransaction()

                fragmentTransaction.replace(
                        R.id.fragmentContainer,
                        LibraryFormFragment.newInstance(chip.text.toString().toLowerCase(Locale.getDefault())))

                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            chipGroup.addView(chip)
        }
    }

    private fun setListeners() {

        val utilService = UtilService(context)

        btnWallpaper.setOnClickListener {

            showDialog(utilService)
        }

        btnWallpaperDownload.setOnClickListener {

            Toast.makeText(context, "Downloading image", Toast.LENGTH_SHORT).show()

            getImageById(utilService, UtilService.FLAG_NOT_SET_WALLPAPER)
        }
    }

    private fun showDialog(utilService: UtilService) {

        val options = arrayOf("Main screen", "Lock screen", "Main and lock screen")

        val builder = context?.let { MaterialAlertDialogBuilder(it) }

        with(builder) {

            this!!.setTitle("Set wallpaper as")
            setItems(options) { _, which ->

                getImageById(utilService, which)
            }
            show()
        }
    }

    private fun getImageById(utilService: UtilService, tag: Int) {

        val url = "https://images-api.nasa.gov/asset/${itemData!!.nasa_id}"

        val json = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->

                    val items = response.getJSONObject("collection").getJSONArray("items")

                    val origImage = items.getJSONObject(0).getString("href").toString()

                    val hdUrl = origImage.replace("http", "https")

                    if (tag != UtilService.FLAG_NOT_SET_WALLPAPER) {

                        Toast.makeText(context, "Setting wallpaper", Toast.LENGTH_SHORT).show()
                    }

                    utilService.downloadImage(hdUrl, tag)
                },
                { _ ->
                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                }
        )

        requestQueue?.add(json)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Library) =

                LibraryDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(itemParamExtra, param1)
                    }
                }
    }
}