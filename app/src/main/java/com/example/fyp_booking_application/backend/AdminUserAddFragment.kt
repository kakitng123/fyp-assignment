package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminUserAddBinding

class AdminUserAddFragment : Fragment() {

    private lateinit var binding: FragmentAdminUserAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_user_add, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("ADD USER")

        // TAKE COACH/CLASS/PRODUCT ADD FRAGMENT

        return binding.root
    }
}