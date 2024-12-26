import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtracker.R

class DeviceAdapter(private val itemList: List<ItemData>) : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each item
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_list_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Bind data to the views
        holder.circleText.text = currentItem.circleText
        holder.title.text = currentItem.title
        holder.date.text = currentItem.date
        holder.location.text = currentItem.location


        // Handle button click
        holder.checkButton.setOnClickListener {
            // Handle button click here
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
    }

    // ItemData class for holding the data for each item
    data class ItemData(
        val circleText: String,
        val title: String,
        val connectedNow: String,
        val date: String,
        val lastMap: String,
        val location: String,
    )
}
