package com.example.truckingtracker.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.truckingtracker.reterofit.BookingItem
import com.example.truckingtracker.reterofit.ChangePasswordRequest
import com.example.truckingtracker.reterofit.ChangePasswordResponse
import com.example.truckingtracker.reterofit.LoginRequest
import com.example.truckingtracker.reterofit.LoginResponse
import com.example.truckingtracker.reterofit.RegisterRequest
import com.example.truckingtracker.reterofit.RetrofitHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _loginResponse = MutableStateFlow<Response<LoginResponse>?>(null)
    val loginResponseX: StateFlow<Response<LoginResponse>?> = _loginResponse

    private val _regResponse = MutableStateFlow<Response<LoginResponse>?>(null)
    val regResponse: StateFlow<Response<LoginResponse>?> = _regResponse

    private val _changepasswordResponse = MutableStateFlow<Response<ChangePasswordResponse>?>(null)
    val changepasswordResponse: StateFlow<Response<ChangePasswordResponse>?> = _changepasswordResponse

    // --- SharedPreferences Helpers ---

    fun saveToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPref.edit().putString("token", token).apply()
    }

    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    fun deleteToken(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        sharedPref.edit().putString("token", null).apply()
        return sharedPref.getString("token", null) == null
    }

    // --- API Calls (Authentication) ---

    fun login(context: Context, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitHelper.api.login(
                    request = LoginRequest(email, password)
                )
                _loginResponse.value = response

                if (response.isSuccessful && response.body()?.token != null) {
                    val token = response.body()?.token!!
                    saveToken(context, token)
                    Log.d("AuthViewModel", "Token saved: $token")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error: ${e.message}")
            }
        }
    }

    fun register(data: RegisterRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitHelper.api.register(
                    request = data
                )
                _regResponse.value = response
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration error: ${e.message}")
            }
        }
    }

    fun changePassword(context: Context, data: ChangePasswordRequest) {
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    Log.e("AuthViewModel", "Token not found, cannot change password.")
                    // Optionally update a state flow for the UI to show an error
                    return@launch
                }

                val response = RetrofitHelper.api.changePassword(
                    token = "Bearer $token",
                    request = data
                )
                _changepasswordResponse.value = response

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Change password error: ${e.message}")
            }
        }
    }

    // --- API Calls (Booking History) ---
    suspend fun loadBookings(
        context: Context, // Added Context to access token
        userId: Int,
        onResult: (List<BookingItem>, String?) -> Unit
    ) {
        val token = getToken(context)
        if (token.isNullOrEmpty()) {
            onResult(emptyList(), "Authentication token missing. Please log in.")
            return
        }

        try {
            val response = RetrofitHelper.api.getBookingHistory(token = "Bearer $token", userId =  userId)

            if (response.isSuccessful && response.body()?.data != null) {
                onResult(response.body()!!.data!!, null)
            } else {
                onResult(emptyList(), response.body()?.message ?: "Failed to fetch bookings.")
            }
        } catch (e: Exception) {
            onResult(emptyList(), e.localizedMessage ?: "Network error.")
        }
    }
}