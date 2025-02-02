import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
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

        // Get Android ID
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

        // Request current location
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

    private fun showCustomDialog() {
        if (!::uniqueCode.isInitialized || uniqueCode.isBlank()) {
            Toast.makeText(requireContext(), "Unique code is not available yet. Please try again later.", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_custom, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val closeButton: ImageView = dialogView.findViewById(R.id.btnClose)
        val code: TextView = dialogView.findViewById(R.id.textView6)
        val btnCopy: TextView = dialogView.findViewById(R.id.textView10)
        val btnShare: LinearLayout = dialogView.findViewById(R.id.btnShare)

        code.text = uniqueCode.uppercase()

        btnShare.setOnClickListener {
            shareCode(uniqueCode)
        }

        btnCopy.setOnClickListener {
            copyToClipboard(uniqueCode)
            Toast.makeText(requireContext(), "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
        }

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    @SuppressLint("ServiceCast")
    private fun copyToClipboard(uniqueCode: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Unique Code", uniqueCode)
        clipboard.setPrimaryClip(clip)
    }

    private fun shareCode(uniqueCode: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Here is my unique code: $uniqueCode")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return requireContext().contentResolver.let {
            Settings.Secure.getString(it, Settings.Secure.ANDROID_ID)
        }
    }


}
