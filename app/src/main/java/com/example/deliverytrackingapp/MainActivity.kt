package com.example.deliverytrackingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var deliveryRecyclerView: RecyclerView
    private lateinit var deliveryAdapter: DeliveryAdapter
    private val deliveryList = mutableListOf<Delivery>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and Adapter
        deliveryRecyclerView = findViewById(R.id.deliveryRecyclerView)
        deliveryAdapter = DeliveryAdapter(deliveryList) { delivery ->
            // Handle track button click - Navigate to MapsActivity
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("deliveryId", delivery.deliveryId) // Pass deliveryId to MapsActivity
            startActivity(intent)
        }

        // Set the RecyclerView's layout manager and adapter
        deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        deliveryRecyclerView.adapter = deliveryAdapter

        // Fetch deliveries from Firestore
        fetchDeliveriesFromFirestore()
    }

    private fun fetchDeliveriesFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        // Fetch the "deliveries" collection from Firestore
        db.collection("deliveries")
            .get()
            .addOnSuccessListener { documents ->
                // Loop through the documents and add them to the deliveryList
                for (document in documents) {
                    val delivery = document.toObject(Delivery::class.java)
                    deliveryList.add(delivery)
                }
                // Notify the adapter to update the UI with the new data
                deliveryAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Log any errors that occur while fetching data from Firestore
                Log.w("MainActivity", "Error getting documents: ", exception)
            }
    }
}
