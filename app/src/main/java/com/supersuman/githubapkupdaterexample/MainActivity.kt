package com.supersuman.githubapkupdaterexample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.supersuman.githubapkupdater.Updater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch {
            val updater = Updater(this@MainActivity,"https://github.com/supersu-man/Macronium/releases/latest")
            checkForUpdates(updater)
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