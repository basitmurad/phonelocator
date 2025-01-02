import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class UserData(
    val time: String,
    val date: String,
    val batteryPercentage: String,
    val latlon: LatLng
)


//private fun getCurrentLocation() {
//    if (!isAdded) return
//
//    if (ActivityCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        return
//    }
//    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//        if (location != null) {
//            currentLatLng = LatLng(location.latitude, location.longitude)
//
//        } else {
//            Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show()
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//fun getCurrentTime(): String {
//    val current = LocalDateTime.now()
//    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss") // Format for time
//    return current.format(timeFormatter)
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//fun getCurrentDate(): String {
//    val current = LocalDateTime.now()
//    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Format for date
//    return current.format(dateFormatter)
//}
//
//private fun initializeCurrentLatLng() {
//    // Default initialization or fetch from location services
//    currentLatLng = LatLng(0.0, 0.0) // Default value
//    // TODO: Replace with actual location logic
//}