package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentCourtManageBinding

class CourtAdminFragment : Fragment(){

    private lateinit var binding: FragmentCourtManageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Variable Declarations
        val adminActivityView = (activity as AdminDashboardActivity)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_court_manage, container, false)
        adminActivityView.replaceFragment(CourtManageAdminFragment(), R.id.courtAdminLayout)

        // Directing Navigation
        binding.courtAdminNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.court_management -> adminActivityView.replaceFragment(CourtManageAdminFragment(), R.id.courtAdminLayout)
                R.id.court_pending -> adminActivityView.replaceFragment(CourtPendingAdminFragment(), R.id.courtAdminLayout)
            }
            true
        }
        return binding.root
    }
}