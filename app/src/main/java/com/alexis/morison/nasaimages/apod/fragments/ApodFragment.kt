package com.alexis.morison.nasaimages.apod.fragments

import android.app.Activity
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.models.APOD
import com.alexis.morison.nasaimages.services.UtilService
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.IOException


private const val itemParamExtra = "itemAPOD"

class ApodFragment : Fragment() {

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

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_apod, container, false)

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

            utilService.downloadImage(hdUrl, true)
        }

        btnWallpaperDownload.setOnClickListener {

            utilService.askPermissions()

            utilService.downloadImage(hdUrl, false)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: APOD) =

                ApodFragment().apply {
                arguments = Bundle().apply {

                    putSerializable(itemParamExtra, param1)
                }
            }
    }
}