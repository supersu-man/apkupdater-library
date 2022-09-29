package com.supersuman.githubapkupdater

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import khttp.get
import org.jsoup.Jsoup
import kotlin.concurrent.thread


class Updater(private val activity: Activity, url: String) {
    private var response: String = get(url).text
    var threeNumbers: Boolean = false

    fun isNewUpdateAvailable(): Boolean? {
        val latestVersion = if (threeNumbers) {
            Regex("[0-9]+\\.[0-9]+\\.[0-9]+").find(response)?.value
        } else {
            Regex("[0-9]+\\.[0-9]+").find(response)?.value
        }
        val currentVersionName = activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
        if (latestVersion != null) return latestVersion != currentVersionName
        return null
    }


    fun requestDownload() {
        val src = Regex("\"http.+?expanded.+?\"").find(response)?.value?.removeSurrounding("\"") ?: return
        val res = get(src).text
        val apkLink = Regex("\".+?\\.apk\"").find(res)?.value?.removeSurrounding("\"") ?: return
        val title = Regex(">.+?\\.apk<").find(res)?.value?.removeSurrounding(">", "<") ?: return
        val request = DownloadManager.Request(Uri.parse("https://github.com/$apkLink"))
        val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        request.setTitle(title)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
        downloadManager.enqueue(request)
    }

    fun requestMyPermissions(function: () -> Unit) {
        Dexter.withContext(activity).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    function()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    println(response)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?, token: PermissionToken?
                ) {
                    println(permission)
                    println(token)
                }
            }).check()
    }

    fun hasPermissionsGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    fun isInternetConnection(): Boolean {
        return try {
            thread {
                get("https://www.google.com/")
            }.join()
            true
        } catch (e: Exception) {
            false
        }
    }

}