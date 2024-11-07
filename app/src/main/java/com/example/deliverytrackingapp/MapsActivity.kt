package com.example.deliverytrackingapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.deliverytrackingapp.databinding.ActivityMapsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var locationListener: ListenerRegistration? = null
    private val firestore = FirebaseFirestore.getInstance()
    private val deliveryRef = firestore.collection("deliveries").document("yourDeliveryId") // Use actual delivery ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Optional: Handle refresh button clicks (for example, to refresh the location)
        binding.refreshButton.setOnClickListener {
            Toast.makeText(this, "Refreshing location...", Toast.LENGTH_SHORT).show()
            // Trigger refresh logic if needed
        }
    }

    /**
     * This callback is triggered when the map is ready to be used.
     * Here we can manipulate the map (e.g., add markers, move camera, etc.).
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set the map type (optional)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Set initial map camera position (optional)
        val initialLocation = LatLng(-34.0, 151.0) // Example coordinates
        mMap.addMarker(MarkerOptions().position(initialLocation).title("Delivery Vehicle"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))

        // Start real-time location updates
        startLocationUpdates()
    }

    /**
     * Start listening for real-time location updates from Firestore.
     * You will get the delivery vehicle's latest location and update the map accordingly.
     */
    private fun startLocationUpdates() {
        locationListener = deliveryRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Error getting location: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            // Ensure the document exists and contains latitude/longitude fields
            snapshot?.let {
                val lat = it.getDouble("latitude")
                val lon = it.getDouble("longitude")

                if (lat != null && lon != null) {
                    updateLocationOnMap(lat, lon)
                }
            }
        }
    }

    /**
     * Update the delivery vehicle's location on the map.
     * Clears any previous markers and adds a new marker at the current location.
     */
    private fun updateLocationOnMap(lat: Double, lon: Double) {
        val location = LatLng(lat, lon)
        mMap.clear() // Clear any previous markers
        mMap.addMarker(MarkerOptions().position(location).title("Delivery Vehicle"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f)) // Adjust zoom level
    }

    override fun onStop() {
        super.onStop()
        // Stop listening for updates when the activity is stopped
        locationListener?.remove()
    }
}
