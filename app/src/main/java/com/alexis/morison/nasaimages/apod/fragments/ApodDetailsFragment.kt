package com.alexis.morison.nasaimages.apod.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.models.APOD
import com.alexis.morison.nasaimages.services.UtilService
import com.squareup.picasso.Picasso

private const val itemParamExtra = "itemAPOD"

class ApodDetailsFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var copyright: TextView
    private lateinit var date: TextView
    private lateinit var explanation: TextView
    private lateinit var btnWallpaper: Button
    private lateinit var btnWallpaperDownload: Button

    private var hdUrl = ""

    private var itemData: APOD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            itemData = it.getSerializable(itemParamExtra) as APOD
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_apod_details, container, false)

        setViews(v)

        setData()

        setListeners()

        return v
    }

    private fun setViews(v: View) {

        imageView = v.findViewById(R.id.apod_image_view)
        title = v.findViewById(R.id.apod_title)
        copyright = v.findViewById(R.id.apod_copyright)
        date = v.findViewById(R.id.apod_date)
        explanation = v.findViewById(R.id.apod_explanation)
        btnWallpaper = v.findViewById(R.id.btn_set_wallpaper)
        btnWallpaperDownload = v.findViewById(R.id.btn_download_wallpaper)
    }

    private fun setData() {

        title.text = itemData!!.title
        copyright.text = itemData!!.copyright
        date.text = itemData!!.date
        explanation.text = itemData!!.explanation
        hdUrl = itemData!!.hdurl

        Picasso.get()
                .load(itemData!!.url)
                .error(R.drawable.library)
                .into(imageView)
    }

    private fun setListeners() {

        val utilService = UtilService(context, activity as Activity)

        btnWallpaper.setOnClickListener {

            utilService.askPermissions()

            Toast.makeText(context, "Setting wallpaper", Toast.LENGTH_SHORT).show()

            utilService.downloadImage(hdUrl, true)
        }

        btnWallpaperDownload.setOnClickListener {

            utilService.askPermissions()

            Toast.makeText(context, "Downloading image", Toast.LENGTH_SHORT).show()

            utilService.downloadImage(hdUrl, false)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: APOD) =

                ApodDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(itemParamExtra, param1)
                    }
                }
    }
}