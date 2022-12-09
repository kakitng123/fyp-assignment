package com.example.fyp_booking_application.frontend.adapter

data class PaymentData(
    val paymentID: String ?= null,
    val paymentDetail: String ?= null,
    val paymentDate: String ?= null,
    val paymentTime: String ?= null,
    val paymentType: String ?= null,
    val paymentTotal: Double = 0.00,
    val paymentStatus: String ?= null,
)
