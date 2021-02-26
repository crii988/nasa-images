package com.alexis.morison.nasaimages.services

import android.Manifest
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
                    .setPositiveButton("Allow") { _, _ ->
                        ActivityCompat.requestPermissions(
                                context,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        //finish()
                    }
                    .setNegativeButton("Deny") { dialog, _ -> dialog.cancel() }
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

    fun downloadImage(url: String, flag: Int) {

        askPermissions()

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

                val cursorStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                if (cursorStatus == DownloadManager.STATUS_SUCCESSFUL) {

                    downloading = false
                }
                else if (cursorStatus == DownloadManager.STATUS_FAILED) {

                    downloading = false
                }
                status = cursorStatus

                cursor.close()
            }

            when (status) {

                DownloadManager.STATUS_FAILED -> {

                    val title = if (flag != -1)
                        "Setting wallpaper failed"
                    else
                        "Download image failed"

                    sendNotification(title, "Please try again", R.drawable.ic_baseline_error_outline_24)
                }

                DownloadManager.STATUS_SUCCESSFUL -> {

                    if (flag != -1) {

                        setWallpaperManager(directory, url, flag)
                    }
                    else {
                        sendNotification("Download image successfully",
                                "The image is in the folder $directory",
                                R.drawable.ic_baseline_check_circle_outline_24)
                    }
                }
            }
        }).start()
    }

    private fun setWallpaperManager(directory: File, url: String, flag: Int) {

        val wallManager = WallpaperManager.getInstance(context)

        try {
            val sdcardPath = Environment.getExternalStorageDirectory().toString()

            val filePath = sdcardPath +
                    File.separator +
                    "$directory" +
                    File.separator +
                    url.substring(url.lastIndexOf("/") + 1)

            val bitmap = BitmapFactory.decodeFile(filePath)

            when (flag) {

                FLAG_MAIN_SCREEN -> wallManager.setBitmap(bitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_SYSTEM)

                FLAG_LOCK_SCREEN -> wallManager.setBitmap(bitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_LOCK)

                FLAG_BOTH_SCREEN -> wallManager.setBitmap(bitmap)
            }

            sendNotification(
                    "Wallpaper set successfully",
                    "The wallpaper is in the folder $directory",
                    R.drawable.ic_baseline_check_circle_outline_24)
        }
        catch (e: Exception) {

            sendNotification(
                    "Setting wallpaper failed",
                    "Please try again",
                    R.drawable.ic_baseline_error_outline_24)
        }
    }

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Download notification"

    private fun sendNotification(title: String, content: String, icon: Int) {

        notificationManager = context?.let {
            ContextCompat.getSystemService(it, NotificationManager::class.java)
        } as NotificationManager

        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)

        notificationManager.notify(1234, builder.build())
    }

    companion object {

        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

        const val FLAG_NOT_SET_WALLPAPER = -1
        const val FLAG_MAIN_SCREEN = 0
        const val FLAG_LOCK_SCREEN = 1
        const val FLAG_BOTH_SCREEN = 2
    }
}