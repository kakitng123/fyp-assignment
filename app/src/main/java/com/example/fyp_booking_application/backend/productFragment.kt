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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        val mainactivityview = (activity as AdminActivity)

        binding.btnAddProduct.setOnClickListener(){
            mainactivityview.replaceFragment(addProductFragment())
        }

        return binding.root
    }
}