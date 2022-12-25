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
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Admin Profile")

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
        databaseRef.collection("Purchases").get().addOnSuccessListener { documents ->
            for (document in documents){
                purchaseSales += document["purchasePrice"].toString().toDouble()
            }
            binding.tfPurchaseSales.text = "RM $purchaseSales"
        }
        return binding.root
    }
}