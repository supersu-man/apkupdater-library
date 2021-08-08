# GitHubAPKUpdater-Library
Checks for the latest release in the GitHub repo and if latest apk release is found, it downloads the apk for you.

If you also release APK files with every new release on GitHub just like me, this could be a very useful Library.

# Requirements
- Your App Version name in Gradle and your release Tag version on GitHub needs to be same (adding 'v' is optional in GitHub Tag version).

Gradle:
```
versionName "1.0"
```
GitHub Tag version for release:
```
v1.0 or 1.0
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
implementation 'com.github.supersu-man:GitHubAPKUpdater-Library:v1.1'
```

# Usage
### Example
```
thread {
    val updater = Updater(this,"https://github.com/supersu-man/Macronium/releases/latest")
    if (updater.hasPermissionsGranted()){
        checkForUpdates(updater)
    } else{
        updater.requestMyPermissions{
            checkForUpdates(updater)
        }
    }
}
```
```
private fun checkForUpdates(updater: Updater){
    if (updater.isInternetConnection()){
        updater.init()
        updater.isNewUpdateAvailable {
            updater.requestDownload()
        }
    }
}
```
### Explanation
- Initializing (use your repo link like this)
```
val updater = Updater(this,"https://github.com/supersu-man/Macronium/releases/latest")
```
- Checking for required Permissions
```
updater.hasPermissionsGranted()
```
- Requesting required permissions (it's WRITE_EXTERNAL_STORAGE here)
```
updater.requestMyPermissions {
    //Your code after user gives the permission 
}
```
- Checking for Internet Connection
```
updater.isInternetConnection()
```
- Checking for New Update (if the version name and latest tag version don't match it detects as new update)
```
updater.init()
updater.isNewUpdateAvailable {
    //Your code when new update is found in the repo
}
```
- Download latest APK
```
updater.requestDownload()
```
# Libraries Used 
- [khttp](https://github.com/ascclemens/khttp#readme)
- [Dexter](https://github.com/Karumi/Dexter#readme)
