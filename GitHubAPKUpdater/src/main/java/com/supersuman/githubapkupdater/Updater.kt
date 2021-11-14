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


class Updater(private val context: Activity, private val url: String) {
    lateinit var response: String

    fun init(){
        response = get(url).text
    }

    private fun getLatestVersionFromGitHuB(): String {
        return try {
            val document = Jsoup.parse(response)
            var version = document.getElementsByAttributeValue("class", "ml-1")[0].text().trim()
            if ("v" in version){
                version = version.replace("v","")
            }
            version
        }catch (e : Exception){
            ""
        }
    }

    fun isNewUpdateAvailable(function: () -> Unit) {
        val currentVersionName = context.packageManager
            .getPackageInfo(context.packageName, 0).versionName
        val latestVersion  = getLatestVersionFromGitHuB()
        if ( latestVersion != "" && latestVersion!= currentVersionName) {
            function()
        }
    }


    fun requestDownload() {
        val document = Jsoup.parse(response)
        val list = document.getElementsByAttributeValue("class", "box-row")
        var downloadLink = ""
        var title = ""
        for(i in list){
            if ("apk" in i.text()){
                title = i.getElementsByTag("a")[0].text()
                downloadLink = i.getElementsByTag("a")[0].attr("href").toString()
            }
        }
        val request = DownloadManager.Request(Uri.parse("https://github.com/$downloadLink"))
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        request.setDescription("")
        request.setTitle(title)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
        downloadManager.enqueue(request)
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