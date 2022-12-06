package com.example.fyp_booking_application.backend

// ProductData is done
data class ProductData(
    val productID: String ?= null,
    val productName: String ?= null,
    val productImage: String ?= null,
    val productCategory: String ?= null,
    val productDesc: String ?= null,
    val productPrice: Double = 0.00,
    val productQty: Int = 0
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

data class CoachData (
    val coachEmail: String ?= null,
    val coachExperience: String ?= null,
    val coachID: String ?= null,
    val coachName: String ?= null,
    val coachPhone: String ?= null
)

data class ClassData (
    val trainingClassDate: String ?= null,
    val trainingClassName: String ?= null,
    val trainingClassPrice: Int = 0,
    val trainingClassTime: String ?= null
)



