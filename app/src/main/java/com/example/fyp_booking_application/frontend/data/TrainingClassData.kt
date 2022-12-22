package com.example.fyp_booking_application.frontend.data

data class TrainingClassData(
    val classID: String ?= null,
    val className: String ?= null,
    val classDesc: String ?= null,
    val classPrice: Double = 0.00,
    val classDate: String ?= null,
    val classTime: String ?= null,
)
