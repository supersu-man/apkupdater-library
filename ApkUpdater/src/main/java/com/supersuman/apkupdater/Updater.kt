package com.supersuman.apkupdater

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.github.kittinunf.fuel.Fuel


class ApkUpdater(private val activity: Activity, url: String) {
    private var response: String = getResponse(url)
    var threeNumbers: Boolean = false

    fun isNewUpdateAvailable(): Boolean? {
        val latestVersion = getLatestVersion(threeNumbers, response)
        if (latestVersion=="") return null
        val currentVersionName = activity.packageManager.getPackageInfo(activity.packageName, 0).versionName
        return latestVersion != currentVersionName
    }

    fun requestDownload() {
        val res = getReleaseResponse(response)
        val apkLink = getApkLink(res)
        val title = getApkTitle(res)

        val request = DownloadManager.Request(Uri.parse("https://github.com/$apkLink"))
        val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        request.setTitle(title)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
        downloadManager.enqueue(request)
    }

    companion object {
        internal fun getResponse(url: String): String {
            return Fuel.get(url).response().second.toString()
        }

        internal fun getLatestVersion(threeNumbers: Boolean, response: String): String {
            val latestVersion = if (threeNumbers) {
                Regex("[0-9]+\\.[0-9]+\\.[0-9]+").find(response)?.value
            } else {
                Regex("[0-9]+\\.[0-9]+").find(response)?.value
            }
            return latestVersion ?: ""
        }

        internal fun getReleaseResponse(response: String): String {
            val src = Regex("\"http.+?expanded_assets.+?\"").find(response)?.value?.removeSurrounding("\"") ?: ""
            return Fuel.get(src).response().second.toString()
        }

        internal fun getApkLink(response: String): String {
            return Regex("\".+?\\.apk\"").find(response)?.value?.removeSurrounding("\"") ?: ""
        }

        internal fun getApkTitle(response: String): String {
            return Regex(">.+?\\.apk<").find(response)?.value?.removeSurrounding(">", "<") ?: ""
        }
    }


}