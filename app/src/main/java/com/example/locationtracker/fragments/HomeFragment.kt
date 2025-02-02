import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.locationtracker.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import android.Manifest
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.locationtracker.api.RetrofitClient
import com.example.locationtracker.connectionModels.QRCodeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var buttonOpen: Button

    private lateinit var androidId: String
    private lateinit var uniqueCode: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var checker: Checker

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mapView = view.findViewById(R.id.mapView)
        buttonOpen = view.findViewById(R.id.btnOpenDialoge)
        checker = Checker(requireContext().applicationContext)



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        androidId = getAndroidId()


        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        buttonOpen.setOnClickListener {
            showCustomDialog()
        }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        if (!isAdded) return

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            } else {
                Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }
    }




    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return requireContext().contentResolver.let {
            Settings.Secure.getString(it, Settings.Secure.ANDROID_ID)
        }
    }

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_custom, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val qrImageView: ImageView = dialogView.findViewById(R.id.btnClose) // Ensure this exists in `dialog_custom.xml`
        val code: TextView = dialogView.findViewById(R.id.textView6)
        val btnCopy: TextView = dialogView.findViewById(R.id.textView10)
        val btnShare: LinearLayout = dialogView.findViewById(R.id.btnShare)

        // Generate and display the QR code in the dialog
        generateQRCode1(androidId, qrImageView)

        btnShare.setOnClickListener {
            shareCode(uniqueCode)
        }

        btnCopy.setOnClickListener {
            copyToClipboard(uniqueCode)
            Toast.makeText(requireContext(), "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
        }

        qrImageView.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    // Generate QR code and display it in the ImageView
    @SuppressLint("HardwareIds")
    fun generateQRCode1(deviceId: String, imageView: ImageView) {
        RetrofitClient.apiService.generateQRCode(deviceId).enqueue(object : Callback<QRCodeResponse> {
            override fun onResponse(call: Call<QRCodeResponse>, response: Response<QRCodeResponse>) {
                if (response.isSuccessful) {
                    val qrCodeResponse = response.body()
                    if (qrCodeResponse != null && qrCodeResponse.success) {
                        uniqueCode = qrCodeResponse.qrCode // Store the unique code
                        Log.d("QRCodeData", "Received QR Code: $uniqueCode")
                        Log.d("QRCodeData", "QR Code Length: ${uniqueCode.length}")

                        // Remove prefix if it exists
                        if (uniqueCode.startsWith("data:image/png;base64,")) {
                            uniqueCode = uniqueCode.substring("data:image/png;base64,".length)
                        }

                        // Decode Base64 to Bitmap
                        if (isValidBase64(uniqueCode)) {
                            try {
                                val imageBytes = Base64.decode(uniqueCode, Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                imageView.setImageBitmap(bitmap)
                            } catch (e: IllegalArgumentException) {
                                Log.e("QRCodeError", "Invalid Base64 string", e)
                            }
                        } else {
                            Log.e("QRCodeError", "Received an invalid Base64 string")
                        }
                    } else {
                        Log.e("QRCodeError", "API Response Error: ${qrCodeResponse?.message}")
                    }
                } else {
                    Log.e("QRCodeError", "Server Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<QRCodeResponse>, t: Throwable) {
                Log.e("QRCode", "Failure: ${t.message}")
            }
        })
    }

    // Copy the unique code to the clipboard
    private fun copyToClipboard(uniqueCode: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Unique Code", uniqueCode)
        clipboard.setPrimaryClip(clip)
    }

    // Share the unique code
    private fun shareCode(uniqueCode: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Here is my unique code: $uniqueCode")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun isValidBase64(base64: String): Boolean {
        return try {
            Base64.decode(base64, Base64.DEFAULT)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }


}
