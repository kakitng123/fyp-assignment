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
                    val testing = document.toObject(BookingDataTesting::class.java)

                    binding.tfBDetailID.setText(testing?.bookingID.toString())
                    binding.tfBDetailDate.setText(testing?.bookingDate.toString())
                    binding.tfBDetailTime.setText(testing?.bookingTime.toString())
                    binding.tfBDetailCourt.setText(testing?.courtID.toString())
                    binding.tfBDetailStatus.setText(testing?.status.toString())
                    binding.tfBDetailUser.setText(testing?.userID.toString())
                }
                else Log.d("FETCHING DOCUMENT", "INVALID DOCUMENT")
            }.addOnFailureListener { e ->
                Log.e("FETCHING DOCUMENT", "INVALID DOCUMENT", e)
            }
        }

        binding.button.setOnClickListener{
            adminActivityView.replaceFragment(AdminBookingFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}