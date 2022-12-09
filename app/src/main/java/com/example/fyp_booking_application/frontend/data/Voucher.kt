package com.example.fyp_booking_application.frontend.data

data class Voucher(
    val voucherID: String ?= null,
    val voucherTitle: String ?= null,
    val voucherDetail: String ?= null,
    val voucherExpiredDate: String ?= null,
    val voucherStatus: String ?= null,
)
