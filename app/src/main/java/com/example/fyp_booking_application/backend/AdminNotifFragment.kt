package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminNotifBinding

class AdminNotifFragment : Fragment() {

    private lateinit var binding: FragmentAdminNotifBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_notif, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Notification Management")

        adminView.replaceFragment(AdminNotifManageFragment(), R.id.notificationLayout)

        binding.notificationNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_notificationManage -> adminView.replaceFragment(AdminNotifManageFragment(), R.id.notificationLayout)
                R.id.nav_notificationHistory -> adminView.replaceFragment(AdminNotifHistoryFragment(), R.id.notificationLayout)
            }
            true
        }

        return binding.root
    }
}