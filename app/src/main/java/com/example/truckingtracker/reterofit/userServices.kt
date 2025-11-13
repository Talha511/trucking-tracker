package com.example.truckingtracker.reterofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @POST("login")
    suspend fun login(
        @Header("x-api-key") apiKey: String = "8624D6BADAD500DC5E5199FA66DA8468526942",
        @Body request: LoginRequest
    ): Response<LoginResponse>


    @POST("register")
    suspend fun register(
        @Header("x-api-key") apiKey: String = "8624D6BADAD500DC5E5199FA66DA8468526942",
        @Body request: RegisterRequest
    ): Response<LoginResponse>

    @POST("change-password")
    suspend fun changePassword(
        @Header("x-api-key") apiKey: String = "8624D6BADAD500DC5E5199FA66DA8468526942",
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

        @GET("booking_history/{userId}")
        suspend fun getBookingHistory(
            @Header("x-api-key") apiKey: String = "8624D6BADAD500DC5E5199FA66DA8468526942",
            @Header("Authorization") token: String,
            @Path("userId") userId: Int
        ): Response<BookingHistoryResponse>
}
