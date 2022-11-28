package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentClassAdminBinding

class classAdminFragment : Fragment() {

    private lateinit var binding : FragmentClassAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Variable Declarations
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_class_admin, container, false)

        return binding.root
    }

}