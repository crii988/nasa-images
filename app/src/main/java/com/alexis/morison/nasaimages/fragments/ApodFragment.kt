package com.alexis.morison.nasaimages.fragments

import android.app.Activity
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.services.UtilService
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.IOException

private const val dateParamExtra = ""

class ApodFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var title: TextView
    private lateinit var copyright: TextView
    private lateinit var date: TextView
    private lateinit var explanation: TextView
    private lateinit var btnWallpaper: Button
    private lateinit var btnWallpaperDownload: Button

    private var requestQueue: RequestQueue? = null

    private val apiKey = "XdRrmURyk5bW91jnAyoHbaAngJrF8vKIiQiZI6AV"

    private var hdUrl = ""

    private var dateParam: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            dateParam = it.getString(dateParamExtra)
        }

        requestQueue = Volley.newRequestQueue(context)
        getApod()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_apod, container, false)

        setViews(v)

        setListeners(v)

        return v
    }

    private fun setViews(v: View) {

        imageView = v.findViewById(R.id.apod_image_view)
        videoView = v.findViewById(R.id.apod_video_view)
        title = v.findViewById(R.id.apod_title)
        copyright = v.findViewById(R.id.apod_copyright)
        date = v.findViewById(R.id.apod_date)
        explanation = v.findViewById(R.id.apod_explanation)
        btnWallpaper = v.findViewById(R.id.btn_set_wallpaper)
        btnWallpaperDownload = v.findViewById(R.id.btn_download_wallpaper)
    }

    private fun setListeners(v: View) {

        btnWallpaper.setOnClickListener {

            Picasso.get().load(hdUrl).into(object : Target {

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                    val wallManager = WallpaperManager.getInstance(context)

                    try {
                        wallManager.setBitmap(bitmap)

                        Toast.makeText(context, "Download success", Toast.LENGTH_SHORT).show()

                    } catch (e: IOException) {
                        e.printStackTrace()

                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()

                    Toast.makeText(context, "If the wallpaper is not set, download image please", Toast.LENGTH_SHORT).show()
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                    if (e != null) {
                        e.message?.let { it1 -> Log.d("ASDASD", it1) }
                    }

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnWallpaperDownload.setOnClickListener {

            val utilService = UtilService(context, activity as Activity)

            utilService.askPermissions()

            utilService.downloadImage(hdUrl)
        }
    }

    private fun getApod() {

        val url = "https://api.nasa.gov/planetary/apod?date=$dateParam&api_key=$apiKey"

        val json = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                title.text = response.getString("title")
                date.text = response.getString("date")
                explanation.text = response.getString("explanation")

                copyright.text = ""

                try {
                    copyright.text = response.getString("copyright")
                }
                catch (e: Exception) {
                    Log.d("asdasd", e.message.toString())
                }

                hdUrl = response.getString("hdurl")

                Picasso.get()
                    .load(response.getString("url"))
                    .error(R.drawable.library)
                    .into(imageView)

                btnWallpaper.visibility = View.VISIBLE
                btnWallpaperDownload.visibility = View.VISIBLE
            },
            { _ ->
                Picasso.get()
                    .load(R.drawable.apod)
                    .into(imageView)

                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue?.add(json)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            ApodFragment().apply {
                arguments = Bundle().apply {
                    putString(dateParamExtra, param1)
                }
            }
    }
}