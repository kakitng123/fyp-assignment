package com.example.fyp_booking_application.frontend.data

data class BookingData(
    val bookingID: String ?= null,
    val bookingCourt: String ?= null,
    val bookingRate: String ?= null,
    val bookingPhone: String ?= null,
    val bookingDate: String ?= null,
    val bookingTime: String ?= null,
    val spinner: String ?= null,
    //val bookingPrice: Double = 0.0,
    val bookingStatus: String ?= null,
    val userID: String ?= null
)
