package com.example.truckingtracker.repos

import com.example.truckingtracker.R
import com.example.truckingtracker.screen.ServiceItem

class HomeRepo {

    // Map your uploaded files to hypothetical R.drawable IDs
    fun getServiceDataList() = listOf(
        // Ensure these map correctly to your resource files
        ServiceItem("Small Pickup", R.drawable.pickupsmall_vector),
        ServiceItem("Medium Pickup", R.drawable.pickupmedium_vector),
        ServiceItem("Heavy Vehicle", R.drawable.pickupheavy_vector),
        ServiceItem("Train Cargo", R.drawable.pickuptrain_vector),
        ServiceItem("Air Cargo", R.drawable.pickupair_vector)
    )
}