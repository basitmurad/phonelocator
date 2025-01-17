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
import com.google.firebase.Firebase
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions

class ShowDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityShowDetailBinding
    private lateinit var mMap: GoogleMap
    private lateinit var deviceAdapter: DeviceDetailAdapter
    private val functions = FirebaseFunctions.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getLocation.setOnClickListener {
            callGreetUserFunction()

        }
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


    private fun callGreetUserFunction() {
        val functions = Firebase.functions
        val data = hashMapOf("name" to "John Doe")

        functions
            .getHttpsCallable("greetUser")
            .call(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Access the result using getData() method
                    val result = task.result?.getData() as? Map<*, *>

                    // Check if result is not null and handle it
                    if (result != null) {
                        val message = result["message"] as? String
                        Log.d("FirebaseFunction", "Response: $message")
                    } else {
                        Log.e("FirebaseFunction", "No data in response")
                    }
                } else {
                    // Handle error
                    Log.e("FirebaseFunction", "Error calling function", task.exception)
                }
            }
    }


//    private fun callGreetUserFunction() {
//        val functions = Firebase.functions
//        val data = hashMapOf("name" to "John Doe")
//
//        functions
//            .getHttpsCallable("greetUser")
//            .call(data)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val result = task.result.getData();
//
//
//
//                    Log.d("FirebaseFunction", "Response" +result)
//                } else {
//                    // Handle error
//                    Log.e("FirebaseFunction", "Error calling function", task.exception)
//                }
//            }
//    }

}
