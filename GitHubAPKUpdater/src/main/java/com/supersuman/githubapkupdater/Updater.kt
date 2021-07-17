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
import kotlin.concurrent.thread


class Updater(private val context: Activity, private val url: String) {
    lateinit var response: String
    lateinit var title : String

    fun init(){
        thread {
            response = get(url).text
        }.join()
    }

    fun getLatestVersionFromGitHuB(): String {
        var version = ""
        thread {
            val temp2 = response.split("label-latest")[1].split("list-style-none")[1]
            version = temp2.split("version")[1].split("\"")[1]
        }.join()
        return version
    }

    fun isNewUpdateAvailable(function: () -> Unit) {
        val versionName = context.packageManager
            .getPackageInfo(context.packageName, 0).versionName
        if (getLatestVersionFromGitHuB() != versionName) {
            function()
        }
    }

    fun getAPKTitle(): String {
        val temp2 = response.split("release-main-section")[1].split("Box Box--condensed")[1]
        val temp3 = temp2.split("<a href=\"")
        var temp4 = ""
        for (i in temp3) {
            if (".apk" in i) {
                temp4 = i
            }
        }
        val temp5 = temp4.split(
            url.replace("latest", "").replace("https://github.com", "")
        )[1].split("\"")[0]
        val dlink = url.replace("latest", temp5)
        title = dlink.split("download/")[1].split("/")[1]
        return title
    }

    fun requestDownload() {
        thread {
            val temp2 = response.split("release-main-section")[1].split("Box Box--condensed")[1]
            val temp3 = temp2.split("<a href=\"")
            var temp4 = ""
            for (i in temp3) {
                if (".apk" in i) {
                    temp4 = i
                }
            }
            val temp5 = temp4.split(
                url.replace("latest", "").replace("https://github.com", "")
            )[1].split("\"")[0]
            val dlink = url.replace("latest", temp5)
            title = dlink.split("download/")[1].split("/")[1]
            val request = DownloadManager.Request(Uri.parse(dlink))
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            request.setDescription("")
            request.setTitle(title)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
            downloadManager.enqueue(request)
        }.join()
    }

    fun requestMyPermissions(function: () -> Unit){
        Dexter.withContext(context)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    function()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    println(response)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    println(permission)
                    println(token)
                }
            }).check()

    }



    fun hasPermissionsGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }else{
            return true
        }
    }

    fun isInternetConnection(): Boolean {
        return try {
            thread {
                get("https://www.google.com/")
            }.join()
            true
        } catch (e:Exception){
            false
        }
    }

}