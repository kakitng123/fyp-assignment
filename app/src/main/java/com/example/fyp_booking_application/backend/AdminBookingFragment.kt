package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminBookingBinding

class AdminBookingFragment : Fragment() {

    private lateinit var binding: FragmentAdminBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin_booking, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        // Directing Navigation
        binding.bookingNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_bookingHistory -> adminActivityView.replaceFragment(AdminBookingHistoryFragment(), R.id.bookingLayout)
                R.id.nav_bookingPending -> adminActivityView.replaceFragment(AdminBookingPendingFragment(), R.id.bookingLayout)
            }
            true
        }

        return binding.root
    }
}