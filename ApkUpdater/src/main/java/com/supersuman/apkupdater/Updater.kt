package com.supersuman.apkupdater

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import khttp.get
import kotlin.concurrent.thread


class ApkUpdater(private val activity: Activity, url: String) {
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