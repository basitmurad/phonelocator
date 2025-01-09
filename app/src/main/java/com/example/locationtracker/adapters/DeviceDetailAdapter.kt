import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R
import com.google.android.gms.maps.model.LatLng

class DeviceDetailAdapter(private val itemList: List<ItemData>,
                          private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DeviceDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_list_layout, parent, false)
        return ViewHolder(itemView)
    }

    interface OnItemClickListener {
        fun onButtonClick(latLng: LatLng)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Bind data to the views
        holder.circleText.text = currentItem.circleText
        holder.title.text = currentItem.title
        holder.date.text = currentItem.date

        val latLongObj = currentItem.latLong
        val battery = currentItem.battery
        val batteryPercentage = currentItem.battery.toIntOrNull() ?: 0 // Default to 0 if the value is invalid
        setBatteryIcon(holder.batteryImage, batteryPercentage)

        holder.checkButton.setOnClickListener {
            itemClickListener.onButtonClick(latLongObj)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // ViewHolder class to hold references to the views
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val circleText: TextView = view.findViewById(R.id.circleText)
        val title: TextView = view.findViewById(R.id.mobileName)
        val date: TextView = view.findViewById(R.id.date)
        val location: TextView = view.findViewById(R.id.locationName)
        val checkButton: TextView = view.findViewById(R.id.button)
        val batteryImage: ImageView = view.findViewById(R.id.batteryImage)
    }

    // ItemData class for holding the data for each item
    data class ItemData(
        val circleText: String,
        val battery: String,
        val title: String,
        val connectedNow: String,
        val date: String,
        val lastMap: String,
        val latLong: LatLng
    )

    // Function to set the battery icon based on the percentage
    private fun setBatteryIcon(batteryImage: ImageView, batteryPercentage: Int) {
        when (batteryPercentage) {
            in 76..100 -> batteryImage.setImageResource(R.drawable.baseline_battery_charging_full_24)   // Full battery icon
            in 51..75 -> batteryImage.setImageResource(R.drawable.baseline_battery_6_bar_24)   // Half battery icon
            in 26..50 -> batteryImage.setImageResource(R.drawable.baseline_battery_5_bar_24) // Quarter battery icon
            in 0..25 -> batteryImage.setImageResource(R.drawable.baseline_battery_4_bar_24)    // Low battery icon
        }
    }
}
