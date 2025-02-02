import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.locationtracker.R
import com.example.locationtracker.databinding.ActivityShowDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class ShowDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityShowDetailBinding
    private lateinit var mMap: GoogleMap
    private lateinit var deviceAdapter: DeviceDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the location name passed from the previous activity
        val locationName = intent.getStringExtra("location_name")

        // Show a toast message for the location name (optional)
        Toast.makeText(this, "Location: $locationName", Toast.LENGTH_SHORT).show()

        // Get the MapFragment and initialize it
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView2) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Callback method to be called once the map is ready
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Get the location data (you can use actual lat/long or mock data)
        val locationName = intent.getStringExtra("location_name")

        // You may want to get actual latitude/longitude based on the location name
        // For now, let's use some dummy coordinates
        val locationLatLng = LatLng(37.7749, -122.4194)  // Example: San Francisco coordinates

        // Add a marker on the map for the location
        mMap.addMarker(MarkerOptions().position(locationLatLng).title(locationName))

        // Move the camera to the location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 12f))
    }






}
