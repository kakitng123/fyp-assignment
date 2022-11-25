package com.example.fyp_booking_application.backend

data class productData(
    val product_id: String ?= null,
    val product_name: String ?= null,
    val product_image: String ?= null,
    val product_category: String ?= null,
    val product_desc: String ?= null,
    val product_price: Double = 0.00,
    val product_qty: Int = 0
)
