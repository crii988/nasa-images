package com.alexis.morison.nasaimages.rovers.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.rovers.models.Rover
import com.alexis.morison.nasaimages.services.UtilService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jsibbold.zoomage.ZoomageView
import com.squareup.picasso.Picasso

private const val roverParam = "rover"

class RoverDetailsFragment : Fragment() {

    private lateinit var imageView: ZoomageView
    private lateinit var date: TextView
    private lateinit var sol: TextView
    private lateinit var camera: TextView
    private lateinit var status: TextView
    private lateinit var btnWallpaper: Button
    private lateinit var btnWallpaperDownload: Button
    private lateinit var toolbar: MaterialToolbar
    private lateinit var btnShare: ImageButton

    private var imgUrl = ""

    private var rover: Rover? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            rover = it.getSerializable(roverParam) as Rover
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_rover_details, container, false)

        setViews(v)

        setListeners()

        setData()

        return v
    }

    private fun setViews(v: View) {

        imageView = v.findViewById(R.id.rover_image_view)
        date = v.findViewById(R.id.rover_date)
        sol = v.findViewById(R.id.rover_sol)
        camera = v.findViewById(R.id.rover_camera)
        status = v.findViewById(R.id.rover_status)
        btnWallpaper = v.findViewById(R.id.btn_set_wallpaper_rover)
        btnWallpaperDownload = v.findViewById(R.id.btn_download_wallpaper_rover)
        btnShare = v.findViewById(R.id.rover_share)

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = rover!!.rover_name
    }

    private fun setData() {

        date.text = rover!!.earth_date
        sol.text = rover!!.sol_date
        camera.text = rover!!.camera_full_name
        status.text = rover!!.status

        imgUrl = rover!!.img_src

        Picasso.get()
                .load(rover!!.img_src)
                .error(R.drawable.rover)
                .into(imageView)
    }

    private fun setListeners() {

        val utilService = UtilService(context)

        btnWallpaper.setOnClickListener {

            showDialog(utilService)
        }

        btnWallpaperDownload.setOnClickListener {

            Toast.makeText(context, "Downloading image", Toast.LENGTH_SHORT).show()

            utilService.downloadImage(imgUrl, UtilService.FLAG_NOT_SET_WALLPAPER)
        }

        btnShare.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, rover!!.img_src)
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

                utilService.downloadImage(imgUrl, which)
            }
            show()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Rover) =
            RoverDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(roverParam, param1)
                }
            }
    }
}