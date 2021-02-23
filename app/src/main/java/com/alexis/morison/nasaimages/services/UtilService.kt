package com.alexis.morison.nasaimages.services

import android.Manifest
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.alexis.morison.nasaimages.R
import java.io.File


class UtilService(private val context: Context?, private val activity: Activity) {

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(context)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                                context,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        //finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private var status: Int? = null
    private var lastMsg = ""

    fun downloadImage(url: String, setWallpaper: Boolean) {

        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = context?.let {
            ContextCompat.getSystemService(it, DownloadManager::class.java)
        } as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                        directory.toString(),
                        url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)

        Thread(Runnable {

            var downloading = true

            while (downloading) {

                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                var msg = ""
                msg = when (status) {
                    DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
                    DownloadManager.STATUS_PAUSED -> "Paused"
                    DownloadManager.STATUS_PENDING -> "Pending"
                    DownloadManager.STATUS_RUNNING -> "Downloading"
                    DownloadManager.STATUS_SUCCESSFUL -> "Download success"
                    else -> "There's nothing to download"
                }

                if (msg != lastMsg) {

                    activity.runOnUiThread {

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        lastMsg = msg

                        if (msg == "Download success" && setWallpaper) {

                            val wallManager = WallpaperManager.getInstance(context)

                            try {
                                val sdcardPath = Environment.getExternalStorageDirectory().toString()

                                val filePath = sdcardPath +
                                        File.separator +
                                        "$directory" +
                                        File.separator +
                                        url.substring(url.lastIndexOf("/") + 1)

                                val bitmap = BitmapFactory.decodeFile(filePath)

                                wallManager.setBitmap(bitmap)

                                Toast.makeText(context, "Set Wallpapers successfully", Toast.LENGTH_SHORT).show()

                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                cursor.close()
            }
        }).start()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }
}