package com.supersuman.apkupdater

import org.junit.Assert.assertEquals
import org.junit.Test

class ApkUpdaterUnitTest {

    @Test
    fun regex3() {
        val url = "https://github.com/supersu-man/macronium-android/releases/tag/v2.3.0"
        val response = ApkUpdater.getResponse(url)

        val version = ApkUpdater.getLatestVersion(true, response)
        assertEquals("2.3.0", version)

        val releaseResponse = ApkUpdater.getReleaseResponse(response)

        val apkLink = ApkUpdater.getApkLink(releaseResponse)
        assertEquals("/supersu-man/macronium-android/releases/download/v2.3.0/app-release-signed-v2.3.0.apk", apkLink)

        val title = ApkUpdater.getApkTitle(releaseResponse)
        assertEquals("app-release-signed-v2.3.0.apk", title)
    }

    @Test
    fun regex2() {
        val url = "https://github.com/supersu-man/macronium-android/releases/tag/v1.4"
        val response = ApkUpdater.getResponse(url)

        val version = ApkUpdater.getLatestVersion(false, response)
        assertEquals("1.4", version)

        val releaseResponse = ApkUpdater.getReleaseResponse(response)

        val apkLink = ApkUpdater.getApkLink(releaseResponse)
        assertEquals("/supersu-man/macronium-android/releases/download/v1.4/Macronium-v1.4.apk", apkLink)

        val title = ApkUpdater.getApkTitle(releaseResponse)
        assertEquals("Macronium-v1.4.apk", title)
    }

}