package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminNotifManageBinding

class AdminNotifManageFragment : Fragment() {

    private lateinit var binding: FragmentAdminNotifManageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_notif_manage, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        // TESTING BUTTON
        binding.btnAddNotif.setOnClickListener {
            adminActivityView.replaceFragment(AdminNotifAddFragment(), R.id.notificationLayout)
        }

        return binding.root
    }

}