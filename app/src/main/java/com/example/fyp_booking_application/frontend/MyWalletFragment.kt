package com.example.fyp_booking_application.frontend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fyp_booking_application.UserDashboardActivity
import com.example.fyp_booking_application.databinding.FragmentMyWalletBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyWalletFragment : Fragment() {
    private lateinit var auth: FirebaseAuth //get the shared instance of the FirebaseAuth object
    private lateinit var fstore: FirebaseFirestore //get the shared instance of the FirebaseAuth object
    private lateinit var binding : FragmentMyWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyWalletBinding.inflate(layoutInflater)
        val userView = (activity as UserDashboardActivity)
        userView.setTitle("My Wallet")

        binding.historyBookingBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(BookingCourtHistoryFragment())
        }
        binding.historyEnrollBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(EnrollClassHistoryFragment())
        }
        binding.historyPurchaseBtn.setOnClickListener {
            Toast.makeText(context, "View Successfully", Toast.LENGTH_SHORT).show()
            userView.replaceFragment(PurchaseProductHistoryFragment())
        }
        return binding.root
    }
}
