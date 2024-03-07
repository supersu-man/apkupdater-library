package com.supersuman.apkupdater

import com.github.kittinunf.fuel.Fuel
import org.junit.Test

import org.junit.Assert.*

class ApkUpdaterUnitTest {

    @Test
    fun regex3() {
        val response = Fuel.get("https://github.com/supersu-man/macronium-android/releases/tag/v2.3.0").response().second.toString()
        val version = Regex("[0-9]+\\.[0-9]+\\.[0-9]+").find(response)?.value
        assertEquals("2.3.0", version)
        val src = Regex("\"http.+?expanded.+?\"").find(response)?.value?.removeSurrounding("\"")
        assertEquals("https://github.com/supersu-man/macronium-android/releases/expanded_assets/v2.3.0", src)
        val res = Fuel.get(src.toString()).response().second.toString()
        val apkLink = Regex("\".+?\\.apk\"").find(res)?.value?.removeSurrounding("\"")
        assertEquals("/supersu-man/macronium-android/releases/download/v2.3.0/app-release-signed-v2.3.0.apk", apkLink)
        val title = Regex(">.+?\\.apk<").find(res)?.value?.removeSurrounding(">", "<")
        assertEquals("app-release-signed-v2.3.0.apk", title)
    }

    @Test
    fun regex2() {
        val response = Fuel.get("https://github.com/supersu-man/macronium-android/releases/tag/v1.4").response().second.toString()
        val version = Regex("[0-9]+\\.[0-9]+").find(response)?.value
        assertEquals("1.4", version)
    }

}