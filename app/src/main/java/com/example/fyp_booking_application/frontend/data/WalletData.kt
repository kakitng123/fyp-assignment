package com.example.fyp_booking_application.frontend.data

data class WalletData(
    val walletID: String ?= null,
    val walletType: String ?= null,
    val walletBalance: Double = 0.00,
    val walletStatus: String ?= null,
)
