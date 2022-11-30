package com.example.fyp_booking_application.frontend.data

data class BookedData(
    val bookedCourt: String ?= null,
    val bookedRate: Int = 0,
    val bookedPhone: String ?= null,
    val bookedDate: String ?= null,
    val bookedTime: String ?= null,
)
