package com.example.deliverytrackingapp




data class Delivery(
    val deliveryId: String = "",
    val expectedDeliveryDate: String = "",
    val status: String = "",
    val latitude: Double = 0.0,  // Add latitude
    val longitude: Double = 0.0  // Add longitude
)
