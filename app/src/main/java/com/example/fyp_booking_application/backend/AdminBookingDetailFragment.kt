package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.BookingData
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminBookingDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminBookingDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminBookingDetailBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_booking_detail, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Booking Details")

        setFragmentResultListener("toAdminBookingDetails") {_, bundle ->
            val bookingID = bundle.getString("toAdminBookingDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("Bookings").document(bookingID.toString())

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val booking = document.toObject(BookingData::class.java)

                    binding.bookingIDField.setText(booking?.bookingID.toString())
                    binding.bookingCourtNameField.setText(booking?.bookingCourt.toString())
                    binding.bookingDateField.setText(booking?.bookingDate.toString())
                    binding.bookingTimeField.setText(booking?.bookingTime.toString())
                    binding.bookingStatusField.setText(booking?.bookingStatus.toString())
                    binding.bookingUserIDField.setText(booking?.userID.toString())
                    binding.bookingAmountField.setText(booking?.bookingPayment.toString())
                }
                else Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }.addOnFailureListener { e -> Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e) }
        }

        binding.tvBackBookingDetail.setOnClickListener{
            adminView.replaceFragment(AdminBookingFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}