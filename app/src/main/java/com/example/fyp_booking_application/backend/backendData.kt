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
data class CoachData (
    val coachEmail: String ?= null,
    val coachExperience: String ?= null,
    val coachID: String ?= null,
    val coachName: String ?= null,
    val coachPhone: String ?= null
)

// BELOW THIS LINE IS EXAMPLE DATA FOR ADAPTER (REAL DATA WE DISCUSS)
data class CourtPendingData (
    val court_id: String ?= null,
    val bookingTime: String ?= null,
    val bookingDate: String ?= null,
    val players: Int = 0
)


