package com.example.fyp_booking_application.frontend.data

data class NotificationData(
    val notificationID: String ?= null,
    val notificationTitle: String ?= null,
    val notificationDetail: String ?= null,
    val notificationDate: String ?= null,
    val notificationTime: String ?= null,
    val notificationStatus: String ?= null,
)
