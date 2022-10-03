# GitHubAPKUpdater-Library
Checks for the latest release in the GitHub repo and if latest apk release is found, it downloads the apk for you.

If you also release APK files with every new release on GitHub just like me, this could be a very useful Library.

# Requirements
- Your App Version name in Gradle and your release Tag version on GitHub needs to be SAME (adding 'v' is optional in github tag).

Gradle:
```
versionName "X.Y"
or
versionName "X.Y.Z"
```
GitHub Tag for release:
```
vX.Y or X.Y
or
vX.Y.Z or X.Y.Z
```
- Add these lines in top level build.gradle
```
allprojects {
    repositories {
        /// ....
        maven { url "https://jitpack.io" }
    }
}
```
- Add this line in build.gradle
```
implementation 'com.github.supersu-man:apkupdater-library:v2.0.0'
```

# Usage
### Simple implementation [here](https://github.com/supersu-man/apkupdater-library/blob/main/app/src/main/java/com/supersuman/apkupdaterdemo/MainActivity.kt)
### Example
```
thread {
    val url = "https://github.com/supersu-man/macronium-android/releases/latest"
    val updater = ApkUpdater(this@MainActivity, url)
    updater.threeNumbers = false //if versioning is X.Y
    updater.threeNumbers = true  //if versioning is X.Y.Z
    checkForUpdates(updater)
}
```
```
private fun checkForUpdates(updater: ApkUpdater) {
    if (updater.isInternetConnection() && updater.isNewUpdateAvailable() == true) {
        updater.requestDownload()
    }
}
```
### Explanation
- Initializing (use your repo link like this)
```
val updater = ApkUpdater(this@MainActivity, url)
```
- Checking for Internet connection
```
updater.isInternetConnection()
```
- Checking for new update (if the versionName and latest tag don't match it detects as new update)
```
updater.isNewUpdateAvailable {
    //Your code when new update is found in the repo
}
```
- Download latest apk
```
updater.requestDownload()
```
# Libraries Used 
- [khttp](https://github.com/ascclemens/khttp#readme)