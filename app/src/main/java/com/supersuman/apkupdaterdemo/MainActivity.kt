package com.supersuman.apkupdaterdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.supersuman.apkupdater.ApkUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        val url = "https://github.com/supersu-man/macronium-android/releases/latest"

        coroutineScope.launch {
            try {
                val updater = ApkUpdater(this@MainActivity, url)
                updater.threeNumbers = true
                checkForUpdates(updater)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private fun checkForUpdates(updater: ApkUpdater) {
        if (updater.isNewUpdateAvailable() == true) {
            updater.requestDownload()
        }
    }

}