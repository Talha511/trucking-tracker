package com.example.truckingtracker.reterofit

// Request body
data class LoginRequest(
    val email: String,
    val password: String
)
data class RegisterRequest(
    val name: String,
    val email: String,
    val cnic: String,
    val password: String
)

data class ChangePasswordRequest(
    val old_password: String,
    val new_password: String,
    val new_password_confirmation: String
)
data class ChangePasswordResponse(
    val success: Boolean?,
    val msg: String?
)


// Response
data class LoginResponse(
    val status: Boolean,
    val token: String?,
    val expires_in: String?,
    val user: User?
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val cnic: String,
    val email_verified_at: String?,
    val role: Int,
    val created_at: String,
    val updated_at: String
)

data class BookingHistoryResponse(
    val status: Boolean,
    val message: String?,
    val data: List<BookingItem>?
)

data class BookingItem(
    val id: Int?,
    val user_id: Int?,
    val vehicle_id: Int?,
    val pickup_location: String?,
    val dropoff_location: String?,
    val vehicle_name: String?,
    val vehicle_type: String?,
    val pickup: String?,
    val dropoff: String?,
    val date: String?,
    val time: String?,
    val weight: String?,
    val goods: String?,
    val payer: String?,
    val labour_count: String?,
    val time_relaxation: String?,
    val additional_info: String?
)