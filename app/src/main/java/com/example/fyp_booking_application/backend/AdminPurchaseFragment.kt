package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminPurchaseBinding

class AdminPurchaseFragment : Fragment() {

    private lateinit var binding: FragmentAdminPurchaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin_purchase, container, false)
        val adminActivityView = (activity as AdminDashboardActivity)

        binding.btnTestDetail.setOnClickListener(){
            adminActivityView.replaceFragment(AdminPurchaseDetailFragment(), R.id.adminLayout)
        }

        return binding.root
    }
}