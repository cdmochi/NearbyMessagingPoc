package cdmochi.segment.control.nearbymessaging.widget

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NearbyDiscoveryAdapter(private val items: MutableList<String> = mutableListOf()): RecyclerView.Adapter<DeviceDiscoveredViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceDiscoveredViewHolder {
        return DeviceDiscoveredViewHolder(
            TextView(parent.context)
        )
    }

    override fun onBindViewHolder(holder: DeviceDiscoveredViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int) = items.getOrNull(position)
    fun addItem(item: String) {
        items.add(item)
        notifyItemInserted(items.size)
    }
    fun removeItem(item: String) {
        val position = items.indexOf(item)
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
