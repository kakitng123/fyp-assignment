package com.example.fyp_booking_application.backend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fyp_booking_application.AdminActivity
import com.example.fyp_booking_application.MainActivity
import com.example.fyp_booking_application.R
import com.example.fyp_booking_application.databinding.FragmentProductBinding

class productFragment : Fragment() {

    private lateinit var binding : FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Variable Declaration
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        val adminactivityview = (activity as AdminActivity)

        // Jumping Fragments
        binding.btnManage.setOnClickListener(){
            adminactivityview.replaceFragment(manageProductFragment())
        }

        return binding.root
    }
}