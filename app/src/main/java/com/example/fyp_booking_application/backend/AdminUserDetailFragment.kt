package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminUserDetailBinding

class AdminUserDetailFragment : Fragment() {

    private lateinit var binding: FragmentAdminUserDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_user_detail, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)
        adminActivityView.setTitle("USER DETAIL")

        setFragmentResultListener("toUserDetail"){ _, bundle ->
            // val user = bundle.getString("toUserDetail")

        }

        return binding.root
    }
}