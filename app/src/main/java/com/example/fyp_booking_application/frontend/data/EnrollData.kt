package com.example.fyp_booking_application.frontend.data

data class EnrollData(
    val enrollID: String ?= null,
    val enrollDate: String ?= null,
    val enrollTime: String ?= null,
    val enrollClassID: String ?= null,
    val enrollClassName: String ?= null,
    val enrollPrice: Double = 0.00,
    val enrollStatus: String ?= null,
    val enrollCoach: String ?= null,
    val userID: String ?= null,
)
