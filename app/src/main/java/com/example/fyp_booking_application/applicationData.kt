package com.example.fyp_booking_application

// Class Management Data (Complete)
data class ClassData (
    val classID: String ?= null,
    val className: String ?= null,
    val classDesc: String ?= null,
    val classPrice: Double ?= null,
    val classSlot: HashMap<String, Any> ?= null,
    val entitledCoach: String ?= null,
)


// Court Management Data (Complete)
data class CourtData (
    val courtID: String ?= null,
    val courtName: String ?= null,
    val courtPrice: Double ?= null,
    val courtSlots: HashMap<String, Any> ?= null
)
data class CourtDataTimeslot (
    val timeslot: String ?= null,
    val availability: Boolean ?= null
)

// EnrollData (Changed enrollPrice from String to Double)
data class EnrollData(
    val enrollID: String ?= null,
    val enrollDate: String ?= null,
    val enrollTime: String ?= null,
    val enrollClassName: String ?= null,
    val enrollPrice: String ?= null,
    val enrollStatus: String ?= null,
    val enrollCoach: String ?= null,
    val classID: String ?= null,
    val userID: String ?= null,
)

// Booking Data (Scuffed version but usable)
// Going to remove bookingRate + bookingPhone + spinner
// Will Change bookingPayment to Double after this
data class BookingData(
    val bookingID: String ?= null,
    val bookingCourt: String ?= null,
    val bookingPhone: String ?= null,
    val bookingDate: String ?= null,
    val bookingTime: String ?= null,
    val bookingStatus: String ?= null,
    val bookingPayment: Double ?= null,
    val userID: String ?= null
)

//Product Data
data class ProductData(
    val productID: String ?= null,
    val productName: String ?= null,
    val productImage: String ?= null,
    val productCategory: String ?= null,
    val productDesc: String ?= null,
    val productPrice: Double = 0.00,
    val productQty: Int = 0
)

//Product Data
data class PurchaseData(
    val purchaseID: String ?= null,
    val purchaseName: String ?= null,
    val purchaseQty: Int = 0,
    val purchaseStatus: String ?= null,
    val purchasePayment: Double = 0.00,
    val productID: String ?= null,
    val userID: String ?= null
)

//Coach Data
data class CoachData(
    val coachID: String ?= null,
    val coachName: String ?= null,
    val coachEmail: String ?= null,
    val coachPhone: String ?= null,
    val coachExp: String ?= null,
    val coachImage: String ?= null,
)

//Training Class Data
data class TrainingClassData(
    val classID: String ?= null,
    val className: String ?= null,
    val classPrice: Double = 0.00,
    val classDate: String ?= null,
    val classTime: String ?= null,
)






