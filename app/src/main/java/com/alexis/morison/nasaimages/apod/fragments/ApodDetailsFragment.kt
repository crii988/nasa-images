package com.alexis.morison.nasaimages.apod.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.models.APOD
import com.alexis.morison.nasaimages.services.UtilService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.apod_menu_card.view.*

private const val itemParamExtra = "itemAPOD"

class ApodDetailsFragment : Fragment() {

    private lateinit var imageView: ZoomageView
    private lateinit var title: TextView
    private lateinit var copyright: TextView
    private lateinit var date: TextView
    private lateinit var explanation: TextView
    private lateinit var btnWallpaper: Button
    private lateinit var btnWallpaperDownload: Button
    private lateinit var btnShare: ImageButton
    private lateinit var toolbar: MaterialToolbar

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
        btnShare = v.findViewById(R.id.apod_share)
        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = resources.getString(R.string.apod_title)
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

        btnShare.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, hdUrl)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share URL")
            startActivity(shareIntent)
        }
    }

    private fun showDialog(utilService: UtilService) {

        val options = arrayOf("Main screen", "Lock screen", "Main and lock screen")

        val builder = context?.let { MaterialAlertDialogBuilder(it) }

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