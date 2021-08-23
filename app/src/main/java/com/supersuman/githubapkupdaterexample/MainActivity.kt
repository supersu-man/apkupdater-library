package com.supersuman.githubapkupdaterexample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.supersuman.githubapkupdater.Updater

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runOnUiThread {
            val updater = Updater(this,"https://github.com/supersu-man/Macronium/releases/latest")
            if (updater.hasPermissionsGranted()){
                checkForUpdates(updater)
            } else{
                updater.requestMyPermissions{
                    checkForUpdates(updater)
                }
            }
        }
    }

    private fun checkForUpdates(updater: Updater){
        if (updater.isInternetConnection()){
            updater.init()
            updater.isNewUpdateAvailable {
                updater.requestDownload()
            }
        }
    }

}