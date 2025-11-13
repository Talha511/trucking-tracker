package com.example.truckingtracker.models


data class BookingDetails(
    val vehicleName: String,
    val vehicleType: String,
    val vehicleImageRes: Int,
    val pickupAddress: String,
    val dropOffAddress: String,
    val date: String,
    val time: String
)
