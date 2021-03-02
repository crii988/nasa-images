package com.alexis.morison.nasaimages.apod.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.transition.TransitionInflater
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.models.APOD
import com.alexis.morison.nasaimages.services.UtilService
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.fragment_apod_details.*

private const val itemParamExtra = "itemAPOD"

class ApodDetailsFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var copyright: TextView
    private lateinit var date: TextView
    private lateinit var explanation: TextView
    private lateinit var btnWallpaper: Button
    private lateinit var btnWallpaperDownload: Button
    private lateinit var card: MaterialCardView
    private lateinit var check: CheckBox

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
        card = v.findViewById(R.id.apod_card_detail)
        check = v.findViewById(R.id.apod_check)
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

        val utilService = UtilService(context)

        btnWallpaper.setOnClickListener {

            showDialog(utilService)
        }

        btnWallpaperDownload.setOnClickListener {

            Toast.makeText(context, "Downloading image", Toast.LENGTH_SHORT).show()

            utilService.downloadImage(hdUrl, UtilService.FLAG_NOT_SET_WALLPAPER)
        }

        check.setOnClickListener {

            if (check.isChecked) {

            }
        }
    }

    private fun showDialog(utilService: UtilService) {

        val options = arrayOf("Main screen", "Lock screen", "Main and lock screen")

        val builder = context?.let { MaterialAlertDialogBuilder(it, R.style.ThemeOverlay_MaterialComponents_Dark) }

        with(builder) {

            this!!.setTitle("Set wallpaper as")
            setItems(options) { _, which ->

                Toast.makeText(context, "Setting wallpaper", Toast.LENGTH_SHORT).show()

                utilService.downloadImage(hdUrl, which)
            }
            show()
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