package com.example.fyp_booking_application.frontend.data

data class EnrollData(
    val enrollID: String ?= null,
    val enrollPax: Int = 0,
    val enrollDate: String ?= null,
    val enrollTime: String ?= null,
    val enrollRate: Int = 0,
    val enrollStatus: String ?= null,
)
