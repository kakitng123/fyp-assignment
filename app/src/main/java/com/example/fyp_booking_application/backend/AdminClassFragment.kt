package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminDashboardActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentAdminClassBinding

class AdminClassFragment : Fragment(){

    private lateinit var binding: FragmentAdminClassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_class, container, false)
        val adminView = (activity as AdminDashboardActivity)
        adminView.setTitle("Training Class Management")

        adminView.replaceFragment(AdminClassManageFragment(), R.id.classAdminLayout)
        binding.classNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_class_manage -> adminView.replaceFragment(AdminClassManageFragment(), R.id.classAdminLayout)
                R.id.nav_class_pending -> adminView.replaceFragment(AdminEnrollFragment(), R.id.classAdminLayout)
                R.id.nav_class_history -> adminView.replaceFragment(AdminEnrollHistoryFragment(), R.id.classAdminLayout)
            }
            true
        }
        return binding.root
    }
}