package com.example.fyp_booking_application.backend

// ProductData (Complete)
data class ProductData(
    val productID: String ?= null,
    val productName: String ?= null,
    val productImage: String ?= null,
    val productCategory: String ?= null,
    val productDesc: String ?= null,
    val productPrice: Double = 0.0,
    val productQty: Int = 0
)
// CourtData (Complete)
data class CourtData (
    val courtID: String ?= null,
    val courtName: String ?= null,
    val courtSlots: MutableList<CourtTimeslots> ?= null
)
// CourtTimeslots (Complete) - Linked to CourtData
data class CourtTimeslots (
    val availability: Boolean ?= null,
    val timeslot: String ?= null
)
// ClassData (Complete)
data class ClassData2 (
    val classID: String ?= null,
    val className: String ?= null,
    val classDesc: String ?= null,
    val classPrice: Double ?= null,
    val classDate: String ?= null,
    val classTime: String ?= null,
    val entitledCoach: String ?= null,
)
// CoachData (Complete)
data class CoachData (
    val coachID: String ?= null,
    val coachName: String ?= null,
    val coachEmail: String ?= null,
    val coachExp: String ?= null,
    val coachPhone: String ?= null
)

// BELOW THIS LINE IS EXAMPLE DATA FOR ADAPTER (REAL DATA WE DISCUSS)
data class NotificationData (
    val notifyID: String ?= null,
    val userID: String ?= null,
    val notifyTitle: String ?= null,
    val notifyMessage: String ?= null,
    val referralCode: String ?= null
)

data class BookingDataTesting (
    val bookingID: String ?= null,
    val courtID: String ?= null,
    val userID: String ?= null,
    val bookingTime: String ?= null,
    val bookingDate: String ?= null,
    val status: String ?= null
)

data class ClassData1 (
    val trainingClassDate: String ?= null,
    val trainingClassName: String ?= null,
    val trainingClassPrice: Int = 0,
    val trainingClassTime: String ?= null
)

data class PurchaseData (
    val transactID: String ?= null,
    val productName: String ?= null,
    val productQty: Int = 0,
    val transactAmt: Double = 0.0 // ProductPrice*QTY
)

data class UserData2 (
    val userID: String ?= null,
    val username: String ?= null,
    val email: String ?= null,
    val password: String ?= null,
    val userType: String ?= null
)

data class TestCourtData (
    val courtID: String ?= null,
    val courtName: String ?= null,
    val courtSlots: HashMap<String, Any> ?= null
)

data class TestCourtData2 (
    val timeslot: String ?= null,
    val availability: Boolean ?= null
)







