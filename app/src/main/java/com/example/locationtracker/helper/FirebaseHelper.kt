import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class FirebaseHelper {

    private val functions = FirebaseFunctions.getInstance()

    // Function to call getChildDetailsWithPing Cloud Function
    fun getChildDetails(parentID: String, childID: String) {
        val data = hashMapOf(
            "parentID" to parentID,
            "childID" to childID
        )

        // Call the Cloud Function
        functions.getHttpsCallable("getChildDetailsWithPing")
            .call(data)
            .addOnSuccessListener { result ->
                // Success
                val response = result.getData() as Map<String, Any>
                val success = response["success"] as Boolean
                val message = response["message"] as String
                val details = response["data"] as Map<String, Any>

                if (success) {
                    Log.d("FirebaseHelper", "Details fetched successfully: $details")
                } else {
                    Log.d("FirebaseHelper", "Error: $message")
                }
            }
            .addOnFailureListener { exception ->
                // Error handling
                Log.e("FirebaseHelper", "Error fetching child details: ", exception)
            }
    }
}
