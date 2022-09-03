package cdmochi.nearbymessaging.widget

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceDiscoveredViewHolder(private val view: TextView): RecyclerView.ViewHolder(view) {
    fun bind(item: String?) {
        item?.let { view.text = item }
    }
}
