package com.example.deliverytrackingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter class to bind the list of deliveries to RecyclerView
class DeliveryAdapter(
    private val deliveryList: List<Delivery>,
    private val onTrackClick: (Delivery) -> Unit // Callback function when track button is clicked
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    // ViewHolder class holds the views for each item
    inner class DeliveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val deliveryIdText: TextView = itemView.findViewById(R.id.deliveryIdText)  // Delivery ID text view
        private val expectedDateText: TextView = itemView.findViewById(R.id.expectedDateText) // Expected Date text view
        private val trackButton: Button = itemView.findViewById(R.id.trackButton)  // Track button

        // Function to bind data to the item views
        fun bind(delivery: Delivery) {
            // Set the delivery ID and expected date text
            deliveryIdText.text = "Delivery ID: ${delivery.deliveryId}"
            expectedDateText.text = "Expected Date: ${delivery.expectedDeliveryDate}"

            // Set the click listener on the Track button
            trackButton.setOnClickListener {
                onTrackClick(delivery) // Call the callback with the current delivery item
            }
        }
    }

    // Inflate the item view layout (make sure you have 'item_delivery.xml' layout)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_delivery, parent, false) // Inflate item layout
        return DeliveryViewHolder(view) // Return the ViewHolder
    }

    // Bind data to the ViewHolder for a specific position
    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(deliveryList[position]) // Bind data for each delivery
    }

    // Return the total number of items in the delivery list
    override fun getItemCount(): Int = deliveryList.size
}
