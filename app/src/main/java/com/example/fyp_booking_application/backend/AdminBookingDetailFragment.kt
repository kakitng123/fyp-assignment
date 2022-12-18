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
        val adminActivityView = (activity as AdminDashboardActivity)

        setFragmentResultListener("toAdminBookingDetails") {_, bundle ->
            val bookingID = bundle.getString("toAdminBookingDetails")
            databaseRef = FirebaseFirestore.getInstance()
            val docRef = databaseRef.collection("court_testing").document(bookingID.toString())

            docRef.get().addOnSuccessListener { document ->
                if(document != null){
                    val booking = document.toObject(BookingDataTesting::class.java)

                    binding.tfBDetailID.setText(booking?.bookingID.toString())
                    binding.tfBDetailDate.setText(booking?.bookingDate.toString())
                    binding.tfBDetailTime.setText(booking?.bookingTime.toString())
                    binding.tfBDetailCourt.setText(booking?.courtID.toString())
                    binding.tfBDetailStatus.setText(booking?.status.toString())
                    binding.tfBDetailUser.setText(booking?.userID.toString())
                }
                else Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
            }
        }

        binding.tvBackBookingDetail.setOnClickListener{
            adminActivityView.replaceFragment(AdminBookingFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}