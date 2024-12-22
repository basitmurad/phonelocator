
package com.example.locationtracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.databinding.ActivityDeviceNameBinding

class DeviceNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceNameBinding

    private lateinit var editext:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding object
        binding = ActivityDeviceNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default text to TextView
        val deviceInfo = getDeviceInfo()
        binding.textView.text = Editable.Factory.getInstance().newEditable(deviceInfo)
    }

    private fun getDeviceInfo(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val androidVersion = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        val deviceName = "${Build.DEVICE} (${Build.BRAND})"
        val androidId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )

        return """
           
            $deviceName
        """.trimIndent()
    }

    fun navigateToNextScreen(view: View) {
        val intent = Intent(this, PermissionActivity::class.java)
        startActivity(intent)
    }
}
