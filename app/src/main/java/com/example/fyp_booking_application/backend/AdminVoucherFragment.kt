package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminVoucherBinding

class AdminVoucherFragment : Fragment() {

    private lateinit var binding: FragmentAdminVoucherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_voucher, container, false)

        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Voucher Management")

        adminView.replaceFragment(AdminVoucherManageFragment(), R.id.voucherLayout)

        binding.voucherNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_voucher_history -> adminView.replaceFragment(AdminVoucherHistoryFragment(), R.id.voucherLayout)
                R.id.nav_voucher_manage -> adminView.replaceFragment(AdminVoucherManageFragment(), R.id.voucherLayout)
            }
            true
        }

        return binding.root
    }
}