//import android.Manifest
//import android.app.Activity
//import android.content.pm.PackageManager
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
//class PermissionHelper(private val activity: Activity) {
//
//    companion object {
//        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
//        const val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 1002
//    }
//
//    fun checkLocationPermissions(): Boolean {
//        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//    }
//
//    fun requestLocationPermissions() {
//        ActivityCompat.requestPermissions(
//            activity,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
//            LOCATION_PERMISSION_REQUEST_CODE
//        )
//    }
//
//    fun checkBackgroundLocationPermission(): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true
//        }
//    }
//
//    fun requestBackgroundLocationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }
//    }
//
//
//
//}


import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity) {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 1002
        const val ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE = 1003
    }

    // Check location permissions
    fun checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Request location permissions
    fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    // Check background location permission
    fun checkBackgroundLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // Request background location permission
    fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Check activity recognition (motion tracking) permission
    fun checkActivityRecognitionPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // Request activity recognition permission
    fun requestActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Open auto-start or protected apps settings for specific manufacturers
    fun openAutoStartSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }

    // Redirect users to motion tracking or protected app settings
    fun openProtectedAppSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_SETTINGS // General settings; you can adjust for specific OEMs
        activity.startActivity(intent)
    }

    // Handle auto-start for specific OEMs
    fun handleAutoStartSettings() {
        val manufacturer = Build.MANUFACTURER.lowercase()
        when (manufacturer) {
            "xiaomi" -> {
                val intent = Intent("miui.intent.action.POWER_HIDE_MODE_APP_LIST")
                intent.putExtra("package_name", activity.packageName)
                activity.startActivity(intent)
            }
            "huawei" -> {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
                activity.startActivity(intent)
            }
            "oppo" -> {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                )
                activity.startActivity(intent)
            }
            "vivo" -> {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                )
                activity.startActivity(intent)
            }
            else -> {
                openAutoStartSettings()
            }
        }
    }
}
