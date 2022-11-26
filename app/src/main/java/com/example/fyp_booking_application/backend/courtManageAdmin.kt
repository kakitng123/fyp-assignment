package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentCourtManageAdminBinding
import com.example.fyp_booking_application.databinding.FragmentCourtManageBinding

class courtManageAdmin : Fragment() {

    private lateinit var binding: FragmentCourtManageAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_court_manage_admin, container, false)

        return binding.root
    }

}