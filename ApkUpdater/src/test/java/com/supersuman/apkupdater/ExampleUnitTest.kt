package com.supersuman.apkupdater

import khttp.get
import org.junit.Test

import org.junit.Assert.*

class ApkUpdaterUnitTest {

    @Test
    fun regex3() {
        val response = get("https://github.com/supersu-man/macronium-android/releases/tag/v2.3.0").text
        val version = Regex("[0-9]+\\.[0-9]+\\.[0-9]+").find(response)?.value
        assertEquals("2.3.0", version)
        val src = Regex("\"http.+?expanded.+?\"").find(response)?.value?.removeSurrounding("\"")
        assertEquals("https://github.com/supersu-man/macronium-android/releases/expanded_assets/v2.3.0", src)
        val res = get(src.toString()).text
        val apkLink = Regex("\".+?\\.apk\"").find(res)?.value?.removeSurrounding("\"")
        assertEquals("/supersu-man/macronium-android/releases/download/v2.3.0/app-release-signed-v2.3.0.apk", apkLink)
        val title = Regex(">.+?\\.apk<").find(res)?.value?.removeSurrounding(">", "<")
        assertEquals("app-release-signed-v2.3.0.apk", title)
    }

    @Test
    fun regex2() {
        val response = get("https://github.com/supersu-man/macronium-android/releases/tag/v1.4").text
        val version = Regex("[0-9]+\\.[0-9]+").find(response)?.value
        assertEquals("1.4", version)
    }

}