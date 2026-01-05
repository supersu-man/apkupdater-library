# APKUpdater Library 📱

[![JitPack](https://jitpack.io/v/com.github.supersu-man/apkupdater-library.svg)](https://jitpack.io/#com.github.supersu-man/apkupdater-library)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A lightweight Android library to automatically check for the latest release in your GitHub repository and download the latest APK if a new update is available. Perfect for apps that release APK files with every GitHub release!

## ✨ Features

- 🔍 **Automatic Update Checks**: Seamlessly check for new releases on GitHub
- 📥 **One-Click Downloads**: Download the latest APK with a simple method call
- 🎯 **Flexible Versioning**: Supports both X.Y and X.Y.Z versioning schemes
- 🚀 **Easy Integration**: Minimal setup required for your Android app
- 🛡️ **Lightweight**: No bloat, just the essentials

## 📋 Requirements

- **Android API Level**: 21+ (Android 5.0+)
- **Version Matching**: Your app's `versionName` in Gradle must match the GitHub release tag (optional 'v' prefix allowed)

### Version Format Examples

**Gradle (`build.gradle`):**
```gradle
versionName "1.2"
versionName "1.2.3"
```

**GitHub Release Tags:**
```
v1.2 or 1.2
v1.2.3 or 1.2.3
```

## 🚀 Installation

### Step 1: Add JitPack Repository

Add the JitPack repository to your root `build.gradle` file:

```gradle
allprojects {
    repositories {
        // ... other repositories
        maven { url "https://jitpack.io" }
    }
}
```

### Step 2: Add Dependency

Add the APKUpdater library to your app's `build.gradle` file:

```gradle
dependencies {
    implementation 'com.github.supersu-man:apkupdater-library:v2.2.0'
}
```

## 💡 Usage

### Basic Implementation

Here's a simple example of how to integrate APKUpdater into your app:

```kotlin
import com.github.supersu_man.apkupdater.ApkUpdater

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Run update check in background thread
        Thread {
            val githubReleasesUrl = "https://github.com/your-username/your-repo/releases/latest"
            val updater = ApkUpdater(this@MainActivity, githubReleasesUrl)

            // Configure versioning (false for X.Y, true for X.Y.Z)
            updater.threeNumbers = false  // or true

            checkForUpdates(updater)
        }.start()
    }

    private fun checkForUpdates(updater: ApkUpdater) {
        val updateAvailable = updater.isNewUpdateAvailable()
        if (updateAvailable == true) {
            // New update available!
            runOnUiThread {
                // Show update dialog or notification
                showUpdateDialog()
            }
            updater.requestDownload()
        } else if (updateAvailable == false) {
            // App is up to date
            runOnUiThread {
                Toast.makeText(this, "App is up to date", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Unable to check for updates
            runOnUiThread {
                Toast.makeText(this, "Unable to check for updates", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpdateDialog() {
        // Implement your update notification UI
        Toast.makeText(this, "New update available! Downloading...", Toast.LENGTH_LONG).show()
    }
}
```

## 📚 API Reference

### `ApkUpdater(Context, String)`

Creates a new ApkUpdater instance.

- **Parameters:**
  - `context`: Android Context (Activity or Application)
  - `githubReleasesUrl`: URL to your GitHub releases page (e.g., `https://github.com/user/repo/releases/latest`)

### `isNewUpdateAvailable(): Boolean?`

Synchronously checks if a new update is available by comparing app version with latest GitHub release tag.

- **Returns:** `true` if update available, `false` if up to date, `null` if unable to determine

### `requestDownload()`

Downloads the latest APK from the GitHub release. The APK will be saved to the device's downloads directory.

### `threeNumbers: Boolean`

Configures the versioning scheme:
- `false`: X.Y format (e.g., "1.2")
- `true`: X.Y.Z format (e.g., "1.2.3")

## 🔧 Configuration

### Permissions

Add these permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

**Note:** For Android 6.0+ (API 23+), request runtime permissions for storage access.

## 📖 Libraries Used

This library uses:
- [Fuel](https://github.com/kittinunf/fuel) - Lightweight HTTP networking library for Android

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋 Support

If you find this library helpful, please ⭐ star the repository!

For issues or questions, please [open an issue](https://github.com/supersu-man/apkupdater-library/issues) on GitHub.