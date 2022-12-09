package com.example.fyp_booking_application.frontend.data

data class TopupData(
    val topupID: String ?= null,
    val topupDetail: String ?= null,
    val topupAmount: Double = 0.00,
    val topupStatus: String ?= null,
)
