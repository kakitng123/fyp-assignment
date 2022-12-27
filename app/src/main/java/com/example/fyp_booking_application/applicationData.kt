package com.example.fyp_booking_application

// Class Management Data (Complete)
data class ClassData (
    val classID: String ?= null,
    val className: String ?= null,
    val classDesc: String ?= null,
    val classPrice: Double ?= 0.00,
    val classSlot: HashMap<String, Any> ?= null,
    val entitledCoach: String ?= null,
)

// Court Management Data (Complete)
data class CourtData (
    val courtID: String ?= null,
    val courtName: String ?= null,
    val courtPrice: Double ?= 0.00,
    val courtSlots: HashMap<String, Any> ?= null
)
data class CourtDataTimeslot (
    val timeslot: String ?= null,
    val availability: Boolean ?= null
)

// EnrollData (Complete)
data class EnrollData(
    val enrollID: String ?= null,
    val enrollDate: String ?= null,
    val enrollTime: String ?= null,
    val enrollClassName: String ?= null,
    val enrollPrice: Double ?= null,
    val enrollStatus: String ?= null,
    val classID: String ?= null,
    val userID: String ?= null,
)

// Booking Data (Complete)
data class BookingData(
    val bookingID: String ?= null,
    val bookingCourt: String ?= null,
    val bookingPhone: String ?= null,
    val bookingDate: String ?= null,
    val bookingTime: String ?= null,
    val bookingStatus: String ?= null,
    val bookingPayment: Double ?= 0.00,
    val userID: String ?= null
)

// Product Data
data class ProductData(
    val productID: String ?= null,
    val productName: String ?= null,
    val productImage: String ?= null,
    val productCategory: String ?= null,
    val productDesc: String ?= null,
    val productPrice: Double = 0.00,
    val productQty: Int = 0
)

// Purchase Data
data class PurchaseData(
    val purchaseID: String ?= null,
    val purchaseName: String ?= null,
    val purchaseQty: Int = 0,
    val purchaseStatus: String ?= null,
    val purchasePrice: Double = 0.00,
    val purchaseDate: String ?= null,
    val purchaseTime: String ?= null,
    val productID: String ?= null,
    val userID: String ?= null
)

// Coach Data
data class CoachData(
    val coachID: String ?= null,
    val coachName: String ?= null,
    val coachEmail: String ?= null,
    val coachPhone: String ?= null,
    val coachExp: String ?= null,
    val coachImage: String ?= null,
)

// Training Class Data
data class TrainingClassData(
    val classID: String ?= null,
    val className: String ?= null,
    val classPrice: Double = 0.00,
    val classDate: String ?= null,
    val classTime: String ?= null,
)

// TestUserData
data class TestUserData(
    val userID: String ?= null,
    val username: String ?= null,
    val password: String ?= null,
    val email: String ?= null,
    val phone: String ?= null,
    val gender: String ?= null,
    val imgID: String ?= null,
    @field:JvmField
    val isSubscribed: Boolean ?= null
)

// Notification Data (Not Complete)
data class NotificationData (
    val notifyID: String ?= null,
    val userID: String ?= null,
    val notifyTitle: String ?= null,
    val notifyMessage: String ?= null,
    val referralCode: String ?= null
    // Might need to add Date/Time
)

// Voucher Data (Not Complete)
data class VoucherData (
    val voucherID: String ?= null,
    val voucherImage: String ?= null,
    val voucherTitle: String ?= null,
    val voucherMessage: String ?= null,
    val pointsRequired: Int = 0,
    val voucherCode: String ?= null,
    val voucherDiscount: Double = 0.00,
    val userID: String ?= null
)

data class WalletData(
    val topUpID : String ?= null,
    val topUpTitle: String ?= null,
    val topUpAmount: Double = 0.00,
    val balance: Double = 0.00,
    val topUpDesc: String ?= null,
    val topUpStatus: String ?= null
)


