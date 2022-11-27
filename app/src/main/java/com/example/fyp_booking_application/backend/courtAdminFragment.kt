package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentCourtManageBinding

class courtAdminFragment : Fragment(){

    private lateinit var binding: FragmentCourtManageBinding
    private lateinit var toggle : ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adminActivityView = (activity as AdminDashboardActivity)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_court_manage, container, false)
        adminActivityView.replaceFragment(courtManageAdmin(), R.id.courtMainLayout)

        binding.courtAdminNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.court_management -> adminActivityView.replaceFragment(courtManageAdmin(), R.id.courtMainLayout)
                R.id.court_pending -> adminActivityView.replaceFragment(courtPendingAdmin(), R.id.courtMainLayout)
            }
            true
        }
        return binding.root
    }
}