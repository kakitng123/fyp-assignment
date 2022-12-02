package com.example.fyp_booking_application.backend

// ProductData is done
data class ProductData(
    val product_id: String ?= null,
    val product_name: String ?= null,
    val product_image: String ?= null,
    val product_category: String ?= null,
    val product_desc: String ?= null,
    val product_price: Double = 0.00,
    val product_qty: Int = 0
)
/*
data class CoachData (
    val coachEmail: String ?= null,
    val coachExperience: String ?= null,
    val coachID: String ?= null,
    val coachName: String ?= null,
    val coachPhone: String ?= null
)
 */

// BELOW THIS LINE IS EXAMPLE DATA FOR ADAPTER (REAL DATA WE DISCUSS)
data class CourtPendingData (
    val document_id: String ?= null,
    val court_id: String ?= null,
    val bookingTime: String ?= null,
    val bookingDate: String ?= null,
    val players: Int = 0,
    val status: String ?= null
)
data class CourtData (
    val courtID: String ?= null,
    val courtName: String ?= null,
    val courtSlots: MutableList<CourtTimeslots> ?= null
)

data class CourtTimeslots (
    val availability: Boolean ?= null,
    val timeslot: String ?= null
)



