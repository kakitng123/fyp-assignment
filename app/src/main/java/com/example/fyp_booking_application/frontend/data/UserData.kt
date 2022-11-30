package com.example.fyp_booking_application.frontend.data

data class UserData(
    val email: String ,
    val username: String ,
    val password: String ,
    val confirmPassword: String ,
    val isAdmin: Int,
)
