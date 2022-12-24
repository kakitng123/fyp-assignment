package com.example.fyp_booking_application.backend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeFragment : Fragment() {

    private lateinit var binding: FragmentAdminHomeBinding
    private lateinit var databaseRef: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_home, container, false)
        databaseRef = FirebaseFirestore.getInstance()
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("ADMIN PROFILE")

        // Add Date Picker to filter which day of transactions

        // TEST FEATURE
        var bookingSales = 0.0
        databaseRef.collection("Bookings").get().addOnSuccessListener { documents ->
            for (document in documents){
                bookingSales += document["bookingPayment"].toString().toDouble()
            }
            binding.tfBookingSales.text = "RM $bookingSales"
        }

        var purchaseSales = 0.0
        databaseRef.collection("purchase_testing1").get().addOnSuccessListener { documents ->
            for (document in documents){
                purchaseSales += document["transactAmt"].toString().toDouble()
            }
            binding.tfPurchaseSales.text = "RM $purchaseSales"
        }
        return binding.root
    }
}